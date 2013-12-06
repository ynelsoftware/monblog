package es.ynel.monblog.repository.custom;

import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import es.ynel.monblog.model.BlogComment;
import es.ynel.monblog.model.BlogComment.Comment;
import es.ynel.monblog.model.BlogPost;
import es.ynel.monblog.repository.BlogCommentRepository;
import es.ynel.monblog.repository.BlogPostRepository;

public class BlogCommentRepositoryImpl implements BlogCommentRepositoryCustom {

	private static final int PAGE_SIZE = 20;

	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private BlogPostRepository blogPostRepository;
	
	@Autowired
	private BlogCommentRepository blogCommentRepository;
	
	@Override
	public BlogComment comment(String text, String author, String email, BlogPost blogpost, Date now)
	{
		Comment comment = new BlogComment().new Comment();
		comment.setId(new ObjectId());
		comment.setPosted(now);
		comment.setText(StringEscapeUtils.escapeHtml4(text));
		comment.setAuthor(author);
		comment.setEmail(email);
		
		blogpost = blogPostRepository.findOne(blogpost.getId());
		
		Query query = new Query(
				Criteria.
				where("blogPostId").is(blogpost.getId()).
				and("page").is(blogpost.getCommentPages()).
				and("count").lt(PAGE_SIZE)
		);
		
		Update update = new Update().
				inc("count", 1).
				push("comments", comment);
		
		BlogComment blogcomment = mongoOperations.findAndModify(query, update, BlogComment.class);
		
		if (blogcomment == null)
		{
			query = new Query(
					Criteria.
					where("blogPostId").is(blogpost.getId()).
					and("page").is(blogpost.getCommentPages() + 1).
					and("count").lt(PAGE_SIZE)
			);
			
			FindAndModifyOptions options = new FindAndModifyOptions()
				.upsert(true)
				.returnNew(true);
			
			blogcomment = mongoOperations.findAndModify(query, update, options, BlogComment.class);
			
			query = new Query(
					Criteria.
					where("id").is(blogpost.getId()).
					and("commentPages").is(blogpost.getCommentPages())
			);
			
			update = new Update().
					inc("commentPages", 1);
			
			mongoOperations.updateFirst(query, update, BlogPost.class);
		}
		
		query = new Query(
				Criteria.
				where("id").is(blogpost.getId())
		);
		
		update = new Update().
				inc("comments", 1);
		
		mongoOperations.updateFirst(query, update, BlogPost.class);
		
		return blogcomment;
	}

	@Override
	public BlogComment comment(String text, String author, String email, BlogPost blogPost)
	{
		return this.comment(text, author, email, blogPost, new Date());
	}
}
