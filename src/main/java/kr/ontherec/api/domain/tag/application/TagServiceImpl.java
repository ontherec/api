package kr.ontherec.api.domain.tag.application;

import kr.ontherec.api.domain.tag.dao.TagRepository;
import kr.ontherec.api.domain.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public Tag getOrCreate(Tag tag) {
        if(tagRepository.existsByTitle(tag.getTitle()))
            return tagRepository.findByTitleOrThrow(tag.getTitle());

        return tagRepository.save(tag);
    }
}
