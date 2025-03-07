package kr.ontherec.api.domain.post.application;

import kr.ontherec.api.domain.post.domain.Post;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.PostFactory;
import kr.ontherec.api.infra.fixture.TagFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UnitTest
class PostQueryServiceTest {

    @Autowired private TagFactory tagFactory;
    @Autowired private PostFactory postFactory;

    @Autowired private PostQueryService postQueryService;

    @DisplayName("게시글 검색 성공")
    @Test
    void search() {
        // given
        Set<Tag> tags = tagFactory.create("tag");
        Post post = postFactory.create("test", "post", tags);

        // when
        List<Post> posts = postQueryService.search("post");

        // then
        assertThat(posts.contains(post)).isTrue();
    }

    @DisplayName("게시글 조회 성공")
    @Test
    void get() {
        // given
        Set<Tag> tags = tagFactory.create("tag");
        Post post = postFactory.create("test", "post", tags);

        // when
        Post foundPost = postQueryService.get(post.getId());

        // then
        assertThat(foundPost.getId()).isEqualTo(post.getId());
    }

    @DisplayName("게시글 작성자 확인 성공")
    @Test
    void isAuthor() {
        // given
        Set<Tag> tags = tagFactory.create("tag");
        Post post = postFactory.create("test", "post", tags);

        // when
        boolean isAuthor = postQueryService.isAuthor(post.getId(), "test");

        // then
        assertThat(isAuthor).isTrue();
    }

    @DisplayName("게시글 작성자 확인 성공 - 다른 작성자")
    @Test
    void isAuthorWithOtherAuthor() {
        // given
        Set<Tag> tags = tagFactory.create("tag");
        Post post = postFactory.create("host", "post", tags);

        // when
        boolean isAuthor = postQueryService.isAuthor(post.getId(), "test");

        // then
        assertThat(isAuthor).isFalse();
    }
}