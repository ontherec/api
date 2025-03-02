package kr.ontherec.api.domain.tag.application;

import kr.ontherec.api.domain.tag.domain.Tag;

public interface TagService {
    Tag getOrCreate(Tag tag);
}
