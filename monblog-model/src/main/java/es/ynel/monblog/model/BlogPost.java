package es.ynel.monblog.model;


import java.util.Date;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blogpost")
public class BlogPost {

	@Id
	private ObjectId id;
	
	private String title;
	private Date published;
	private String publisher;
	
	@Indexed
	private String link;
	private String content;
	
	@Indexed
	private Set<String> tags;
	
	private long comments;
	private long commentPages;
	
	public ObjectId getId()
	{
		return id;
	}
	public void setId(ObjectId id)
	{
		this.id = id;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public Date getPublished()
	{
		return published;
	}
	public void setPublished(Date published)
	{
		this.published = published;
	}
	public String getPublisher()
	{
		return publisher;
	}
	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}
	public Set<String> getTags()
	{
		return tags;
	}
	public void setTags(Set<String> tags)
	{
		this.tags = tags;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getLink()
	{
		return link;
	}
	public void setLink(String link)
	{
		this.link = link;
	}
	public long getCommentPages()
	{
		return commentPages;
	}
	public void setCommentPages(long commentPages)
	{
		this.commentPages = commentPages;
	}
	public long getComments()
	{
		return comments;
	}
	public void setComments(long commments)
	{
		this.comments = commments;
	}
}
