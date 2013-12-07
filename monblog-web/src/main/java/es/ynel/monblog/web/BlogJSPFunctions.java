package es.ynel.monblog.web;

import java.util.List;

import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import es.ynel.monblog.model.BlogPost;
import es.ynel.monblog.model.BlogTag;
import es.ynel.monblog.repository.BlogPostRepository;
import es.ynel.monblog.repository.BlogTagRepository;

@Component
public class BlogJSPFunctions {

	private static BlogPostRepository blogPostRepository;
	private static BlogTagRepository blogTagRepository;
	
	/**
	 * @deprecated Use {@link #getPopularBlogTags(int)}
	 */
	@Deprecated
	public static List<BlogTag> getBlogTags()
	{
		return blogTagRepository.findAll(new PageRequest(0, 20, new Sort(new Sort.Order(Direction.DESC, "value"), new Sort.Order(Direction.ASC, "id")))).getContent();
	}
	
	/**
	 * @since 0.6.0
	 * @param tags Number of popular tags to return
	 */
	public static List<BlogTag> getPopularBlogTags(int tags)
	{
		return blogTagRepository.findAll(new PageRequest(0, tags, new Sort(new Sort.Order(Direction.DESC, "value"), new Sort.Order(Direction.ASC, "id")))).getContent();
	}
	
	/**
	 * @since 0.6.0 Rename from getBlogEntries(int entries) to getLastBlogPosts(int posts)
	 * @param posts
	 */
	public static List<BlogPost> getLastBlogPosts(int posts)
	{
		return blogPostRepository.findAll(new PageRequest(0, posts, new Sort(Direction.DESC, "published"))).getContent();
	}
	
	/**
	 * @since 0.6.0
	 */
	public static Iterable<BlogTag> getAllBlogTags()
	{
		return blogTagRepository.findAll(new Sort(new Sort.Order(Direction.ASC, "id")));
	}
	
	/**
	 * @since 0.6.0
	 */
	public static String parseMarkDown(String text)
	{
		PegDownProcessor processor = new PegDownProcessor();
		return processor.markdownToHtml(text);
	}
	
	@Autowired
	public void setBlogRepository(BlogTagRepository blogTagRepository)
	{
		BlogJSPFunctions.blogTagRepository = blogTagRepository;
	}

	@Autowired
	public void setBlogPostRepository(BlogPostRepository blogRepository)
	{
		BlogJSPFunctions.blogPostRepository = blogRepository;
	}
}
