package kr.ontherec.api.modules.practiceroom.entity;

import jakarta.persistence.*;
import kr.ontherec.api.infra.entity.BaseEntity;
import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.item.entity.Address;
import kr.ontherec.api.modules.item.entity.HolidayType;
import kr.ontherec.api.modules.item.entity.RefundPolicy;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PRIVATE)
@Getter @Setter @EqualsAndHashCode(of = "id", callSuper = false)
public class PracticeRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(updatable = false, nullable = false)
    private Host host;

    @ElementCollection(fetch = EAGER)
    @Column(nullable = false)
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(unique = true, updatable = false, nullable = false)
    private String brn;

    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(updatable = false, nullable = false)
    private Address address;

    // counts
    @Column(nullable = false)
    private long viewCount;

    @Column(nullable = false)
    private long likeCount;

    @ElementCollection(fetch = EAGER)
    @Column
    @Builder.Default
    private Set<String> likedUsernames = new HashSet<>();

    // introduction
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ElementCollection(fetch = EAGER)
    @Column
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @ElementCollection(fetch = EAGER)
    @Column
    @Builder.Default
    private Set<String> links = new HashSet<>();

    // capacity
    @Column(nullable = false)
    private int standardCapacity;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private BigDecimal extraPerPerson;

    // business
    @ElementCollection(fetch = EAGER)
    @Column
    @Enumerated(STRING)
    @Builder.Default
    private Set<HolidayType> holidays = new HashSet<>();

    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @JoinColumn
    @Builder.Default
    private Set<PracticeRoomTimeBlock> timeBlocks = new HashSet<>();

    @Column(nullable = false)
    private Duration bookingFrom;

    @Column(nullable = false)
    private Duration bookingUntil;

    // TODO: booking slots

    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @JoinColumn
    @Builder.Default
    private Set<RefundPolicy> refundPolicies = new HashSet<>();

    // TODO: equipments

    // parking
    @Column(nullable = false)
    private int parkingCapacity;

    @Column
    private String parkingLocation;

    @Column
    private Boolean freeParking;

    // facilities
    @Column(nullable = false)
    private boolean hasElevator;

    @Column(nullable = false)
    private boolean hasRestroom;

    @Column(nullable = false)
    private boolean hasWifi;

    @Column(nullable = false)
    private boolean hasCameraStanding;

    // fnb policies
    @Column(nullable = false)
    private boolean allowsWater;

    @Column(nullable = false)
    private boolean allowsDrink;

    @Column(nullable = false)
    private boolean allowsFood;

    @Column(nullable = false)
    private boolean sellDrink;
}