package com.pushnews.app.model;

/** 新闻列表实体类 */
public class NewsList {
	/** 新闻ID */
	private int news_id;
	/** 新闻类型 */
	private String newsType;
	/** 新闻标题 */
	private String newsTitle;
	/** 新闻概要 */
	private String newsSummary;
	/** 新闻来源 */
	private String newsFrom;
	/** 新闻时间 */
	private String newsTime;

	public NewsList() {
		super();
	}

	public NewsList(String newsType, String newsTitle, String newsSummary,
			String newsFrom, String newsTime) {
		super();
		this.newsType = newsType;
		this.newsTitle = newsTitle;
		this.newsSummary = newsSummary;
		this.newsFrom = newsFrom;
		this.newsTime = newsTime;
	}

	public String toStirng() {
		return "news_id =" + news_id + ";newsType =" + newsType
				+ ";newsTitle =" + newsTitle + ";newsSummary =" + ";newsFrom ="
				+ ";newsTime =" + newsTime;
	}

	public int getId() {
		return news_id;
	}

	public void setId(int news_id) {
		this.news_id = news_id;
	}

	public String getnewsType() {
		return newsType;
	}

	public void setnewsType(String newsType) {
		this.newsType = newsType;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getnewsSummary() {
		return newsSummary;
	}

	public void setnewsSummary(String newsSummary) {
		this.newsSummary = newsSummary;
	}

	public String getNewsFrom() {
		return newsFrom;
	}

	public void setNewsFrom(String newsFrom) {
		this.newsFrom = newsFrom;
	}

	public String getNewsTime() {
		return newsTime;
	}

	public void setNewsTime(String newsTime) {
		this.newsTime = newsTime;
	}
}
