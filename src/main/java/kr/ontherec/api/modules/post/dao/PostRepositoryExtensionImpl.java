package kr.ontherec.api.modules.post.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import kr.ontherec.api.modules.post.entity.Post;
import kr.ontherec.api.modules.post.entity.QPost;
import kr.ontherec.api.modules.post.exception.PostException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.Assert;

import java.util.List;

import static kr.ontherec.api.modules.post.exception.PostExceptionCode.UNAUTHORIZED;

public class PostRepositoryExtensionImpl extends QuerydslRepositorySupport implements PostRepositoryExtension {
    public PostRepositoryExtensionImpl() {
        super(Post.class);
    }

    @Override
    public List<Post> search(String query, Boolean liked, Pageable pageable, String username) {
        QPost post = QPost.post;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // query
        if(query != null) {
            booleanBuilder.or(post.title.containsIgnoreCase(query))
                    .or(post.content.containsIgnoreCase(query))
                    .or(post.author.containsIgnoreCase(query));
        }

        // like
        if(liked != null && username == null)
            throw new PostException(UNAUTHORIZED);
        if(liked != null && liked)
            booleanBuilder.and(post.likedUsernames.contains(username));
        if(liked != null && !liked)
            booleanBuilder.andNot(post.likedUsernames.contains(username));

        // pagination
        Assert.notNull(getQuerydsl(), "QueryDSL must not be null");
        List<Long> ids = getQuerydsl().applyPagination(
                        pageable,
                        getQuerydsl().createQuery()
                                .select(post.id)
                                .from(post)
                                .where(booleanBuilder))
                .fetch();

        // select
        JPQLQuery<Post> jpqlQuery = from(post)
                .join(post.images).fetchJoin()
                .leftJoin(post.likedUsernames).fetchJoin();

        return jpqlQuery.where(post.id.in(ids)).fetch();
    }
}
