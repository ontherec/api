package kr.ontherec.api.modules.post.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record PostResponseDto (
        Long id,
        String author,
        String title,
        Set<String> tags,
        String content,
        int viewCount,
        int likeCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {}
