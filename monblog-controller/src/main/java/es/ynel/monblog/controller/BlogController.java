package es.ynel.monblog.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import es.ynel.monblog.controller.action.PostBlogControllerActions;
import es.ynel.monblog.controller.bean.CommentBean;
import es.ynel.monblog.controller.bean.Pagination;
import es.ynel.monblog.model.BlogComment;
import es.ynel.monblog.model.BlogPost;
import es.ynel.monblog.model.BlogTag;
import es.ynel.monblog.repository.BlogCommentRepository;
import es.ynel.monblog.repository.BlogPostRepository;
import es.ynel.monblog.repository.BlogTagRepository;

@Controller
@RequestMapping("/blog")
public class BlogController {

	@Autowired
	private BlogPostRepository blogPostRepository;
	
	@Autowired
	private BlogCommentRepository blogCommentRepository;
	
	@Autowired
	private BlogTagRepository blogTagRepository;
	
	@Autowired
	private PostBlogControllerActions actions;
	
	@RequestMapping
	public ModelAndView index(ModelMap model) throws NoSuchRequestHandlingMethodException
	{
		return getBlogPage(1, model);
	}
	
	@RequestMapping(value = "/page/{page}")
	public ModelAndView getBlogPage(@PathVariable("page") int page, ModelMap model) throws NoSuchRequestHandlingMethodException
	{
		Page<BlogPost> posts = blogPostRepository.findAll(new PageRequest(page - 1, 5, new Sort(Direction.DESC, "published")));
		
		if (posts.getNumberOfElements() == 0)
		{
			throw new NoSuchRequestHandlingMethodException("/blog/page/" + page, "page", null);
		}
		
		Pagination pagination = new Pagination(posts, "blog/page");
		
		model.put("posts", posts.getContent());
		model.put("pagination", pagination);
		
		if (actions != null)
		{
			actions.postGetBlogPageAction(model, page);
		}
		
		return new ModelAndView("blog/index", "model", model);
	}
	
	@RequestMapping(value = "/{year}/{month}/{day}/{alias}")
	public ModelAndView showBlogPost(@PathVariable("day") String day, @PathVariable("month") String month, @PathVariable("year") String year, @PathVariable("alias") String alias, ModelMap model) throws NoSuchRequestHandlingMethodException
	{
		BlogPost blogPost = getBlogPost(day, month, year, alias, true);
		
		BlogComment blogComment = blogCommentRepository.findByBlogPostIdAndPage(blogPost.getId(), 1);
		
		model.put("blogPost", blogPost);
		model.put("blogComment", blogComment);
		
		if (actions != null)
		{
			actions.postShowBlogPostAction(model, blogPost, blogComment);
		}
		
		if (!model.containsKey("commentBean"))
		{
			model.addAttribute(new CommentBean());
		}
		
		return new ModelAndView("blog/post", "model", model);
	}

	private BlogPost getBlogPost(String day, String month, String year, String alias, boolean addVisit) throws NoSuchRequestHandlingMethodException
	{
		String link = year + "/" + month + "/" + day + "/" + alias;
		BlogPost blog = (addVisit ? blogPostRepository.addVisit(link) : blogPostRepository.findByLink(link));
		
		if (blog == null)
		{
			throw new NoSuchRequestHandlingMethodException("/" + link, "showEntry", null);
		}
		return blog;
	}
	
	@RequestMapping(value = "/{year}/{month}/{day}/{alias}/comment", method = RequestMethod.POST)
	public ModelAndView commentPost(@PathVariable("day") String day, @PathVariable("month") String month, @PathVariable("year") String year, @PathVariable("alias") String alias, ModelMap model, @Valid CommentBean commentBean, BindingResult result, RedirectAttributes redirectAttributes) throws NoSuchRequestHandlingMethodException
	{
		BlogPost blog = getBlogPost(day, month, year, alias, false);
		
		RedirectView view = new RedirectView("/blog/" + year +"/" + month + "/" + day + "/" + alias, true);
		view.setExposeModelAttributes(false);
		
		if (result.hasErrors())
		{
			redirectAttributes.addFlashAttribute("commentBean", commentBean);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.commentBean", result);
			return new ModelAndView(view);
		}
		
		BlogComment blogComment = blogCommentRepository.comment(commentBean.getText(), commentBean.getUser(), commentBean.getEmail(), blog);
		
		if (actions != null)
		{
			actions.postCommentPostAction(model, blog, blogComment);
		}
		
		return new ModelAndView(view);
	}
	
	@RequestMapping(value = "/tag/{tag}")
	public ModelAndView showPostsByTag(@PathVariable("tag") String tag, ModelMap model) throws NoSuchRequestHandlingMethodException
	{
		return this.showPostsByTag(tag, 1, model);
	}
	
	@RequestMapping(value = "/tag/{tag}/page/${page}")
	public ModelAndView showPostsByTag(@PathVariable("tag") String tag, @PathVariable("page") int page, ModelMap model) throws NoSuchRequestHandlingMethodException
	{
		Page<BlogPost> posts = blogPostRepository.findByTags(tag, new PageRequest(page - 1, 5, new Sort(Direction.DESC, "published")));
		
		if (posts.getNumberOfElements() == 0)
		{
			throw new NoSuchRequestHandlingMethodException("/blog/tag/" + tag + "/page" + page, "showEntryByTag", null);
		}
		
		Pagination pagination = new Pagination(posts, "blog/page");
		
		model.put("posts", posts.getContent());
		model.put("pagination", pagination);
		
		if (actions != null)
		{
			actions.postShowPostsByTagAction(model, tag, page);
		}
		
		return new ModelAndView("blog/index", "model", model);
	}
	
	/**
	 * @since 0.6.0
	 */
	@RequestMapping(value = "/addPost")
	public ModelAndView createBlogPost(ModelMap model)
	{
		if (!model.containsKey("blogPost"))
		{
			model.addAttribute(new BlogPost());
		}
		
		Iterable<BlogTag> tags = blogTagRepository.findAll(new Sort(new Sort.Order(Direction.ASC, "id")));
		model.put("tags", tags);
		return new ModelAndView("blog/addPost", "model", model);
	}
	
	/**
	 * @throws Exception 
	 * @since 0.6.0
	 */
	@RequestMapping(value = "/addPost", method = RequestMethod.POST)
	public ModelAndView createBlogPost(@Valid BlogPost blogPost, BindingResult result, ModelMap model) throws Exception
	{
		if (result.hasErrors())
		{
			return createBlogPost(model);
		}
		
		blogPost = blogPostRepository.createBlogPost(blogPost);
		
		if (actions != null)
		{
			try
			{
				actions.postCreateBlogPost(blogPost);
			}
			catch (Exception e)
			{
				throw e;
			}
		}
		
		return new ModelAndView("redirect:/blog/" + blogPost.getLink());
	}
}
