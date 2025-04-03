package kr.ontherec.api.modules.stage.entity;

import jakarta.persistence.*;
import kr.ontherec.api.infra.model.BaseEntity;
import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.item.entity.Address;
import kr.ontherec.api.modules.item.entity.Holiday;
import kr.ontherec.api.modules.item.entity.Link;
import kr.ontherec.api.modules.item.entity.RefundPolicy;
import kr.ontherec.api.modules.stage.exception.StageException;
import kr.ontherec.api.modules.tag.entity.Tag;
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
import static kr.ontherec.api.modules.stage.exception.StageExceptionCode.*;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PRIVATE)
@Getter @Setter @EqualsAndHashCode(of = "id", callSuper = false)
public class Stage extends BaseEntity {
    private static final int BOOKING_PERIOD_MIN = 7;

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

    // introduction
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToMany(fetch = EAGER)
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @JoinColumn
    @Builder.Default
    private Set<Link> links = new HashSet<>();

    // area
    @Column(nullable = false)
    private int minCapacity;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    @Enumerated(STRING)
    private StageType stageType;

    @Column(nullable = false)
    private BigDecimal stageWidth;

    @Column(nullable = false)
    private BigDecimal stageHeight;

    // business
    // TODO: timeblocks

    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @JoinColumn
    @Builder.Default
    private Set<Holiday> holidays = new HashSet<>();

    // TODO: booking slots

    @Column(nullable = false)
    private Duration bookingFrom;

    @Column(nullable = false)
    private Duration bookingUntil;

    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    @JoinColumn
    @Builder.Default
    private Set<RefundPolicy> refundPolicies = new HashSet<>();

    // engineering
    @Column(nullable = false)
    private boolean stageManagingAvailable;

    @Column
    private Long stageManagingFee;

    @Column(nullable = false)
    private boolean soundEngineeringAvailable;

    @Column
    private Long soundEngineeringFee;

    @Column(nullable = false)
    private boolean lightEngineeringAvailable;

    @Column
    private Long lightEngineeringFee;

    @Column(nullable = false)
    private boolean photographingAvailable;

    @Column
    private Long photographingFee;

    // documents
    @Column
    private String applicationForm;

    @Column(nullable = false)
    private String cueSheetTemplate;

    @Column(nullable = false)
    private Duration cueSheetDue;

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

    @Column(nullable = false)
    private boolean hasWaitingRoom;

    @Column(nullable = false)
    private boolean hasProjector;

    @Column(nullable = false)
    private boolean hasLocker;

    // fnb policies
    @Column(nullable = false)
    private boolean allowsWater;

    @Column(nullable = false)
    private boolean allowsDrink;

    @Column(nullable = false)
    private boolean allowsFood;

    @Column(nullable = false)
    private boolean allowsFoodDelivery;

    @Column(nullable = false)
    private boolean allowsAlcohol;

    @Column(nullable = false)
    private boolean sellDrink;

    @Column(nullable = false)
    private boolean sellAlcohol;

    public void validateBookingPeriod() {
        if(bookingFrom == null || bookingUntil == null)
            return;

        if(bookingFrom.minus(bookingUntil).minusDays(BOOKING_PERIOD_MIN).isNegative()) {
            throw new StageException(NOT_VALID_BOOKING_PERIOD);
        }
    }

    public void validateParking() {
        if (parkingCapacity > 0 && (parkingLocation == null || freeParking == null))
            throw new StageException(NOT_VALID_PARKING);
        if (parkingCapacity == 0 && (parkingLocation != null || freeParking != null))
            throw new StageException(NOT_VALID_PARKING);
    }

    public void validateEngineering() {
        validateEngineeringFee(stageManagingAvailable, stageManagingFee);
        validateEngineeringFee(soundEngineeringAvailable, soundEngineeringFee);
        validateEngineeringFee(lightEngineeringAvailable, lightEngineeringFee);
        validateEngineeringFee(photographingAvailable, photographingFee);
    }

    private void validateEngineeringFee(boolean available, Long fee) {
        if (available && fee == null)
            throw new StageException(NOT_VALID_ENGINEERING_FEE);
        if (!available && fee != null)
            throw new StageException(NOT_VALID_ENGINEERING_FEE);
    }
}