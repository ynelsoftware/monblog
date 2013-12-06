package es.ynel.monblog.repository.custom;

import java.util.Date;

import es.ynel.monblog.model.BlogComment;
import es.ynel.monblog.model.BlogPost;

public interface BlogCommentRepositoryCustom {

	BlogComment comment(String text, String author, String email, BlogPost blogPost, Date now);
	BlogComment comment(String text, String author, String email, BlogPost blogPost);
}
