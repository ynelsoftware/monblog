package es.ynel.monblog.controller.bean;

import org.springframework.data.domain.Page;

public class Pagination 
{
	private int page;
	private long totalPages;
	private String link;
	
	public Pagination(Page page, String link)
	{
		this(page.getNumber() + 1, page.getTotalPages(), link);
	}
	
	public Pagination(int page, long totalPages, String link)
	{
		this.page = page;
		this.totalPages = totalPages;
		this.link = link;
	}
	
	public long getInitPage()
	{
		if (this.totalPages <= 5 || this.page <= 3)
		{
			return 1;
		}
		
		return this.totalPages - this.page < 2 ? this.totalPages - 4 : this.page - 2;
	}
	
	public long getEndPage()
	{
		if (this.totalPages <= 5)
		{
			return this.totalPages;
		}
		
		if (this.page <= 3)
		{
			return 5;
		}
		
		return this.page + 2 < this.totalPages ? this.page + 2 : this.totalPages;
	}

	public long getTotalPages()
	{
		return this.totalPages;
	}
	
	public int getPage()
	{
		return page;
	}

	public String getLink()
	{
		return link;
	}

	public boolean isShowGoFirstPage()
	{
		if (this.totalPages <= 5 || this.page <= 3)
		{
			return false;
		}
		
		return true;
	}

	public boolean isShowGoLastPage()
	{
		if (this.totalPages <= 5 || this.page + 3 > this.totalPages)
		{
			return false;
		}
		
		return true;
	}
}
