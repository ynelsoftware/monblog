package es.ynel.monblog.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import es.ynel.monblog.model.BlogPost;
import es.ynel.monblog.repository.custom.BlogPostRepositoryCustom;

public interface BlogPostRepository extends PagingAndSortingRepository<BlogPost, ObjectId>, BlogPostRepositoryCustom {

	BlogPost findByLink(String link);

	Page<BlogPost> findByTags(String tag, Pageable page);
}
