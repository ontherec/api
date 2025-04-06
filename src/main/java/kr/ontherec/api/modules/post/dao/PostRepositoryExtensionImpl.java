package kr.ontherec.api.modules.post.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.jpa.JPQLQuery;
import kr.ontherec.api.modules.post.entity.Post;
import kr.ontherec.api.modules.post.entity.QPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.Assert;

import java.util.List;

public class PostRepositoryExtensionImpl extends QuerydslRepositorySupport implements PostRepositoryExtension {
    public PostRepositoryExtensionImpl() {
        super(Post.class);
    }

    @Override
    public List<Post> search(String query, Pageable pageable, String username) {
        QPost post = QPost.post;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // query
        if(query != null) {
            booleanBuilder.or(post.title.containsIgnoreCase(query))
                    .or(post.content.containsIgnoreCase(query))
                    .or(post.author.containsIgnoreCase(query));
        }

        // pagination
        Assert.notNull(getQuerydsl(), "QueryDSL must not be null");
        SubQueryExpression<Post> subQuery = getQuerydsl().applyPagination(pageable, from(post).where(booleanBuilder));

        JPQLQuery<Post> jpqlQuery = from(post)
                .join(post.images).fetchJoin()
                .leftJoin(post.likedUsernames);

        if(username != null) {
            jpqlQuery.on(post.likedUsernames.any().eq(username));
        }

        return jpqlQuery.where(post.in(subQuery)).fetch();
    }
}
