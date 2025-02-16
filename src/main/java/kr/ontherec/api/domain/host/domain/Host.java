package kr.ontherec.api.domain.host.domain;

import jakarta.persistence.*;
import kr.ontherec.api.domain.host.exception.HostException;
import kr.ontherec.api.domain.host.exception.HostExceptionCode;
import kr.ontherec.api.global.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalTime;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity @NoArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PROTECTED)
@Getter
public class Host extends BaseEntity {
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

    public static abstract class HostBuilder<C extends Host, B extends HostBuilder<C, B>> extends BaseEntityBuilder<C, B> {

        private LocalTime contactFrom;

        private LocalTime contactUntil;

        public HostBuilder<C, B> contactFrom(LocalTime contactFrom) {
            this.contactFrom = contactFrom;
            verifyContactTime();
            return this;
        }

        public HostBuilder<C, B> contactUntil(LocalTime contactUntil) {
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
