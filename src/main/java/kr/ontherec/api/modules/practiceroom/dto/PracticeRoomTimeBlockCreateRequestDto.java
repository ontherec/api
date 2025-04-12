package kr.ontherec.api.modules.practiceroom.dto;

import jakarta.validation.constraints.NotNull;
import kr.ontherec.api.modules.item.entity.DOW;

import java.math.BigDecimal;
import java.time.LocalTime;

public record PracticeRoomTimeBlockCreateRequestDto(
        @NotNull(message = "요일을 입력해주세요")
        DOW dow,
        @NotNull(message = "시작 시간을 입력해주세요")
        LocalTime startTime,
        @NotNull(message = "종료 시간을 입력해주세요")
        LocalTime endTime,
        @NotNull(message = "단위 시간당 이용 요금을 입력해주세요")
        BigDecimal pricePerUnit
) {}
