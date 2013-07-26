package com.pushnews.app.model;

/** 频道实体类 */
public class Channel {
	/** 新闻ID */
	private int channel_id;
	/** 新闻类型 */
	private String newsType;
	/** 新闻的配置标签 */
	private int mark;

	public Channel() {
		super();
	}

	public Channel(String newsType, int mark) {
		super();
		this.newsType = newsType;
		this.mark = mark;
	}

	public String getnewsType() {
		return newsType;
	}

	public void setnewsType(String newsType) {
		this.newsType = newsType;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public int getId() {
		return channel_id;
	}

	public void setId(int channel_id) {
		this.channel_id = channel_id;
	}

	@Override
	public String toString() {
		return "channel_id=" + channel_id + ";newsType=" + newsType + ";mark="
				+ mark;
	}
}
