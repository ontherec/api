package kr.ontherec.api.modules.post.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDto (
        Long id,
        String author,
        List<String> images,
        String title,
        String content,
        long viewCount,
        long likeCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {}
