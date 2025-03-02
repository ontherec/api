package kr.ontherec.api.domain.stage.domain;

import jakarta.persistence.*;
import kr.ontherec.api.domain.item.domain.RefundPolicy;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.stage.exception.StageException;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.global.model.BaseEntity;
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
import static kr.ontherec.api.domain.stage.exception.StageExceptionCode.NOT_VALID_ENGINEERING_FEE;
import static kr.ontherec.api.domain.stage.exception.StageExceptionCode.NOT_VALID_PARKING;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PROTECTED)
@Getter @Setter @EqualsAndHashCode(of = "id", callSuper = false)
public class Stage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(updatable = false, nullable = false)
    private Place place;

    // images

    // location
    @Column(nullable = false)
    private String title;

    @Column(updatable = false, nullable = false)
    private int floor;

    @Column(nullable = false)
    private boolean hasElevator;

    // introduction
    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(columnDefinition = "TEXT")
    private String guide;

    @ManyToMany(fetch = EAGER)
    private List<Tag> tags;

    // count
    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private int likeCount;

    // Area
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
    // TODO: booking slots

    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    private Set<RefundPolicy> refundPolicies;

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

    // facilities
    @Column(nullable = false)
    private int parkingCapacity;

    @Column
    private String parkingLocation;

    @Column
    private Boolean freeParking;

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

    public void validateParking() {
        if (parkingCapacity > 0 && freeParking == null)
            throw new StageException(NOT_VALID_PARKING);
        if (parkingCapacity == 0 && freeParking != null)
            throw new StageException(NOT_VALID_PARKING);
    }

    public Set<Tag> getTags() {
        return tags == null ? null : new HashSet<>(tags);
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
    }

    public void setRefundPolicies(Set<RefundPolicy> refundPolicies) {
        this.refundPolicies = refundPolicies == null ? new HashSet<>() : new HashSet<>(refundPolicies);
    }

    public static abstract class StageBuilder<C extends Stage, B extends StageBuilder<C, B>> extends BaseEntityBuilder<C, B> {

        public B tags(List<Tag> tags) {
            this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
            return self();
        }

        public B refundPolicies(Set<RefundPolicy> refundPolicies) {
            this.refundPolicies = refundPolicies == null ? new HashSet<>() : new HashSet<>(refundPolicies);
            return self();
        }
    }
}
