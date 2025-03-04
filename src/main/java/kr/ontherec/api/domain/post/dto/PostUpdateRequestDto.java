package kr.ontherec.api.domain.post.dto;

import kr.ontherec.api.domain.tag.domain.Tag;

import java.util.List;

public record PostUpdateRequestDto (
        String title,
        List<Tag> tags,
        String content
) {}
