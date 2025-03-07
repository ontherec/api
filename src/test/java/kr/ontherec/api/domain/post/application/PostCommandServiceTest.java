package kr.ontherec.api.domain.post.application;

import kr.ontherec.api.domain.post.domain.Post;
import kr.ontherec.api.domain.post.dto.PostCreateRequestDto;
import kr.ontherec.api.domain.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.domain.post.exception.PostException;
import kr.ontherec.api.domain.post.exception.PostExceptionCode;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.PostFactory;
import kr.ontherec.api.infra.fixture.TagFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@UnitTest
class PostCommandServiceTest {

    @Autowired private TagFactory tagFactory;
    @Autowired private PostFactory postFactory;

    @Autowired private PostCommandService postCommandService;
    @Autowired private PostQueryService postQueryService;

    private final PostMapper postMapper = PostMapper.INSTANCE;

    @DisplayName("게시글 생성 성공")
    @Test
    void create() {
        // given
        Set<Tag> tags = tagFactory.create("tag");
        PostCreateRequestDto dto = new PostCreateRequestDto(
                "post",
                null,
                "post"
        );
        Post newPost = postMapper.registerRequestDtoToEntity(dto);

        // when
        Post post = postCommandService.create("test", newPost, tags);

        // then
        assertThat(post.getAuthor()).isEqualTo("test");
        assertThat(post.getTitle()).isEqualTo(dto.title());
        assertThat(post.getTags()).isEqualTo(tags);
        assertThat(post.getContent()).isEqualTo(dto.content());
    }

    @DisplayName("게시글 수정 성공")
    @Test
    void update() {
        // given
        Post post = postFactory.create("test", "post", null);
        PostUpdateRequestDto dto = new PostUpdateRequestDto(
                "newPost",
                null,
                "newPost"
        );
        Set<Tag> tags = tagFactory.create("tag");

        // when
        postCommandService.update(post.getId(), dto, tags);

        // then
        assertThat(post.getTitle()).isEqualTo(dto.title());
        assertThat(post.getTags()).isEqualTo(tags);
        assertThat(post.getContent()).isEqualTo(dto.content());
    }

    @DisplayName("게시글 삭제 성공")
    @Test
    void delete() {
        // given
        Set<Tag> tags = tagFactory.create("tag");
        Post post = postFactory.create("test", "post", tags);

        // when
        postCommandService.delete(post.getId());

        // then
        Throwable throwable = catchThrowable(() -> postQueryService.get(post.getId()));

        assertThat(throwable)
                .isInstanceOf(PostException.class)
                .hasMessage(PostExceptionCode.NOT_FOUND.getMessage());
    }
}