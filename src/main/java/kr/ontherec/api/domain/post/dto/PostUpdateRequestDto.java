package kr.ontherec.api.domain.post.dto;

import java.util.Set;

public record PostUpdateRequestDto (
        String title,
        Set<String> tags,
        String content
) {}
