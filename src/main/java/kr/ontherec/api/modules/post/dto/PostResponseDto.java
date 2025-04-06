package kr.ontherec.api.modules.post.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    Long id;
    String author;
    List<String> images;
    String title;
    String content;
    long viewCount;
    long likeCount;
    boolean liked;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
}
