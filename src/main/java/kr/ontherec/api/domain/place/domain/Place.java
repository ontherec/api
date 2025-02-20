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

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(updatable = false, nullable = false)
    private Host host;

    @Column(unique = true, updatable = false, nullable = false)
    private String brn;

    @Column(nullable = false)
    private String name;

    @OneToOne(mappedBy = "place", cascade = ALL, orphanRemoval = true)
    @JoinColumn(updatable = false, nullable = false)
    private Address address;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @ManyToMany(mappedBy = "places", cascade = ALL)
    @JoinColumn
    private Set<Keyword> keywords;

    @OneToMany(mappedBy = "place", cascade = ALL, orphanRemoval = true)
    @JoinColumn
    private Set<Link> links;

    @OneToMany(mappedBy = "place", cascade = ALL, orphanRemoval = true)
    @JoinColumn(nullable = false)
    private Set<RefundPolicy> refundPolicies;

    @Column(nullable = false)
    private Duration bookingFrom;

    @Column(nullable = false)
    private Duration bookingUntil;

    @OneToMany(mappedBy = "place", cascade = ALL, orphanRemoval = true)
    @JoinColumn
    private Set<Holiday> holidays;

    void validateBookingPeriod() {
        if (bookingFrom.minus(bookingUntil).minusDays(BOOKING_PERIOD_MIN).isNegative()) {
            throw new PlaceException(PlaceExceptionCode.NOT_VALID_BOOKING_PERIOD);
        }
    }
}
