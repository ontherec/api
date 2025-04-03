package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.modules.post.application.PostCommandService;
import kr.ontherec.api.modules.post.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostFactory {
    @Autowired private PostCommandService postCommandService;

    public Post create(String author, String title) {
        Post newPost = Post.builder()
                .title(title)
                .content(title)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        return postCommandService.create(author, newPost);
    }
}
