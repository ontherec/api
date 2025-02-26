package kr.ontherec.api.domain.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import kr.ontherec.api.domain.item.exception.ItemException;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Duration;

import static jakarta.persistence.GenerationType.IDENTITY;
import static kr.ontherec.api.domain.item.exception.ItemExceptionCode.NOT_VALID_REFUND_POLICY;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PROTECTED)
@Getter @Setter @EqualsAndHashCode(of = "id", callSuper = false)
public class RefundPolicy {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private Duration dayBefore;

    @Column(updatable = false, nullable = false)
    private BigDecimal percent;

    void validatePercent() {
        if (percent.compareTo(BigDecimal.ZERO) < 0 || percent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ItemException(NOT_VALID_REFUND_POLICY);
        }
    }
}
