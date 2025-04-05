package kr.ontherec.api.modules.post.entity;

import jakarta.persistence.*;
import kr.ontherec.api.infra.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PRIVATE)
@Getter @Setter @EqualsAndHashCode(of = "id", callSuper = false)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private String author;

    @ElementCollection(fetch = EAGER)
    @Column(nullable = false)
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private long viewCount;

    @Column(nullable = false)
    private long likeCount;
}
