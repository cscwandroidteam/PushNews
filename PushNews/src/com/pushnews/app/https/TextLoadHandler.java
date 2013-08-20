package com.pushnews.app.https;

import java.util.ArrayList;
import java.util.List;

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
	
	private List<NewsItem> newsList = new ArrayList<NewsItem>();

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
			long newsTime = 0;
			/** 获取网络端的数据并进行json解析 */
			jsonData = HttpUtils.postByHttpURLConnection(baseUrl, null);
			if (judgeNewsUpdate() == 1) {
				JSONArray newsListarray = new JSONArray(jsonData);
				for (int i = 0; i < newsListarray.length(); i++) {
					JSONObject newsItems = (JSONObject) newsListarray.opt(i);
					String newsId = newsItems.getString("id");
					System.out.println("" + newsId);
					String imageUrl = "http://a1.eoe.cn/thumb/www/home/201307/28/f12b/51f4874532e03.jpg-108x72-5.jpg";
					System.out.println(imageUrl);
					String newsTitle = newsItems.getString("mainTitle");
					System.out.println(newsTitle);
					String newsType = "国内";
					newsTime = (Long) newsItems.get("releaseTime");
					System.out.println(""+newsTime);
					int topLine = (int) newsItems.getInt("topLine");
					String newsSummary = "熊宇，eoe上海同城会现任负责人，eoeAndroid社区问答...";		
					String newsFrom = "广州";
					NewsItem item = new NewsItem(newsId, newsType, newsTitle,
							newsSummary, newsFrom, newsTime, imageUrl,topLine);
					newsList.add(item);
				}
			}
			Message message = new Message();
			/** 完成后发送消息 */
			message.what = Messages.TASK_SUCCESSS;
			message.obj = newsList;
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
