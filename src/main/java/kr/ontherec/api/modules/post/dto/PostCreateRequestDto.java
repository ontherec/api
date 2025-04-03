package kr.ontherec.api.modules.post.dto;

public record PostCreateRequestDto (
        String title,
        String content
) {}
