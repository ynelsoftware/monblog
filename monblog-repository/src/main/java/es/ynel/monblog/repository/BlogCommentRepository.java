package es.ynel.monblog.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import es.ynel.monblog.model.BlogComment;
import es.ynel.monblog.repository.custom.BlogCommentRepositoryCustom;

public interface BlogCommentRepository extends PagingAndSortingRepository<BlogComment, ObjectId>, BlogCommentRepositoryCustom {

	BlogComment findByBlogPostIdAndPage(ObjectId blogPostId, int page);
}
