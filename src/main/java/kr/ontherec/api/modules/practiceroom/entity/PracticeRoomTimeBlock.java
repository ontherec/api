package kr.ontherec.api.modules.practiceroom.entity;

import jakarta.persistence.*;
import kr.ontherec.api.modules.item.entity.DOW;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@Builder @AllArgsConstructor(access = PRIVATE)
@Getter @Setter @EqualsAndHashCode(of = { "dow", "startTime", "endTime" })
public class PracticeRoomTimeBlock {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(STRING)
    private DOW dow;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private BigDecimal pricePerUnit;
}
