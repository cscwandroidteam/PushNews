package com.pushnews.app.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pushnews.app.R;
import com.pushnews.app.adapter.NewListAdapter;
import com.pushnews.app.cofig.Constants;
import com.pushnews.app.cofig.Messages;
import com.pushnews.app.cofig.Urls;
import com.pushnews.app.db.NewsItemDbManger;
import com.pushnews.app.https.TextLoadHandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;

public class MainActivity extends Activity {

	private String url = Urls.NewsDetailUrl;
	private List<HashMap<String, Object>> newlist = new ArrayList<HashMap<String, Object>>();
	private ListView newList;
	private Handler uiHandler;
/*	private Runnable showRunable;*/
	private TextLoadHandler textLoadHandler;
	private NewsItemDbManger dbManger;
	private NewListAdapter adapter;
	private Button button;
	// 固定五个线程来执行任务
	private static ExecutorService executorService = Executors.newFixedThreadPool(5);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel_manger_listselector);
		dbManger = new NewsItemDbManger(this);
		newList = (ListView) findViewById(R.id.cmls_list_v);
		button = (Button)findViewById(R.id.cmls_ibt_back);
		button.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				dbManger.open();
				dbManger.deleteNews();
				dbManger.close();
				
			}
		});
		uiUpdate();
		adapter = new NewListAdapter(this, newlist,
				R.layout.newslist_item, new String[] { "id", "image", "title",
				"time", "short_content" }, new int[] {
				R.id.newslist_item_news_picture,
				R.id.newslist_item_title, R.id.newslist_item_time,
				R.id.newslist_item_digest });
	}	
	private void uiUpdate() {
		/**接受文本加载线程的消息*/
		uiHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Messages.TASK_SUCCESSS:
					dbManger.open();
					getDataFormDb();
					dbManger.close();
					newList.setAdapter(adapter);
					System.out.println("123===123");
					break;
				}
			}
		};
		/**文本加载线程*/
		textLoadHandler = new TextLoadHandler(this, uiHandler, url);
		textLoadHandler.start();
/*		*//**当前界面发消息给文本加载线程*//*
		showRunable = new Runnable() {
			@Override
			public void run() {
				// 給线程发送一个Message				
				textLoadHandler.getHandler().sendEmptyMessage(
						Messages.TASK_BEGIN);
				System.out.println("=======");
			}			
		};		
		uiHandler.post(showRunable);		*/
	}

	private void getDataFormDb(){
		
		Cursor cursor = dbManger.getdiaries();
		startManagingCursor(cursor);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put(
						"id",
						cursor.getInt(cursor
								.getColumnIndex(Constants.NewsListTable.NEWS_ID)));
				dataMap.put(
						"image",
						cursor.getString(cursor
								.getColumnIndex(Constants.NewsListTable.NEWS_IMAGE_URL)));
				dataMap.put(
						"title",
						cursor.getString(cursor
								.getColumnIndex(Constants.NewsListTable.NEWS_TITLE)));
				dataMap.put(
						"time",
						cursor.getString(cursor
								.getColumnIndex(Constants.NewsListTable.NEWS_TIME)));
				dataMap.put(
						"short_content",
						cursor.getString(cursor
								.getColumnIndex(Constants.NewsListTable.NEWS_SUMMRY)));
				newlist.add(dataMap);
				System.out.println(cursor.getString(cursor
						.getColumnIndex(Constants.NewsListTable.NEWS_SUMMRY)));

			} while (cursor.moveToNext());
			System.out.println(newlist.size());
		
		}
		
	}
	

}
