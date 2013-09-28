package com.pushnews.app.model;

/** 收藏列表实体类 */
public class Mycollect {
	/** 新闻ID */
	private String news_id;
	/** 新闻类型 */
	private String newsType;
	/** 新闻标题 */
	private String newsTitle;
	/** 新闻概要 */
	private String newsSummary;
	/** 新闻来源 */
	private String newsFrom;
	/** 新闻时间 */
	private long newsTime;
	/** 新闻图片地址 */
	private String imageUrl;
	/** 0：表示不是头条，1：表示头条*/
	private int topLine;

	public Mycollect(String news_id, String newsType, String newsTitle,
			String newsSummary, String newsFrom, long newsTime,
			String imageUrl, int topLine) {
		super();
		this.newsType = newsType;
		this.newsTitle = newsTitle;
		this.newsSummary = newsSummary;
		this.newsFrom = newsFrom;
		this.newsTime = newsTime;
		this.imageUrl = imageUrl;
		this.news_id = news_id;
		this.topLine = topLine;
	}
	
	public Mycollect(String news_id, String newsTitle,
			String newsSummary, long newsTime,
			String imageUrl){
		super();
		this.newsTitle = newsTitle;
		this.newsSummary = newsSummary;
		this.newsTime = newsTime;
		this.imageUrl = imageUrl;
		this.news_id = news_id;
	}

	public String toStirng() {
		return "news_id =" + news_id + ";newsType =" + newsType
				+ ";newsTitle =" + newsTitle + ";newsSummary =" + ";newsFrom ="
				+ ";newsTime =" + newsTime;
	}

	public String getNews_id() {
		return news_id;
	}

	public void setNews_id(String news_id) {
		this.news_id = news_id;
	}

	public String getNewsType() {
		return newsType;
	}

	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getNewsSummary() {
		return newsSummary;
	}

	public void setNewsSummary(String newsSummary) {
		this.newsSummary = newsSummary;
	}

	public String getNewsFrom() {
		return newsFrom;
	}

	public void setNewsFrom(String newsFrom) {
		this.newsFrom = newsFrom;
	}

	public long getNewsTime() {
		return newsTime;
	}

	public void setNewsTime(long newsTime) {
		this.newsTime = newsTime;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getTopLine() {
		return topLine;
	}

	public void setTopLine(int topLine) {
		this.topLine = topLine;
	}

}
