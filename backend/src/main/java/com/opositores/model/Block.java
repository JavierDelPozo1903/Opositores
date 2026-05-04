package com.opositores.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blocks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opposition_id", nullable = false)
    private Opposition opposition;

    @Column(nullable = false)
    private String name;

    /** Peso relativo en el examen, de 0.0 a 1.0 */
    @Builder.Default
    private Double weight = 1.0;
}
