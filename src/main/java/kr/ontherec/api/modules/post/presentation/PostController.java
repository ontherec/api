package kr.ontherec.api.modules.post.presentation;


import jakarta.validation.Valid;
import kr.ontherec.api.modules.post.application.PostCommandService;
import kr.ontherec.api.modules.post.application.PostMapper;
import kr.ontherec.api.modules.post.application.PostQueryService;
import kr.ontherec.api.modules.post.dto.PostCreateRequestDto;
import kr.ontherec.api.modules.post.dto.PostResponseDto;
import kr.ontherec.api.modules.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.modules.post.entity.Post;
import kr.ontherec.api.modules.post.exception.PostException;
import kr.ontherec.api.modules.post.exception.PostExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {
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
    ResponseEntity<Long> create(Authentication authentication, @Valid @RequestBody PostCreateRequestDto dto) {
        Post newPost = postMapper.registerRequestDtoToEntity(dto);

        Post post = postCommandService.create(authentication.getName(), newPost);
        return ResponseEntity.created(URI.create("/v1/posts/" + post.getId())).body(post.getId());
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> update(Authentication authentication,
                                           @PathVariable Long id,
                                           @Valid @RequestBody PostUpdateRequestDto dto) {

        if (!postQueryService.isAuthor(id, authentication.getName()))
            throw new PostException(PostExceptionCode.FORBIDDEN);

        postCommandService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(Authentication authentication,
                                @PathVariable Long id) {
        if (!postQueryService.isAuthor(id, authentication.getName()))
            throw new PostException(PostExceptionCode.FORBIDDEN);

        postCommandService.delete(id);
        return ResponseEntity.ok().build();
    }
}
