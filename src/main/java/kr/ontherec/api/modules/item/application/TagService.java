package kr.ontherec.api.modules.item.application;

import kr.ontherec.api.modules.item.entity.Tag;

public interface TagService {
    Tag getOrCreate(Tag tag);
}
