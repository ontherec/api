package kr.ontherec.api.domain.post.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record PostResponseDto (
        Long id,
        String username,
        String title,
        Set<String> tags,
        String content,
        int viewCount,
        int likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
