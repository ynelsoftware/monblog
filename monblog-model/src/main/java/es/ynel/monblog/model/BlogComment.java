package es.ynel.monblog.model;


import java.util.Date;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blogcomment")
@CompoundIndexes({
	@CompoundIndex(name="blogPostIdAndPage", def = "{'blogPostId' : 1, 'page' : 1}")
})
public class BlogComment {

	@Id
	private ObjectId id;
	
	@Indexed
	private ObjectId blogPostId;
	
	@Indexed
	private long page;
	private long count;
	
	private Set<Comment> comments;

	public ObjectId getId()
	{
		return id;
	}

	public void setId(ObjectId id)
	{
		this.id = id;
	}
	
	public ObjectId getBlogPostId()
	{
		return blogPostId;
	}

	public void setBlogPostId(ObjectId blogId)
	{
		this.blogPostId = blogId;
	}

	public long getPage()
	{
		return page;
	}

	public void setPage(long page)
	{
		this.page = page;
	}

	public long getCount()
	{
		return count;
	}

	public void setCount(long count)
	{
		this.count = count;
	}

	public Set<Comment> getComments()
	{
		return comments;
	}

	public void setComments(Set<Comment> posts)
	{
		this.comments = posts;
	}
	
	public class Comment {
		
		private ObjectId id;
		private Date posted;
		private String author;
		private String text;
		private String email;
		
		public ObjectId getId()
		{
			return id;
		}
		public void setId(ObjectId id)
		{
			this.id = id;
		}
		public Date getPosted()
		{
			return posted;
		}
		public void setPosted(Date posted)
		{
			this.posted = posted;
		}
		public String getAuthor()
		{
			return author;
		}
		public void setAuthor(String author)
		{
			this.author = author;
		}
		public String getText()
		{
			return text;
		}
		public void setText(String text)
		{
			this.text = text;
		}
		public String getEmail()
		{
			return email;
		}
		public void setEmail(String email)
		{
			this.email = email;
		}
	}
}
