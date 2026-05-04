package com.opositores.service;

import com.opositores.dto.OppositionRequest;
import com.opositores.dto.OppositionResponse;
import com.opositores.model.Opposition;
import com.opositores.model.User;
import com.opositores.repository.OppositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OppositionService {

    private final OppositionRepository oppositionRepository;
    private final UserService userService;

    public List<OppositionResponse> getAll() {
        User user = userService.getCurrentUser();
        return oppositionRepository.findByUserId(user.getId())
                .stream().map(OppositionResponse::from).collect(Collectors.toList());
    }

    public OppositionResponse getById(Long id) {
        Opposition o = findOwned(id);
        return OppositionResponse.from(o);
    }

    public OppositionResponse create(OppositionRequest req) {
        User user = userService.getCurrentUser();
        Opposition o = Opposition.builder()
                .user(user)
                .name(req.getName())
                .scope(req.getScope())
                .targetExamDate(req.getTargetExamDate())
                .hoursPerWeek(req.getHoursPerWeek())
                .build();
        return OppositionResponse.from(oppositionRepository.save(o));
    }

    public OppositionResponse update(Long id, OppositionRequest req) {
        Opposition o = findOwned(id);
        o.setName(req.getName());
        o.setScope(req.getScope());
        o.setTargetExamDate(req.getTargetExamDate());
        o.setHoursPerWeek(req.getHoursPerWeek());
        return OppositionResponse.from(oppositionRepository.save(o));
    }

    public void delete(Long id) {
        Opposition o = findOwned(id);
        oppositionRepository.delete(o);
    }

    /** Verifica que la oposición pertenece al usuario actual */
    public Opposition findOwned(Long id) {
        User user = userService.getCurrentUser();
        return oppositionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Oposición no encontrada: " + id));
    }
}
