package kr.ontherec.api.domain.host.domain;

import jakarta.persistence.*;
import kr.ontherec.api.domain.host.exception.HostException;
import kr.ontherec.api.domain.host.exception.HostExceptionCode;
import lombok.*;

import java.time.Duration;
import java.time.LocalTime;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class Host {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false, nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private Bank bank;

    @Column(nullable = false)
    private String account;

    @Column
    private LocalTime contactFrom;

    @Column
    private LocalTime contactUntil;

    @Column
    private Duration averageResponseTime;

    public void setContactFrom(LocalTime contactFrom) {
        this.contactFrom = contactFrom;
        verifyContactTime();
    }

    public void setContactUntil(LocalTime contactUntil) {
        this.contactUntil = contactUntil;
        verifyContactTime();
    }

    private void verifyContactTime() {
        if (contactFrom == null || contactUntil == null) {
            return;
        }
        if (Duration.between(contactFrom, contactUntil).minusMinutes(30).isNegative()) {
            throw new HostException(HostExceptionCode.NOT_VALID_CONTACT_TIME);
        }
    }

    public static class HostBuilder {

        private LocalTime contactFrom;

        private LocalTime contactUntil;

        public HostBuilder contactFrom(LocalTime contactFrom) {
            this.contactFrom = contactFrom;
            verifyContactTime();
            return this;
        }

        public HostBuilder contactUntil(LocalTime contactUntil) {
            this.contactUntil = contactUntil;
            verifyContactTime();
            return this;
        }

        private void verifyContactTime() {
            if (contactFrom == null || contactUntil == null) {
                return;
            }
            if (Duration.between(contactFrom, contactUntil).minusMinutes(30).isNegative()) {
                throw new HostException(HostExceptionCode.NOT_VALID_CONTACT_TIME);
            }
        }
    }
}
