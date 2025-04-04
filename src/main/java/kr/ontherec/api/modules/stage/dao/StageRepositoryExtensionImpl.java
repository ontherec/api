package kr.ontherec.api.modules.stage.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import kr.ontherec.api.modules.stage.entity.QStage;
import kr.ontherec.api.modules.stage.entity.Stage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class StageRepositoryExtensionImpl extends QuerydslRepositorySupport implements StageRepositoryExtension {
    public StageRepositoryExtensionImpl() {
        super(Stage.class);
    }

    @Override
    public List<Stage> search(String query, Pageable pageable) {
        QStage stage = QStage.stage;

        BooleanExpression condition = query == null ?
                null :
                stage.title.containsIgnoreCase(query)
                        .or(stage.address.state.containsIgnoreCase(query))
                        .or(stage.address.city.containsIgnoreCase(query))
                        .or(stage.address.streetAddress.containsIgnoreCase(query))
                        .or(stage.tags.any().title.containsIgnoreCase(query))
                        .or(stage.content.containsIgnoreCase(query));

        return from(stage)
                .where(condition)
                .join(stage.host).fetchJoin()
                .join(stage.images).fetchJoin()
                .join(stage.address).fetchJoin()
                .join(stage.tags).fetchJoin()
                .join(stage.links).fetchJoin()
                .join(stage.holidays).fetchJoin()
                .join(stage.refundPolicies).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();
    }
}
