package kr.ontherec.api.domain.place.domain;

import jakarta.persistence.*;
import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.exception.PlaceException;
import kr.ontherec.api.domain.place.exception.PlaceExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PROTECTED)
@Getter @Setter
public class Place {
    private static final int BOOKING_PERIOD_MIN = 7;
    private static final int BOOKING_UNTIL_MAX = 90;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(updatable = false, nullable = false)
    private Host host;

    @Column(unique = true, updatable = false, nullable = false)
    private String brn;

    @Column(updatable = false, nullable = false)
    private String name;

    @OneToOne(mappedBy = "space", cascade = ALL, orphanRemoval = true)
    @JoinColumn(updatable = false, nullable = false)
    private Address address;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @ManyToMany(mappedBy = "spaces", cascade = ALL)
    private Set<Keyword> keywords;

    @OneToMany(mappedBy = "space", cascade = ALL, orphanRemoval = true)
    private Set<Link> links;

    @OneToMany(mappedBy = "space", cascade = ALL, orphanRemoval = true)
    private Set<RefundPolicy> refundPolicies;

    @Column
    private Duration bookingFrom;

    @Column
    private Duration bookingUntil;

    void validateBookingPeriod() {
        if (bookingFrom == null || bookingUntil == null) {
            return;
        }
        if (bookingUntil.minus(bookingUntil).minusDays(BOOKING_PERIOD_MIN).isNegative()) {
            throw new PlaceException(PlaceExceptionCode.NOT_VALID_BOOKING_PERIOD);
        }
    }
}
