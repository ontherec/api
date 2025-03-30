package kr.ontherec.api.modules.post.entity;

import jakarta.persistence.*;
import kr.ontherec.api.infra.model.BaseEntity;
import kr.ontherec.api.modules.tag.entity.Tag;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Set<Tag> getTags() {
        return tags == null ? new HashSet<>() : new HashSet<>(tags);
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
    }

    public static abstract class PostBuilder<C extends Post, B extends Post.PostBuilder<C, B>> extends BaseEntityBuilder<C, B> {

        public B tags(List<Tag> tags) {
            this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
            return self();
        }
    }
}
