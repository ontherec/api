package kr.ontherec.api.domain.stage.domain;

import jakarta.persistence.*;
import kr.ontherec.api.domain.item.domain.RefundPolicy;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.global.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
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

    @Column(nullable = false)
    Long price;

    @Column(nullable = false)
    int minCapacity;

    @Column(nullable = false)
    int maxCapacity;

    @Column(nullable = false)
    int stage_width;

    @Column(nullable = false)
    int stage_height;

    // TODO: timeblocks

    @OneToMany
    Set<RefundPolicy> refundPolicies;

    // engineering
    @Column(nullable = false)
    boolean stageManagingAvailable;

    @Column(nullable = false)
    boolean stageManagingFee;

    @Column(nullable = false)
    boolean soundEngineeringAvailable;

    @Column(nullable = false)
    boolean soundEngineeringFee;

    @Column(nullable = false)
    boolean lightEngineeringAvailable;

    @Column(nullable = false)
    boolean lightEngineeringFee;

    @Column(nullable = false)
    boolean photographingAvailable;

    @Column(nullable = false)
    boolean photographingFee;


    // documents
    @Column
    String applicationForm;

    @Column(nullable = false)
    String cueSheetTemplate;

    @Column(nullable = false)
    Duration cueSheetDue;

    // equipments


    // facilities
    @Column(updatable = false, nullable = false)
    int floor;

    @Column(nullable = false)
    boolean havElevator;

    @Column(nullable = false)
    int parkingCapacity;

    @Column
    String parkingLocation;

    @Column(nullable = false)
    boolean freeParking;

    @Column(nullable = false)
    boolean hasRestroom;

    @Column(nullable = false)
    boolean hasWifi;

    @Column(nullable = false)
    boolean hasCameraStanding;

    @Column(nullable = false)
    boolean hasWaitingRoom;

    @Column(nullable = false)
    boolean hasProjector;

    @Column(nullable = false)
    boolean hasLocker;


    // fnb policies
    @Column(nullable = false)
    boolean allowsWater;

    @Column(nullable = false)
    boolean allowsDrink;

    @Column(nullable = false)
    boolean allowsFood;

    @Column(nullable = false)
    boolean allowsFoodDelivery;

    @Column(nullable = false)
    boolean allowsAlcohol;

    @Column(nullable = false)
    boolean sellDrink;

    @Column(nullable = false)
    boolean sellAlcohol;

    @Column(columnDefinition = "TEXT")
    String introduction;

    @Column(columnDefinition = "TEXT")
    String caution;

    @ManyToMany(fetch = EAGER)
    private List<Tag> tags;

    // TODO: booking slots

    public void setTags(Set<Tag> tags) {
        this.tags = tags == null ? null : new ArrayList<>(tags);
    }
}
