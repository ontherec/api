package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.domain.post.application.PostCommandService;
import kr.ontherec.api.domain.post.domain.Post;
import kr.ontherec.api.domain.tag.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class PostFactory {
    @Autowired private PostCommandService postCommandService;

    public Post create(String author, String title, Set<Tag> tags) {
        Post newPost = Post.builder()
                .title(title)
                .content(title)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        return postCommandService.create(author, newPost, tags);
    }
}
