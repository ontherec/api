package kr.ontherec.api.modules.post.application;

import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.PostFactory;
import kr.ontherec.api.modules.post.dto.PostCreateRequestDto;
import kr.ontherec.api.modules.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.modules.post.entity.Post;
import kr.ontherec.api.modules.post.exception.PostException;
import kr.ontherec.api.modules.post.exception.PostExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@UnitTest
class PostCommandServiceTest {

    @Autowired private PostFactory postFactory;

    @Autowired private PostCommandService postCommandService;
    @Autowired private PostQueryService postQueryService;

    private final PostMapper postMapper = PostMapper.INSTANCE;

    @DisplayName("게시글 생성 성공")
    @Test
    void create() {
        // given
        PostCreateRequestDto dto = new PostCreateRequestDto(
                List.of("https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg"),
                "post",
                "post");
        Post newPost = postMapper.registerRequestDtoToEntity(dto);

        // when
        Post post = postCommandService.create("test", newPost);

        // then
        assertThat(post.getAuthor()).isEqualTo("test");
        assertThat(post.getTitle()).isEqualTo(dto.title());
        assertThat(post.getContent()).isEqualTo(dto.content());
    }

    @DisplayName("게시글 수정 성공")
    @Test
    void update() {
        // given
        Post post = postFactory.create("test", "post");
        PostUpdateRequestDto dto = new PostUpdateRequestDto(
                List.of("https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg"),
                "newPost",
                "newPost");

        // when
        postCommandService.update(post.getId(), dto);

        // then
        assertThat(post.getTitle()).isEqualTo(dto.title());
        assertThat(post.getContent()).isEqualTo(dto.content());
    }

    @DisplayName("게시글 삭제 성공")
    @Test
    void delete() {
        // given
        Post post = postFactory.create("test", "post");

        // when
        postCommandService.delete(post.getId());

        // then
        Throwable throwable = catchThrowable(() -> postQueryService.get(post.getId()));

        assertThat(throwable)
                .isInstanceOf(PostException.class)
                .hasMessage(PostExceptionCode.NOT_FOUND.getMessage());
    }
}