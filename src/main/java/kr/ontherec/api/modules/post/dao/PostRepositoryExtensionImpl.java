package kr.ontherec.api.modules.post.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.SubQueryExpression;
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

        // select
        JPQLQuery<Post> jpqlQuery = from(post)
                .join(post.images).fetchJoin()
                .leftJoin(post.likedUsernames);

        // query
        if(query != null) {
            booleanBuilder.or(post.title.containsIgnoreCase(query))
                    .or(post.content.containsIgnoreCase(query))
                    .or(post.author.containsIgnoreCase(query));
        }

        // like
        if(liked != null && username == null)
            throw new PostException(UNAUTHORIZED);
        if(liked != null && liked) {
            jpqlQuery.on(post.likedUsernames.any().eq(username));
            booleanBuilder.and(post.likedUsernames.contains(username));
        }
        if(liked != null && !liked){
            jpqlQuery.on(post.likedUsernames.any().eq(username));
            booleanBuilder.andNot(post.likedUsernames.contains(username));
        }

        // pagination
        Assert.notNull(getQuerydsl(), "QueryDSL must not be null");
        SubQueryExpression<Post> subQuery = getQuerydsl().applyPagination(pageable, from(post).where(booleanBuilder));

        return jpqlQuery.where(post.in(subQuery)).fetch();
    }
}
