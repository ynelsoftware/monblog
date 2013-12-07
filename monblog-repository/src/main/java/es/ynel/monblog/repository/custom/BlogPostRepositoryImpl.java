package es.ynel.monblog.repository.custom;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;

import es.ynel.monblog.model.BlogPost;
import es.ynel.monblog.model.BlogTag;
import es.ynel.monblog.repository.BlogPostRepository;
import es.ynel.monblog.repository.BlogTagRepository;

public class BlogPostRepositoryImpl implements BlogPostRepositoryCustom {

	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private BlogPostRepository blogPostRepository;
	
	@Autowired
	private BlogTagRepository blogTagRepository;
	
	@Override
	public BlogPost createBlogPost(BlogPost blogPost)
	{
		String alias = cleanAliasString(blogPost.getTitle());
		
		if (blogPost.getPublished() == null)
		{
			blogPost.setPublished(new Date());
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String date = sdf.format(blogPost.getPublished());
		blogPost.setLink(date + "/" + alias);
		
		mongoOperations.save(blogPost);
		mapReduceTags();
		return blogPost;
	}
	
	private void mapReduceTags()
	{
		final MapReduceResults<BlogTag> results = mongoOperations.mapReduce(mongoOperations.getCollectionName(BlogPost.class), "classpath:es/ynel/monblog/repository/blogtagMap.js", "classpath:es/ynel/monblog/repository/blogtagReduce.js", BlogTag.class);
		blogTagRepository.save(new Iterable<BlogTag>() {

			@Override
			public Iterator<BlogTag> iterator()
			{
				return results.iterator();
			}
		});
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
