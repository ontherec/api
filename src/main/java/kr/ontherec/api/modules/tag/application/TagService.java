package kr.ontherec.api.modules.tag.application;

import kr.ontherec.api.modules.tag.entity.Tag;

public interface TagService {
    Tag getOrCreate(Tag tag);
}
