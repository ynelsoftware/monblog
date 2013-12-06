package es.ynel.monblog.web;

import java.util.List;

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
	
	public static List<BlogTag> getBlogTags()
	{
		return blogTagRepository.findAll(new PageRequest(0, 20, new Sort(new Sort.Order(Direction.DESC, "value"), new Sort.Order(Direction.ASC, "id")))).getContent();
	}
	
	public static List<BlogPost> getBlogEntries(int entries)
	{
		return blogPostRepository.findAll(new PageRequest(0, entries, new Sort(Direction.DESC, "published"))).getContent();
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
