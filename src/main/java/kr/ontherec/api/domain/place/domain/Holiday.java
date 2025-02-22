package kr.ontherec.api.domain.place.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@Builder @AllArgsConstructor(access = PRIVATE)
@Getter
public class Holiday {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    @Enumerated(STRING)
    private HolidayType type;
}
