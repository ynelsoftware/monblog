package es.ynel.monblog.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import es.ynel.monblog.model.BlogTag;

public interface BlogTagRepository extends PagingAndSortingRepository<BlogTag, String> {

}
