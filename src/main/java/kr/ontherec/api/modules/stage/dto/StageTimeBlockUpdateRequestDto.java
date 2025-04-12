package kr.ontherec.api.modules.stage.dto;

import jakarta.validation.constraints.NotNull;
import kr.ontherec.api.modules.item.entity.DOW;

import java.time.Duration;
import java.time.LocalTime;

public record StageTimeBlockUpdateRequestDto(
        Long id,
        @NotNull(message = "요일을 입력해주세요")
        DOW dow,
        @NotNull(message = "시작 시간을 입력해주세요")
        LocalTime startTime,
        @NotNull(message = "종료 시간을 입력해주세요")
        LocalTime endTime,
        @NotNull(message = "기본 이용 시간을 입력해주세요")
        Duration standardTime,
        @NotNull(message = "기본 요금을 입력해주세요")
        long standardPrice,
        @NotNull(message = "단위 시간당 추가 비용을 입력해주세요")
        long extraPerUnit
) {}
