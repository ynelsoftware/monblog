package es.ynel.monblog.repository.custom;

import es.ynel.monblog.model.BlogPost;

public interface BlogPostRepositoryCustom {

	public BlogPost createBlogPost(BlogPost blog);
	
	/**
	 * @since 0.6.0
	 */
	BlogPost addVisit(String link);
}
