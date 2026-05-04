package com.opositores.service;

import com.opositores.dto.OppositionStatsResponse;
import com.opositores.dto.StatsOverviewResponse;
import com.opositores.model.Opposition;
import com.opositores.model.TestResult;
import com.opositores.model.User;
import com.opositores.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final OppositionRepository oppositionRepository;
    private final TopicRepository topicRepository;
    private final TestRepository testRepository;
    private final TestResultRepository testResultRepository;
    private final UserService userService;
    private final OppositionService oppositionService;

    public StatsOverviewResponse getOverview() {
        User user = userService.getCurrentUser();
        Long userId = user.getId();

        List<Opposition> oppositions = oppositionRepository.findByUserId(userId);

        long totalTopics = 0;
        long studiedTopics = 0;

        for (Opposition o : oppositions) {
            totalTopics += topicRepository.countByOppositionId(o.getId());
            studiedTopics += topicRepository.countReviewedOrMasteredByOppositionId(o.getId());
        }

        long totalTests = testRepository.findByUserIdOrderByCreatedAtDesc(userId).size();
        Double avgScore = testResultRepository.findAverageScoreByUserId(userId);

        return StatsOverviewResponse.builder()
                .totalOppositions(oppositions.size())
                .totalTopics(totalTopics)
                .studiedTopics(studiedTopics)
                .totalTests(totalTests)
                .averageScore(avgScore)
                .build();
    }

    public OppositionStatsResponse getOppositionStats(Long oppositionId) {
        Opposition opposition = oppositionService.findOwned(oppositionId);
        User user = userService.getCurrentUser();

        long total = topicRepository.countByOppositionId(oppositionId);
        long reviewed = topicRepository.countReviewedOrMasteredByOppositionId(oppositionId);
        double progress = total > 0 ? (reviewed * 100.0 / total) : 0.0;

        List<TestResult> results = testResultRepository
                .findByOppositionIdAndUserIdOrderByEndTime(oppositionId, user.getId());

        List<OppositionStatsResponse.ScoreEntry> scoreHistory = results.stream()
                .filter(r -> r.getEndTime() != null)
                .map(r -> OppositionStatsResponse.ScoreEntry.builder()
                        .date(r.getEndTime().toLocalDate().toString())
                        .score(r.getScore())
                        .build())
                .collect(Collectors.toList());

        return OppositionStatsResponse.builder()
                .oppositionId(oppositionId)
                .oppositionName(opposition.getName())
                .totalTopics(total)
                .reviewedTopics(reviewed)
                .progressPercent(Math.round(progress * 100.0) / 100.0)
                .scoreHistory(scoreHistory)
                .build();
    }
}
