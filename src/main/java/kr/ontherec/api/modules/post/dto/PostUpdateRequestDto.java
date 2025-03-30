package kr.ontherec.api.modules.post.dto;

import java.util.Set;

public record PostUpdateRequestDto (
        String title,
        Set<String> tags,
        String content
) {}
