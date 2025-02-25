package kr.ontherec.api.domain.item.domain;

import jakarta.persistence.*;
import kr.ontherec.api.domain.place.domain.Place;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.File;
import java.time.Duration;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PROTECTED)
@Getter @Setter @EqualsAndHashCode(of = "id", callSuper = false)
public class Stage extends Item {
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
    File application;

    @Column(nullable = false)
    File cueSheet;

    @Column(nullable = false)
    Duration cueSheetDue;

    // equipments


    // info
    @Column(nullable = false)
    int area;

    @Column(nullable = false)
    int stage_width;

    @Column(nullable = false)
    int stage_height;

    @Column(updatable = false, nullable = false)
    int floor;

    @Column(nullable = false)
    boolean havElevator;


    // facilities
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
}
