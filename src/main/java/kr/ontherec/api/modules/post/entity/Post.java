package kr.ontherec.api.modules.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import kr.ontherec.api.infra.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private int likeCount;
}
