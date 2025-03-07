package kr.ontherec.api.domain.post.application;

import kr.ontherec.api.domain.post.domain.Post;
import kr.ontherec.api.domain.post.dto.PostCreateRequestDto;
import kr.ontherec.api.domain.post.dto.PostResponseDto;
import kr.ontherec.api.domain.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.global.config.MapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        config = MapperConfig.class,
        imports = LocalDateTime.class
)
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "likeCount", expression = "java(0)")
    @Mapping(target = "viewCount", expression = "java(0)")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    Post registerRequestDtoToEntity(PostCreateRequestDto dto);

    @Mapping(target = "tags", ignore = true)
    void update(PostUpdateRequestDto dto, @MappingTarget Post post);

    PostResponseDto EntityToResponseDto(Post post);

    default Set<String> serializeTags(Set<Tag> tags) {
        if(tags == null) return null;
        return tags.stream()
                .map(Tag::getTitle)
                .collect(Collectors.toSet());
    }
}
