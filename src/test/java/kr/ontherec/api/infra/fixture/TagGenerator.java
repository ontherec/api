package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.domain.tag.domain.Tag;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TagGenerator {
    public static Set<Tag> generate(String... tags) {

        return Arrays.stream(tags)
                .map(i -> Tag.builder().title(i).build())
                .collect(Collectors.toSet());
    }
}
