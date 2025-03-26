package kr.ontherec.api.domain.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Duration;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PRIVATE)
@Getter @Setter @EqualsAndHashCode(of = { "dayBefore", "percent" }, callSuper = false)
public class RefundPolicy {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private Duration dayBefore;

    @Column(updatable = false, nullable = false)
    private BigDecimal percent;
}
