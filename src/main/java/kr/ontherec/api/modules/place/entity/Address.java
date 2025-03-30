package kr.ontherec.api.modules.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@Builder @AllArgsConstructor(access = PRIVATE)
@Getter @EqualsAndHashCode(of = "id")
public class Address{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private String zipcode;

    @Column(updatable = false, nullable = false)
    private String state;

    @Column(updatable = false, nullable = false)
    private String city;

    @Column(updatable = false, nullable = false)
    private String streetAddress;

    @Column(updatable = false)
    private String detail;

    @Column(updatable = false, nullable = false)
    private BigDecimal latitude;

    @Column(updatable = false, nullable = false)
    private BigDecimal longitude;
}
