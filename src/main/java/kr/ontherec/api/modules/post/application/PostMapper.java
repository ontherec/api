package kr.ontherec.api.modules.post.application;

import kr.ontherec.api.infra.config.MapperConfig;
import kr.ontherec.api.modules.post.dto.PostCreateRequestDto;
import kr.ontherec.api.modules.post.dto.PostResponseDto;
import kr.ontherec.api.modules.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.modules.post.entity.Post;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(
        config = MapperConfig.class,
        imports = LocalDateTime.class
)
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likeCount", expression = "java(0)")
    @Mapping(target = "viewCount", expression = "java(0)")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    Post registerRequestDtoToEntity(PostCreateRequestDto dto);

    void update(PostUpdateRequestDto dto, @MappingTarget Post post);

    PostResponseDto EntityToResponseDto(Post post, String username);

    @AfterMapping
    default void mapLiked(Post post, String username, @MappingTarget PostResponseDto dto) {
        if(username != null) {
            dto.setLiked(post.getLikedUsernames().contains(username));
        }
    }
}
