package kr.ontherec.api.modules.stage.dao;

import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import kr.ontherec.api.modules.stage.entity.QStage;
import kr.ontherec.api.modules.stage.entity.Stage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.Assert;

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
                        .or(stage.tags.any().containsIgnoreCase(query))
                        .or(stage.content.containsIgnoreCase(query));

        Assert.notNull(getQuerydsl(), "QueryDSL must not be null");
        SubQueryExpression<Stage> subQuery = getQuerydsl().applyPagination(pageable, from(stage).where(condition));

        return from(stage)
                .leftJoin(stage.host).fetchJoin()
                .leftJoin(stage.images).fetchJoin()
                .leftJoin(stage.address).fetchJoin()
                .leftJoin(stage.tags).fetchJoin()
                .leftJoin(stage.links).fetchJoin()
                .leftJoin(stage.holidays).fetchJoin()
                .leftJoin(stage.refundPolicies).fetchJoin()
                .where(stage.in(subQuery))
                .fetch();
    }
}
