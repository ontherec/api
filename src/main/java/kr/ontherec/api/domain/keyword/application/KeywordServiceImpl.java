package kr.ontherec.api.domain.keyword.application;

import kr.ontherec.api.domain.keyword.dao.KeywordRepository;
import kr.ontherec.api.domain.keyword.domain.Keyword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {
    private final KeywordRepository keywordRepository;

    @Override
    public Keyword getOrCreate(Keyword keyword) {
        if(keywordRepository.existsByTitle(keyword.getTitle()))
            return keywordRepository.findByTitleOrThrow(keyword.getTitle());

        return keywordRepository.save(keyword);
    }
}
