package kr.ontherec.api.domain.place.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@RequiredArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class RefundPolicy {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(updatable = false, nullable = false)
    private Place place;

    @Column(updatable = false, nullable = false)
    private Duration due;

    @Column(updatable = false, nullable = false)
    @Enumerated(STRING)
    private RefundUnit unit;

    @Column(updatable = false, nullable = false)
    private BigDecimal value;

    private enum RefundUnit {
        PERCENTAGE,
        AMOUNT
    }
}