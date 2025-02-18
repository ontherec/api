package kr.ontherec.api.domain.place.domain;

import jakarta.persistence.*;
import kr.ontherec.api.domain.place.exception.PlaceException;
import kr.ontherec.api.domain.place.exception.PlaceExceptionCode;
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

    void validateValue() {
        if(unit == RefundUnit.PERCENTAGE && value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(new BigDecimal(100)) > 0) {
            throw new PlaceException(PlaceExceptionCode.NOT_VALID_REFUND_POLICY);
        }
    }

    private enum RefundUnit {
        PERCENTAGE,
        AMOUNT
    }
}