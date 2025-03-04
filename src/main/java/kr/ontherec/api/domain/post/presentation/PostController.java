package kr.ontherec.api.domain.post.presentation;


import jakarta.validation.Valid;
import kr.ontherec.api.domain.post.application.PostCommandService;
import kr.ontherec.api.domain.post.application.PostMapper;
import kr.ontherec.api.domain.post.application.PostQueryService;
import kr.ontherec.api.domain.post.domain.Post;
import kr.ontherec.api.domain.post.dto.PostCreateRequestDto;
import kr.ontherec.api.domain.post.dto.PostResponseDto;
import kr.ontherec.api.domain.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.domain.post.exception.PostException;
import kr.ontherec.api.domain.post.exception.PostExceptionCode;
import kr.ontherec.api.domain.tag.application.TagService;
import kr.ontherec.api.domain.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final TagService tagService;
    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;
    private final PostMapper postMapper = PostMapper.INSTANCE;

    @GetMapping
    ResponseEntity<List<PostResponseDto>> search(@RequestParam(value = "q", required = false) String query) {
        List<Post> posts = postQueryService.search(query);
        List<PostResponseDto> response = posts.stream().map(postMapper::EntityToResponseDto).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<PostResponseDto> get(@PathVariable Long id) {
        Post post = postQueryService.get(id);
        PostResponseDto response = postMapper.EntityToResponseDto(post);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    ResponseEntity<Long> register(@Valid @RequestBody PostCreateRequestDto dto) {

        Post newPost = postMapper.registerRequestDtoToEntity(dto);
        Set<Tag> tags = dto.tags() == null ? null : dto.tags()
                .stream()
                .map(s -> Tag.builder().title(s).build())
                .map(tagService::getOrCreate)
                .collect(Collectors.toSet());

        Post post = postCommandService.create(newPost, tags);
        return ResponseEntity.created(URI.create("/v1/posts/" + post.getId())).body(post.getId());
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> update(Authentication authentication,
                                           @PathVariable Long id,
                                           @Valid @RequestBody PostUpdateRequestDto dto) {

        if (!postQueryService.isAuthor(id, authentication.getName()))
            throw new PostException(PostExceptionCode.FORBIDDEN);

        Set<Tag> tags = dto.tags() == null ? null : dto.tags()
                .stream()
                .map(s -> Tag.builder().title(s).build())
                .map(tagService::getOrCreate)
                .collect(Collectors.toSet());

        postCommandService.update(id, dto, tags);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> remove(Authentication authentication,
                                @PathVariable Long id) {
        if (!postQueryService.isAuthor(id, authentication.getName()))
            throw new PostException(PostExceptionCode.FORBIDDEN);

        postCommandService.delete(id);
        return ResponseEntity.ok().build();
    }
}
