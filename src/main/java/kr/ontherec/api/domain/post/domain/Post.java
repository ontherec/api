package kr.ontherec.api.domain.post.domain;

import jakarta.persistence.*;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.global.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @RequiredArgsConstructor(access = PROTECTED)
@SuperBuilder @AllArgsConstructor(access = PROTECTED)
@Getter @Setter @EqualsAndHashCode(of = "id", callSuper = false)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private String username;

    @Column(nullable = false)
    private String title;

    @ManyToMany(fetch = EAGER)
    private List<Tag> tags;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private int likeCount;
}
