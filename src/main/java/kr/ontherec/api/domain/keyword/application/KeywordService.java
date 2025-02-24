package kr.ontherec.api.domain.keyword.application;

import kr.ontherec.api.domain.keyword.domain.Keyword;

public interface KeywordService {
    Keyword getOrCreate(Keyword keyword);
}
