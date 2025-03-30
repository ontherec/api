package kr.ontherec.api.modules.host.dto;

import java.time.Duration;
import java.time.LocalTime;

public record HostResponseDto (
    Long id,
    String username,
    LocalTime contactFrom,
    LocalTime contactUntil,
    Duration averageResponseTime
) { }
