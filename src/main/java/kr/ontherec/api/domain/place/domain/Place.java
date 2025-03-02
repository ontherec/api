package kr.ontherec.api.domain.place.domain;

import jakarta.persistence.*;
import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.exception.PlaceException;
import kr.ontherec.api.domain.place.exception.PlaceExceptionCode;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.global.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PROTECTED)
@Getter @Setter @EqualsAndHashCode(of = "id", callSuper = false)
public class Place extends BaseEntity {
    private static final int BOOKING_PERIOD_MIN = 7;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(updatable = false, nullable = false)
    private Host host;

    // images

    @Column(unique = true, updatable = false, nullable = false)
    private String brn;

    // location
    @Column(nullable = false)
    private String title;

    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(updatable = false, nullable = false)
    private Address address;

    // introduction
    @Column(columnDefinition = "TEXT")
    private String introduction;

    @ManyToMany(fetch = EAGER)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @JoinColumn
    private Set<Link> links = new HashSet<>();

    // business
    @Column(nullable = false)
    private Duration bookingFrom;

    @Column(nullable = false)
    private Duration bookingUntil;

    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @JoinColumn(nullable = false)
    private Set<Holiday> holidays = new HashSet<>();

    public void validateBookingPeriod() {
        if(bookingFrom == null || bookingUntil == null)
            return;

        if(bookingFrom.minus(bookingUntil).minusDays(BOOKING_PERIOD_MIN).isNegative()) {
            throw new PlaceException(PlaceExceptionCode.NOT_VALID_BOOKING_PERIOD);
        }
    }

    public Set<Tag> getTags() {
        return tags == null ? null : new HashSet<>(tags);
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
    }

    public void setLinks(Set<Link> links) {
        this.links = links == null ? new HashSet<>() : new HashSet<>(links);
    }

    public void setHolidays(Set<Holiday> holidays) {
        this.holidays = holidays == null ? new HashSet<>() : new HashSet<>(holidays);
    }

    public static abstract class PlaceBuilder<C extends Place, B extends PlaceBuilder<C, B>> extends BaseEntityBuilder<C, B> {

        public B tags(List<Tag> tags) {
            this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
            return self();
        }

        public B links(Set<Link> links) {
            this.links = links == null ? new HashSet<>() : new HashSet<>(links);
            return self();
        }

        public B holidays(Set<Holiday> holidays) {
            this.holidays = holidays == null ? new HashSet<>() : new HashSet<>(holidays);
            return self();
        }
    }
}
