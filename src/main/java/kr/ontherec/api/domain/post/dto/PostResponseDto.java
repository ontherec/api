package kr.ontherec.api.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDto (
        Long id,
        String username,
        String title,
        List<String> tags,
        String content,
        int viewCount,
        int likeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
