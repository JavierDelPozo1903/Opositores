package com.opositores.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opositores.model.*;
import com.opositores.repository.AIRequestRepository;
import com.opositores.repository.FlashcardRepository;
import com.opositores.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {

    @Qualifier("aiWebClient")
    private final WebClient aiWebClient;

    private final DocumentService documentService;
    private final UserService userService;
    private final AIRequestRepository aiRequestRepository;
    private final FlashcardRepository flashcardRepository;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    @Value("${ai.model}")
    private String model;

    @Value("${ai.max-tokens}")
    private int maxTokens;

    // ─── Resumen ─────────────────────────────────────────────────────────────

    public String generateSummary(Long documentId, String length) {
        Document document = documentService.findById(documentId);
        User user = userService.getCurrentUser();
        AIRequest aiReq = saveRequest(user, document, AIRequest.RequestType.SUMMARY);

        String text = documentService.getDocumentText(document);
        String lengthInstruction = switch (length != null ? length : "medium") {
            case "short"  -> "un resumen muy breve de 3-5 puntos clave";
            case "long"   -> "un resumen detallado con todos los conceptos importantes";
            default       -> "un resumen de extensión media con los puntos principales";
        };

        String prompt = """
                Eres un asistente experto en preparación de oposiciones en España.
                A continuación tienes el contenido de un documento de estudio.
                Genera %s en español, basándote ÚNICAMENTE en el texto proporcionado.

                TEXTO:
                %s
                """.formatted(lengthInstruction, text);

        String summary = callAI(prompt);
        updateRequest(aiReq, AIRequest.RequestStatus.SUCCESS, null);
        return summary;
    }

    // ─── Flashcards ───────────────────────────────────────────────────────────

    public List<Flashcard> generateFlashcards(Long documentId, int numCards) {
        Document document = documentService.findById(documentId);
        User user = userService.getCurrentUser();
        AIRequest aiReq = saveRequest(user, document, AIRequest.RequestType.FLASHCARDS);

        String text = documentService.getDocumentText(document);
        String prompt = """
                Eres un asistente experto en preparación de oposiciones en España.
                Genera exactamente %d flashcards de estudio basadas ÚNICAMENTE en el siguiente texto.
                Responde EXCLUSIVAMENTE con un JSON válido con esta estructura:
                {"flashcards":[{"question":"...","answer":"..."}]}

                TEXTO:
                %s
                """.formatted(numCards, text);

        String response = callAI(prompt);

        List<Flashcard> saved = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode cards = root.path("flashcards");
            for (JsonNode card : cards) {
                Flashcard fc = Flashcard.builder()
                        .topic(document.getTopic())
                        .question(card.path("question").asText())
                        .answer(card.path("answer").asText())
                        .build();
                saved.add(flashcardRepository.save(fc));
            }
            updateRequest(aiReq, AIRequest.RequestStatus.SUCCESS, null);
        } catch (Exception e) {
            log.error("Error parseando flashcards de IA", e);
            updateRequest(aiReq, AIRequest.RequestStatus.ERROR, null);
        }

        return saved;
    }

    // ─── Preguntas MCQ ────────────────────────────────────────────────────────

    public List<Question> generateMcqQuestions(Long documentId, int numQuestions, int difficulty) {
        Document document = documentService.findById(documentId);
        User user = userService.getCurrentUser();
        AIRequest aiReq = saveRequest(user, document, AIRequest.RequestType.MCQ);

        String text = documentService.getDocumentText(document);
        String prompt = """
                Eres un asistente experto en preparación de oposiciones en España.
                Genera exactamente %d preguntas tipo test basadas EXCLUSIVAMENTE en el siguiente texto.
                Nivel de dificultad: %d/5.
                NO inventes leyes, artículos ni conceptos que no estén en el texto.
                Responde EXCLUSIVAMENTE con un JSON válido con esta estructura:
                {"questions":[{"question":"...","options":["A","B","C","D"],"correct_index":0}]}

                TEXTO:
                %s
                """.formatted(numQuestions, difficulty, text);

        String response = callAI(prompt);

        List<Question> saved = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode questions = root.path("questions");
            for (JsonNode q : questions) {
                String optionsJson = objectMapper.writeValueAsString(q.path("options"));
                int correctIdx = q.path("correct_index").asInt(0);
                String correctAnswer = q.path("options").get(correctIdx) != null
                        ? q.path("options").get(correctIdx).asText() : "";

                Question question = Question.builder()
                        .topic(document.getTopic())
                        .source(Question.QuestionSource.AI)
                        .type(Question.QuestionType.MCQ)
                        .questionText(q.path("question").asText())
                        .options(optionsJson)
                        .correctAnswer(correctAnswer)
                        .build();
                saved.add(questionRepository.save(question));
            }
            updateRequest(aiReq, AIRequest.RequestStatus.SUCCESS, null);
        } catch (Exception e) {
            log.error("Error parseando preguntas MCQ de IA", e);
            updateRequest(aiReq, AIRequest.RequestStatus.ERROR, null);
        }

        return saved;
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    /** Llama a la API de IA (compatible con formato OpenAI chat completions) */
    private String callAI(String prompt) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("max_tokens", maxTokens);
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        try {
            JsonNode response = aiWebClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            return response.path("choices").get(0)
                    .path("message").path("content").asText();
        } catch (Exception e) {
            log.error("Error llamando a la API de IA", e);
            throw new RuntimeException("Error en la llamada a la API de IA: " + e.getMessage());
        }
    }

    private AIRequest saveRequest(User user, Document doc, AIRequest.RequestType type) {
        AIRequest req = AIRequest.builder()
                .user(user)
                .document(doc)
                .requestType(type)
                .status(AIRequest.RequestStatus.PENDING)
                .build();
        return aiRequestRepository.save(req);
    }

    private void updateRequest(AIRequest req, AIRequest.RequestStatus status, Integer tokens) {
        req.setStatus(status);
        req.setTokensUsed(tokens);
        aiRequestRepository.save(req);
    }
}
