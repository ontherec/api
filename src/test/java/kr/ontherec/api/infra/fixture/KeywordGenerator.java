package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.domain.keyword.domain.Keyword;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class KeywordGenerator {
    public static Set<Keyword> generate(String... keywords) {

        return Arrays.stream(keywords)
                .map(i -> Keyword.builder().title(i).build())
                .collect(Collectors.toSet());
    }
}
