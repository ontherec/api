package kr.ontherec.api.modules.stage.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import kr.ontherec.api.infra.util.StopWatch;
import kr.ontherec.api.modules.post.exception.PostException;
import kr.ontherec.api.modules.stage.entity.QStage;
import kr.ontherec.api.modules.stage.entity.Stage;
import kr.ontherec.api.modules.stage.exception.StageException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static com.querydsl.core.types.dsl.PathBuilderValidator.PROPERTIES;
import static kr.ontherec.api.infra.util.ReflectionUtils.getFieldTypeMap;
import static kr.ontherec.api.modules.post.exception.PostExceptionCode.UNAUTHORIZED;
import static kr.ontherec.api.modules.stage.exception.StageExceptionCode.NOT_SUPPORT_FILTER;
import static kr.ontherec.api.modules.stage.exception.StageExceptionCode.NOT_VALID_FILTER;

public class StageRepositoryExtensionImpl extends QuerydslRepositorySupport implements StageRepositoryExtension {

    public StageRepositoryExtensionImpl() {
        super(Stage.class);
    }

    @Override
    @StopWatch
    public List<Stage> search(Map<String, String> params, Pageable pageable, String username) {
        QStage stage = QStage.stage;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        PathBuilder<Stage> pathBuilder = new PathBuilder<>(stage.getType(), stage.getMetadata(), PROPERTIES);
        Map<String, Class<?>> fieldTypes = getFieldTypeMap(Stage.class);

        // query
        if (params.containsKey("q")) {
            String query = params.get("q");
            booleanBuilder.or(stage.title.containsIgnoreCase(query))
                    .or(stage.address.state.containsIgnoreCase(query))
                    .or(stage.address.city.containsIgnoreCase(query))
                    .or(stage.address.streetAddress.containsIgnoreCase(query))
                    .or(stage.tags.any().containsIgnoreCase(query))
                    .or(stage.content.containsIgnoreCase(query));
        }

        // filter
        params.forEach((key, value) -> {
            try {
                switch(key) {
                    case "q":
                        break;
                    case "minCapacity":
                        booleanBuilder.and(pathBuilder.getNumber(key, Integer.class).goe(Integer.valueOf(value)));
                        break;
                    case "maxCapacity":
                        booleanBuilder.and(pathBuilder.getNumber(key, Integer.class).loe(Integer.valueOf(value)));
                        break;
                    case "parkingAvailable":
                        if(!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
                            throw new StageException(NOT_VALID_FILTER);
                        if(value.equalsIgnoreCase("true"))
                            booleanBuilder.and(pathBuilder.getNumber("parkingCapacity", Integer.class).gt(0));
                        if(value.equalsIgnoreCase("false"))
                            booleanBuilder.and(pathBuilder.getNumber("parkingCapacity", Integer.class).loe(0));
                        break;
                    case "liked":
                        if(username == null)
                            throw new PostException(UNAUTHORIZED);
                        if(!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
                            throw new StageException(NOT_VALID_FILTER);
                        if(Boolean.parseBoolean(value))
                            booleanBuilder.and(stage.likedUsernames.contains(username));
                        else
                            booleanBuilder.andNot(stage.likedUsernames.contains(username));
                        break;
                    default:
                        if(!fieldTypes.containsKey(key) || !fieldTypes.get(key).getSimpleName().equalsIgnoreCase("boolean"))
                            throw new StageException(NOT_SUPPORT_FILTER);
                        if(!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
                            throw new StageException(NOT_VALID_FILTER);
                        booleanBuilder.and(pathBuilder.getBoolean(key).eq(Boolean.valueOf(value)));
                }
            } catch (IllegalArgumentException ex) {
                throw new StageException(NOT_VALID_FILTER);
            }
        });

        // pagination
        Assert.notNull(getQuerydsl(), "QueryDSL must not be null");
        List<Long> ids = getQuerydsl().applyPagination(
                        pageable,
                        getQuerydsl().createQuery()
                                .select(stage.id)
                                .from(stage)
                                .where(booleanBuilder))
                .fetch();

        // select
        JPQLQuery<Stage> jpqlQuery = from(stage)
                .join(stage.host).fetchJoin()
                .join(stage.images).fetchJoin()
                .join(stage.address).fetchJoin()
                .leftJoin(stage.tags).fetchJoin()
                .leftJoin(stage.links).fetchJoin()
                .leftJoin(stage.holidays).fetchJoin()
                .leftJoin(stage.timeBlocks).fetchJoin()
                .leftJoin(stage.refundPolicies).fetchJoin()
                .leftJoin(stage.likedUsernames).fetchJoin();

        return jpqlQuery.where(stage.id.in(ids)).fetch();
    }
}
