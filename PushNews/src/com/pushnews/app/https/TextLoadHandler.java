package com.pushnews.app.https;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pushnews.app.cofig.Messages;
import com.pushnews.app.db.NewsItemDbManger;
import com.pushnews.app.model.NewsItem;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * 文本异步加载类
 * 
 * @author LZB
 * 
 * */
public class TextLoadHandler extends Thread {
	private static final String TAG = "TextLoadHandler";
	/** 接受其他线程消息的handler */
	private Handler mhandler;
	/** 需要加载文本的地址 */
	private String baseUrl;
	/** 发送消息至其他线程的handler */
	private Handler uiHandler;
	/** 新闻数据库管理类 */
	private NewsItemDbManger newsItemDbManger;
	/** 来自网络端还未解析的json数据， */
	private String jsonData;

	/** 进行与其他线程通信的构造函数 */
	public TextLoadHandler(Context context, Handler handler, String url) {
		this.uiHandler = handler;
		this.baseUrl = url;
		newsItemDbManger = new NewsItemDbManger(context);
/*		// 初始化Handler，接收到主线程发送过来的Message就回复一个Message给主线程
		mhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Messages.TASK_BEGIN:
					break;
				}
			};
		};*/
	}

	@Override
	public void run() {
		Looper.prepare();
		try {
			/** 获取网络端的数据并进行json解析 */
			jsonData = HttpUtils.postByHttpURLConnection(baseUrl, null);
			if (judgeNewsUpdate() == 1) {
				JSONObject jsonObject = new JSONObject(jsonData);
				JSONObject newsData = jsonObject.getJSONObject("response");
				JSONArray newsList = newsData.getJSONArray("items");
				for (int i = 0; i < newsList.length(); i++) {
					JSONObject newsItems = (JSONObject) newsList.opt(i);
					int newsId = (Integer) newsItems.getInt("id");
					System.out.println("" + newsId);
					String imageUrl = newsItems.getString("thumbnail_url");
					System.out.println(imageUrl);
					String newsTitle = newsItems.getString("title");
					System.out.println(newsTitle);
					String newsType = newsData.getString("name");
					System.out.println(newsType);
					String newsTime = newsItems.getString("time");
					System.out.println(newsTime);
					String newsSummary = newsItems.getString("short_content");
					System.out.println(newsSummary);
					String detailUrl = newsItems.getString("detail_url");
					String newsFrom = "广州";
					NewsItem item = new NewsItem(newsId, newsType, newsTitle,
							newsSummary, newsFrom, newsTime, imageUrl,detailUrl);
					newsItemDbManger.open();
					/** 将解析的json数据加入数据库 */
					newsItemDbManger.addNews(item);
					newsItemDbManger.close();
				}
			}
			Message message = new Message();
			/** 完成后发送消息 */
			message.what = Messages.TASK_SUCCESSS;
			uiHandler.sendMessage(message);
			System.out.println("wanchen");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getLocalizedMessage());
			e.printStackTrace();
		}
		// 当前线程处于等待状态
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		Looper.loop();
	}

	/* 测试用的 */
	private int judgeNewsUpdate() throws JSONException {
		return 1;

	}

	public Handler getHandler() {
		return mhandler;
	}

	public void setHandler(Handler handler) {
		this.mhandler = handler;
	}
}
