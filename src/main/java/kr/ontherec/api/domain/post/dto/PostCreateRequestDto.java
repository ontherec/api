package kr.ontherec.api.domain.post.dto;

import java.util.List;

public record PostCreateRequestDto (
        String title,
        List<String> tags,
        String content
) {}
