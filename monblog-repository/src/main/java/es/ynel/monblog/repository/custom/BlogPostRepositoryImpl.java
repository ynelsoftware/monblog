package es.ynel.monblog.repository.custom;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import es.ynel.monblog.model.BlogPost;
import es.ynel.monblog.model.BlogTag;
import es.ynel.monblog.repository.BlogPostRepository;

public class BlogPostRepositoryImpl implements BlogPostRepositoryCustom {

	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private BlogPostRepository blogPostRepository;
	
	@Override
	public BlogPost createBlogPost(BlogPost blogPost)
	{
		String alias = cleanAliasString(blogPost.getTitle());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String date = sdf.format(blogPost.getPublished());
		blogPost.setLink(date + "/" + alias);
		mongoOperations.save(blogPost);
		mapReduceTags();
		return blogPost;
	}
	
	private void mapReduceTags()
	{
		mongoOperations.mapReduce(mongoOperations.getCollectionName(BlogPost.class), "classpath:es/ynel/monblog/repository/blogtagMap.js", "classpath:es/ynel/monblog/repository/blogtagReduce.js", BlogTag.class);
	}

	private String cleanAliasString(String alias)
	{
		alias = alias.trim();
		alias = alias.replaceAll("\\s", "-");
		alias = StringUtils.replaceEach(alias, "ÃÀÁÄÂÈÉËÊÌÍÏÎÒÓÖÔÙÚÜÛãàáäâèéëêìíïîòóöôùúüûÑñÇç".split(""), "AAAAAEEEEIIIIOOOOUUUUaaaaaeeeeiiiioooouuuunncc".split(""));
		alias = alias.replaceAll("[^A-Za-z0-9-]", "");
		alias = alias.replaceAll("-+(?=-)", "");
		alias = alias.toLowerCase();
		alias = alias.replaceAll("\\s", "-");
		return alias;
	}
}
