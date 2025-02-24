package kr.ontherec.api.domain.host.domain;

import jakarta.persistence.*;
import kr.ontherec.api.domain.host.exception.HostException;
import kr.ontherec.api.domain.host.exception.HostExceptionCode;
import kr.ontherec.api.global.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PRIVATE)
@Getter @Setter @EqualsAndHashCode(of = "id", callSuper = false)
public class Host extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false, nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(STRING)
    private Bank bank;

    @Column(nullable = false)
    private String account;

    @Column
    private LocalTime contactFrom;

    @Column
    private LocalTime contactUntil;

    @Column
    private Duration averageResponseTime;

    public void validateContactTime() {
        if (contactFrom == null || contactUntil == null) {
            return;
        }
        if (Duration.between(contactFrom, contactUntil).minusMinutes(30).isNegative()) {
            throw new HostException(HostExceptionCode.NOT_VALID_CONTACT_TIME);
        }
    }
}
