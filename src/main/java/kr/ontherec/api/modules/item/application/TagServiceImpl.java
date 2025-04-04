package kr.ontherec.api.modules.item.application;

import kr.ontherec.api.modules.item.dao.TagRepository;
import kr.ontherec.api.modules.item.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public Tag getOrCreate(Tag tag) {
        if(tagRepository.existsByTitle(tag.getTitle()))
            return tagRepository.findByTitleOrThrow(tag.getTitle());

        return tagRepository.save(tag);
    }
}
