package kr.ontherec.api.modules.post.application;

import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.PostFactory;
import kr.ontherec.api.infra.model.BaseEntity;
import kr.ontherec.api.modules.post.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UnitTest
class PostQueryServiceTest {

    @Autowired private PostFactory postFactory;

    @Autowired private PostQueryService postQueryService;

    @DisplayName("게시글 검색 성공")
    @Test
    void search() {
        // given
        Post post = postFactory.create("test", "post");

        // when
        List<Post> posts = postQueryService.search(
                "post",
                PageRequest.of(0, 12, Sort.sort(Post.class).by(BaseEntity::getCreatedAt).descending()),
                "test"
        );

        // then
        assertThat(posts.contains(post)).isTrue();
    }

    @DisplayName("게시글 조회 성공")
    @Test
    void get() {
        // given
        Post post = postFactory.create("test", "post");

        // when
        Post foundPost = postQueryService.get(post.getId());

        // then
        assertThat(foundPost.getId()).isEqualTo(post.getId());
    }

    @DisplayName("게시글 작성자 확인 성공")
    @Test
    void isAuthor() {
        // given
        Post post = postFactory.create("test", "post");

        // when
        boolean isAuthor = postQueryService.isAuthor(post.getId(), "test");

        // then
        assertThat(isAuthor).isTrue();
    }

    @DisplayName("게시글 작성자 확인 성공 - 다른 작성자")
    @Test
    void isAuthorWithOtherAuthor() {
        // given
        Post post = postFactory.create("host", "post");

        // when
        boolean isAuthor = postQueryService.isAuthor(post.getId(), "test");

        // then
        assertThat(isAuthor).isFalse();
    }
}