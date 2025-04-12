package kr.ontherec.api.modules.practiceroom.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import kr.ontherec.api.infra.util.StopWatch;
import kr.ontherec.api.modules.post.exception.PostException;
import kr.ontherec.api.modules.practiceroom.entity.PracticeRoom;
import kr.ontherec.api.modules.practiceroom.entity.QPracticeRoom;
import kr.ontherec.api.modules.practiceroom.exception.PracticeRoomException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static com.querydsl.core.types.dsl.PathBuilderValidator.PROPERTIES;
import static kr.ontherec.api.infra.util.ReflectionUtils.getFieldTypeMap;
import static kr.ontherec.api.modules.post.exception.PostExceptionCode.UNAUTHORIZED;
import static kr.ontherec.api.modules.practiceroom.exception.PracticeRoomExceptionCode.NOT_SUPPORT_FILTER;
import static kr.ontherec.api.modules.practiceroom.exception.PracticeRoomExceptionCode.NOT_VALID_FILTER;

public class PracticeRoomRepositoryExtensionImpl extends QuerydslRepositorySupport implements PracticeRoomRepositoryExtension {

    public PracticeRoomRepositoryExtensionImpl() {
        super(PracticeRoom.class);
    }

    @Override
    @StopWatch
    public List<PracticeRoom> search(Map<String, String> params, Pageable pageable, String username) {
        QPracticeRoom practiceRoom = QPracticeRoom.practiceRoom;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        PathBuilder<PracticeRoom> pathBuilder = new PathBuilder<>(practiceRoom.getType(), practiceRoom.getMetadata(), PROPERTIES);
        Map<String, Class<?>> fieldTypes = getFieldTypeMap(PracticeRoom.class);

        // query
        if(params.containsKey("q")) {
            String query = params.get("q");
            booleanBuilder.or(practiceRoom.title.containsIgnoreCase(query))
                    .or(practiceRoom.address.state.containsIgnoreCase(query))
                    .or(practiceRoom.address.city.containsIgnoreCase(query))
                    .or(practiceRoom.address.streetAddress.containsIgnoreCase(query))
                    .or(practiceRoom.tags.any().containsIgnoreCase(query))
                    .or(practiceRoom.content.containsIgnoreCase(query));
        }

        // filter
        params.forEach((key, value) -> {
            try {
                switch(key) {
                    case "q":
                        break;
                    case "parkingAvailable":
                        if(!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
                            throw new PracticeRoomException(NOT_VALID_FILTER);
                        if(value.equalsIgnoreCase("true"))
                            booleanBuilder.and(pathBuilder.getNumber("parkingCapacity", Integer.class).gt(0));
                        if(value.equalsIgnoreCase("false"))
                            booleanBuilder.and(pathBuilder.getNumber("parkingCapacity", Integer.class).loe(0));
                        break;
                    case "liked":
                        if(username == null)
                            throw new PostException(UNAUTHORIZED);
                        if(!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
                            throw new PracticeRoomException(NOT_VALID_FILTER);
                        if(Boolean.parseBoolean(value))
                            booleanBuilder.and(practiceRoom.likedUsernames.contains(username));
                        else
                            booleanBuilder.andNot(practiceRoom.likedUsernames.contains(username));
                        break;
                    default:
                        if(!fieldTypes.containsKey(key) || !fieldTypes.get(key).getSimpleName().equalsIgnoreCase("boolean"))
                            throw new PracticeRoomException(NOT_SUPPORT_FILTER);
                        if(!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
                            throw new PracticeRoomException(NOT_VALID_FILTER);
                        booleanBuilder.and(pathBuilder.getBoolean(key).eq(Boolean.valueOf(value)));
                }
            } catch (IllegalArgumentException ex) {
                throw new PracticeRoomException(NOT_VALID_FILTER);
            }
        });

        // pagination
        Assert.notNull(getQuerydsl(), "QueryDSL must not be null");
        List<Long> ids = getQuerydsl().applyPagination(
                        pageable,
                        getQuerydsl().createQuery()
                                .select(practiceRoom.id)
                                .from(practiceRoom)
                                .where(booleanBuilder))
                .fetch();

        // select
        JPQLQuery<PracticeRoom> jpqlQuery = from(practiceRoom)
                .join(practiceRoom.host).fetchJoin()
                .join(practiceRoom.images).fetchJoin()
                .join(practiceRoom.address).fetchJoin()
                .leftJoin(practiceRoom.tags).fetchJoin()
                .leftJoin(practiceRoom.links).fetchJoin()
                .leftJoin(practiceRoom.holidays).fetchJoin()
                .leftJoin(practiceRoom.timeBlocks).fetchJoin()
                .leftJoin(practiceRoom.refundPolicies).fetchJoin()
                .leftJoin(practiceRoom.likedUsernames).fetchJoin();

        return jpqlQuery.where(practiceRoom.id.in(ids)).fetch();
    }
}
