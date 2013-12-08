package es.ynel.monblog.controller.action;

import org.springframework.ui.ModelMap;

import es.ynel.monblog.model.BlogComment;
import es.ynel.monblog.model.BlogPost;

public interface PostBlogControllerActions 
{
	void postGetBlogPageAction(ModelMap modelMap, int page);
	void postShowBlogPostAction(ModelMap modelMap, BlogPost blogPost, BlogComment blogComment);
	void postCommentPostAction(ModelMap modelMap, BlogPost blog, BlogComment blogComment);
	void postShowPostsByTagAction(ModelMap modelMap, String tag, int page);
	
	/**
	 * @throws Exception 
	 * @since 0.6.0
	 */
	void postCreateBlogPost(BlogPost blogPost) throws Exception;
}
