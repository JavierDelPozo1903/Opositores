package com.opositores.service;

import com.opositores.dto.TestCreateRequest;
import com.opositores.dto.TestResultResponse;
import com.opositores.dto.TestSubmitRequest;
import com.opositores.model.*;
import com.opositores.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final TestResultRepository testResultRepository;
    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;
    private final BlockService blockService;
    private final OppositionService oppositionService;
    private final UserService userService;

    @Transactional
    public Test createTest(TestCreateRequest req) {
        User user = userService.getCurrentUser();
        Opposition opposition = oppositionService.findOwned(req.getOppositionId());

        // Resolvemos los topicIds a partir de topicIds o blockIds
        List<Long> topicIds = resolveTopicIds(req, opposition.getId());

        if (topicIds.isEmpty()) {
            throw new IllegalArgumentException("No hay temas disponibles para el test");
        }

        // Obtenemos preguntas aleatorias de esos temas
        List<Question> allQuestions = questionRepository.findRandomByTopicIds(topicIds);

        if (allQuestions.isEmpty()) {
            throw new IllegalArgumentException("No hay preguntas disponibles para los temas seleccionados");
        }

        // Limitamos al número solicitado
        int limit = Math.min(req.getNumQuestions(), allQuestions.size());
        List<Question> selectedQuestions = allQuestions.subList(0, limit);

        Test test = Test.builder()
                .user(user)
                .opposition(opposition)
                .mode(req.getMode())
                .numQuestions(limit)
                .timeLimitMinutes(req.getTimeLimitMinutes())
                .build();

        // Añadimos las preguntas al test con su orden
        List<TestQuestion> testQuestions = new ArrayList<>();
        for (int i = 0; i < selectedQuestions.size(); i++) {
            testQuestions.add(TestQuestion.builder()
                    .test(test)
                    .question(selectedQuestions.get(i))
                    .orderIndex(i)
                    .build());
        }
        test.setTestQuestions(testQuestions);

        return testRepository.save(test);
    }

    public Test getTest(Long testId) {
        User user = userService.getCurrentUser();
        return testRepository.findByIdAndUserId(testId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Test no encontrado: " + testId));
    }

    @Transactional
    public TestResultResponse submitTest(Long testId, TestSubmitRequest req) {
        Test test = getTest(testId);

        // Verificamos que no se haya enviado ya
        if (testResultRepository.findByTestId(testId).isPresent()) {
            throw new IllegalStateException("Este test ya fue enviado");
        }

        // Construimos un mapa questionId -> respuesta del usuario
        Map<Long, String> answerMap = req.getAnswers().stream()
                .collect(Collectors.toMap(
                        TestSubmitRequest.AnswerDto::getQuestionId,
                        a -> a.getSelectedOption() != null ? a.getSelectedOption() : ""));

        int correct = 0, incorrect = 0, blank = 0;
        List<TestAnswer> testAnswers = new ArrayList<>();

        for (TestQuestion tq : test.getTestQuestions()) {
            Question q = tq.getQuestion();
            String selected = answerMap.getOrDefault(q.getId(), "");
            boolean isBlank = selected == null || selected.isBlank();
            boolean isCorrect = !isBlank && q.getCorrectAnswer().equals(selected);

            if (isBlank) blank++;
            else if (isCorrect) correct++;
            else incorrect++;

            testAnswers.add(TestAnswer.builder()
                    .question(q)
                    .selectedOption(isBlank ? null : selected)
                    .isCorrect(isCorrect)
                    .build());
        }

        // Nota: aciertos - (fallos / 3) sobre el total, escala 0-10 normalizada a 0-100
        double rawScore = (double) correct - ((double) incorrect / 3.0);
        double maxScore = test.getNumQuestions();
        double score = Math.max(0, (rawScore / maxScore) * 100);

        TestResult result = TestResult.builder()
                .test(test)
                .user(test.getUser())
                .startTime(LocalDateTime.now().minusMinutes(10)) // aproximado
                .endTime(LocalDateTime.now())
                .score(Math.round(score * 100.0) / 100.0)
                .correctCount(correct)
                .incorrectCount(incorrect)
                .blankCount(blank)
                .build();

        testAnswers.forEach(a -> a.setTestResult(result));
        result.setAnswers(testAnswers);

        return TestResultResponse.from(testResultRepository.save(result));
    }

    private List<Long> resolveTopicIds(TestCreateRequest req, Long oppositionId) {
        if (req.getTopicIds() != null && !req.getTopicIds().isEmpty()) {
            return req.getTopicIds();
        }
        if (req.getBlockIds() != null && !req.getBlockIds().isEmpty()) {
            return topicRepository.findByBlockIdIn(req.getBlockIds())
                    .stream().map(Topic::getId).collect(Collectors.toList());
        }
        // Sin filtro: todos los temas de la oposición
        return topicRepository.findByOppositionIdOrderByPriorityDesc(oppositionId)
                .stream().map(Topic::getId).collect(Collectors.toList());
    }
}
