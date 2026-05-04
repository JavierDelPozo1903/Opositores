package com.opositores.service;

import com.opositores.dto.PlanRequest;
import com.opositores.dto.StudySessionResponse;
import com.opositores.model.Opposition;
import com.opositores.model.StudySessionPlan;
import com.opositores.model.Topic;
import com.opositores.repository.StudySessionPlanRepository;
import com.opositores.repository.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanningService {

    private final StudySessionPlanRepository sessionPlanRepository;
    private final TopicRepository topicRepository;
    private final OppositionService oppositionService;

    /**
     * Genera sesiones de estudio diarias desde startDate hasta targetExamDate.
     * Reparte el tiempo priorizando temas por mayor prioridad y dificultad.
     * Los fines de semana cuentan como días disponibles.
     */
    @Transactional
    public List<StudySessionResponse> generatePlan(Long oppositionId, PlanRequest req) {
        Opposition opposition = oppositionService.findOwned(oppositionId);

        // Actualizamos los datos de la oposición si se proporcionan
        opposition.setTargetExamDate(req.getTargetExamDate());
        opposition.setHoursPerWeek(req.getHoursPerWeek());

        // Eliminamos sesiones previas para regenerar
        sessionPlanRepository.deleteByOppositionId(oppositionId);

        // Calculamos minutos por día (7 días/semana)
        int minutesPerDay = (int) ((req.getHoursPerWeek() * 60) / 7.0);

        // Ordenamos temas por prioridad desc, luego dificultad desc
        List<Topic> topics = topicRepository.findByOppositionIdOrderByPriorityDesc(oppositionId);

        // Generamos los días entre startDate y targetExamDate
        List<LocalDate> studyDays = generateStudyDays(req.getStartDate(), req.getTargetExamDate());

        List<StudySessionPlan> sessions = new ArrayList<>();
        int topicIndex = 0;

        for (LocalDate day : studyDays) {
            // Asignamos el siguiente tema no completado (rotación circular)
            Topic topicForDay = null;
            if (!topics.isEmpty()) {
                topicForDay = topics.get(topicIndex % topics.size());
                topicIndex++;
            }

            StudySessionPlan session = StudySessionPlan.builder()
                    .opposition(opposition)
                    .date(day)
                    .plannedMinutes(minutesPerDay)
                    .topic(topicForDay)
                    .status(StudySessionPlan.SessionStatus.PENDING)
                    .build();

            sessions.add(sessionPlanRepository.save(session));
        }

        return sessions.stream().map(StudySessionResponse::from).collect(Collectors.toList());
    }

    public List<StudySessionResponse> getPlan(Long oppositionId, LocalDate from, LocalDate to) {
        oppositionService.findOwned(oppositionId);
        return sessionPlanRepository
                .findByOppositionIdAndDateBetweenOrderByDateAsc(oppositionId, from, to)
                .stream().map(StudySessionResponse::from).collect(Collectors.toList());
    }

    public StudySessionResponse updateStatus(Long sessionId, StudySessionPlan.SessionStatus status) {
        StudySessionPlan session = sessionPlanRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Sesión no encontrada: " + sessionId));

        // Verificamos que pertenece al usuario actual
        oppositionService.findOwned(session.getOpposition().getId());

        session.setStatus(status);
        return StudySessionResponse.from(sessionPlanRepository.save(session));
    }

    /** Genera todos los días del rango, excluyendo opcionalmente festivos (no implementado) */
    private List<LocalDate> generateStudyDays(LocalDate start, LocalDate end) {
        List<LocalDate> days = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            // Excluimos domingos si el usuario estudia solo L-S; aquí incluimos todos por defecto
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }
}
