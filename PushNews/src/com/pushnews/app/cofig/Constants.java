package com.pushnews.app.cofig;
/**
 * @desprition 数据库配置常量类
 * 
 * */
public class Constants {

	/**数据库名字*/
	public static final String DATABASE_NAME = "Pushnews.db";
	/**数据库版本*/
	public static final int Version = 1;

	/**频道表*/
	public static class ChannelTable {
		/**频道表名*/
		public static final String TABLE_NAME = "channelManger";
		/**频道ID*/
		public static final String ID = "channel_id";
		/**新闻类型*/
		public static final String NEWS_TYPE = "newsType";
		/**新闻配置标签*/
		public static final String MARK = "mark";
	}
	/**用户表*/
	public static class UserTable {
		/**用户表的表名*/
		public static final String TABLE_NAME = "user";
		/**用户表ID*/
		public static final String ID = "user_id";
		/**用户账号*/
		public static final String USER_NAME = "username";
		/**用户密码*/
		public static final String PASSWORD = "password";
		/**用户配置标签*/
		public static final String MARK = "mark";

	}
	/**新闻列表*/
	public static class NewsListTable {
		/**新闻类表的表名*/
		public static final String TABLE_NAME = "newsList";
		/**数据库列表的ID*/
		public static final String ID = "_id";
		/**新闻的类型*/
		public static final String NEWS_TYPE = "newsType";
		/**新闻的标题*/
		public static final String NEWS_TITLE = "newsTitle";
		/**新闻的概要*/
		public static final String NEWS_SUMMRY = "newsSummary";
		/**新闻的来源*/
		public static final String NEWS_FROM = "newsFrom";
		/**新闻时间*/
		public static final String NEWS_TIME = "newsTime";
		/**新闻图片地址*/
		public static final String NEWS_IMAGE_URL = "imageUrl";
		/**新闻的ID*/
		public static final String NEWS_ID = "newsId";
		/**表示头条*/
		public static final String TOP_LINE = "topLine";

	}
	/** 收藏列表 */
	public static class MycollectTable {
		/**新闻类表的表名*/
		public static final String TABLE_NAME = "newsList";
		/**数据库列表的ID*/
		public static final String ID = "_id";
		/**新闻的类型*/
		public static final String NEWS_TYPE = "newsType";
		/**新闻的标题*/
		public static final String NEWS_TITLE = "newsTitle";
		/**新闻的概要*/
		public static final String NEWS_SUMMRY = "newsSummary";
		/**新闻的来源*/
		public static final String NEWS_FROM = "newsFrom";
		/**新闻时间*/
		public static final String NEWS_TIME = "newsTime";
		/**新闻图片地址*/
		public static final String NEWS_IMAGE_URL = "imageUrl";
		/**新闻的ID*/
		public static final String NEWS_ID = "newsId";
		/**表示头条*/
		public static final String TOP_LINE = "topLine";

	
	}
}
