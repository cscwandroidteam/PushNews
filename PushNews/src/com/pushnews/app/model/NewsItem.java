package com.pushnews.app.model;

/** 新闻列表实体类 */
public class NewsItem {
	/** 新闻ID */
	private  int news_id;
	/** 新闻类型 */
	private  String newsType;
	/** 新闻标题 */
	private  String newsTitle;
	/** 新闻概要 */
	private  String newsSummary;
	/** 新闻来源 */
	private  String newsFrom;
	/** 新闻时间 */
	private  String newsTime;
	/**新闻图片地址*/
	private  String imageUrl;
	/**新闻详细内容地址*/
	private String detialUrl;
	
	
	public NewsItem(int news_id,String newsType, String newsTitle, String newsSummary,
			String newsFrom, String newsTime,String imageUrl,String detailUrl) {
		super();		
		this.newsType = newsType;
		this.newsTitle = newsTitle;
		this.newsSummary = newsSummary;
		this.newsFrom = newsFrom;
		this.newsTime = newsTime;
		this.imageUrl = imageUrl;
		this.news_id = news_id;
		this.detialUrl = detailUrl;
	}

	public String toStirng() {
		return "news_id =" + news_id + ";newsType =" + newsType
				+ ";newsTitle =" + newsTitle + ";newsSummary =" + ";newsFrom ="
				+ ";newsTime =" + newsTime;
	}

	public int getNews_id() {
		return news_id;
	}

	public void setNews_id(int news_id) {
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

	public String getNewsTime() {
		return newsTime;
	}

	public void setNewsTime(String newsTime) {
		this.newsTime = newsTime;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDetialUrl() {
		return detialUrl;
	}

	public void setDetialUrl(String detialUrl) {
		this.detialUrl = detialUrl;
	}

	
}
