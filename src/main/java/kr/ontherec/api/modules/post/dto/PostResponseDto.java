package kr.ontherec.api.modules.post.dto;

import java.time.LocalDateTime;

public record PostResponseDto (
        Long id,
        String author,
        String title,
        String content,
        int viewCount,
        int likeCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {}
