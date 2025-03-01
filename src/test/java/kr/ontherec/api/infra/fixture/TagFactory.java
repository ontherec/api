package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.domain.tag.application.TagService;
import kr.ontherec.api.domain.tag.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TagFactory {
    @Autowired private TagService tagService;

    public Set<Tag> create(String... tags) {
        return Arrays.stream(tags)
                .map(tag -> Tag.builder().title(tag).build())
                .map(tagService::getOrCreate)
                .collect(Collectors.toSet());
    }
}
