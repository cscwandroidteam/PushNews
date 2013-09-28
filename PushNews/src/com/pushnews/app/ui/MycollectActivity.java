package com.pushnews.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pushnews.app.R;
import com.pushnews.app.adapter.NewListAdapter;
import com.pushnews.app.cofig.Constants;
import com.pushnews.app.cofig.Urls;
import com.pushnews.app.db.MycollectDbManger;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MycollectActivity extends Activity {

	private static final String TAG = "MycollectActivity";
	/** 我的收藏的返回按钮 */
	private Button backButton;
	/** 存储新闻信息的list */
	private List<HashMap<String, Object>> collectnewlist = new ArrayList<HashMap<String, Object>>();
	private MycollectDbManger mycollectManger;
	/** 收藏的列表 */
	private ListView listView;
	private NewListAdapter adapter;
	/** 新闻列表获取基本地址 */
	private String url = Urls.NewsListUrl;
	/** 新闻ID */
	private String newsId;
	/** 新闻的url */
	private String detialUrl;
	/** 新闻标题 */
	private String newsTitle;
	/** 从这个Activity跳到详细新闻界面 */
	private String comefrom = "MycollectActivity";
	private String detailBaseurl = Urls.NewsDetailUrl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycollect);
		mycollectManger = new MycollectDbManger(this);
		// 更新Ui
		uiUpdate();
		// 为返回按钮绑定监听器
		backButton = (Button) findViewById(R.id.mycollect_back_bn);
		backButton.setOnClickListener(new ButtonOnClickListener());
		listView = (ListView) findViewById(R.id.mycollect_ListView);
		adapter = new NewListAdapter(this, collectnewlist,
				R.layout.newslist_item, new String[] { "id", "imageUrl",
						"title", "time", "short_content" }, new int[] {
						R.id.newslist_item_news_picture,
						R.id.newslist_item_title, R.id.newslist_item_time,
						R.id.newslist_item_digest });
		listView.setAdapter(adapter);
		// 收藏列表的监听事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id1) {
				// 将新闻的详细URL、新闻的标题（这个标题用于在收藏列表中查找信息，
				// 用户在收藏界面查看新闻详细信息后，再取消收藏时有用）
				HashMap<String, Object> hashMap = collectnewlist.get(position);
				newsId = hashMap.get("id").toString();
				detialUrl = detailBaseurl + "?id=" + newsId;
				newsTitle = hashMap.get("title").toString();
				Log.i(TAG, "收藏界面的信息传送成功");
				Intent intent = new Intent(MycollectActivity.this,
						NewsDetailsActivity.class);
				// 将数据传到NewsDetailsActivity
				intent.putExtra("newsTitle", newsTitle);
				intent.putExtra("detialUrl", detialUrl);
				intent.putExtra("comefrom", comefrom);
				startActivity(intent);
			}
		});
		// 监听长按事件 长按时弹出选择菜单
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View view,
					ContextMenuInfo arg2) {

				menu.setHeaderTitle("请选择：");
				menu.add(0, 0, 0, "删除此收藏新闻").setOnMenuItemClickListener(
						mOnMenuItemClickListener);
				menu.add(0, 1, 0, "返回我的收藏").setOnMenuItemClickListener(
						mOnMenuItemClickListener);

			}

		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView ada, View view,

			int index, long longIndex) {

				listView.showContextMenu();
				HashMap<String, Object> hashMap = collectnewlist.get(index);
				newsTitle = hashMap.get("title").toString();
				return true;

			}
		});
	}

	// 长按弹出菜单选项的点击选择事件监听
	final OnMenuItemClickListener mOnMenuItemClickListener = new OnMenuItemClickListener() {
		public boolean onMenuItemClick(MenuItem item) {
			switch (item.getItemId()) {
			case 0: {

				// 将新闻从收藏的列表删除
				mycollectManger.open();
				mycollectManger.deleteCollect(newsTitle);
				Log.i(TAG, newsTitle);
				mycollectManger.close();
				Toast.makeText(MycollectActivity.this, "成功删除此收藏新闻",
						Toast.LENGTH_SHORT).show();
			}
				break;
			case 1:
				break;
			}
			return false;
		}

	};

	/** 从收藏列表拿出所有数据 */
	private void uiUpdate() {
		mycollectManger.open();
		getDataFormDb();
		mycollectManger.close();
	}

	private void getDataFormDb() {
		Cursor cursor = mycollectManger.getdiaries();
		startManagingCursor(cursor);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("id", cursor.getString(cursor
						.getColumnIndex(Constants.NewsListTable.NEWS_ID)));
				dataMap.put("title", cursor.getString(cursor
						.getColumnIndex(Constants.NewsListTable.NEWS_TITLE)));
				dataMap.put("short_content", cursor.getString(cursor
						.getColumnIndex(Constants.NewsListTable.NEWS_SUMMRY)));
				dataMap.put("time", cursor.getLong(cursor
						.getColumnIndex(Constants.NewsListTable.NEWS_TIME)));
				dataMap.put(
						"imageUrl",
						cursor.getString(cursor
								.getColumnIndex(Constants.NewsListTable.NEWS_IMAGE_URL)));
				collectnewlist.add(dataMap);
			} while (cursor.moveToNext());
			System.out.println(collectnewlist.size());
		}
	}

	private class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.mycollect_back_bn) {
				goBack();
			}

		}
	}

	/** 退出按钮 */
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				goBack();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	/** 返回上一个界面 */
	private void goBack() {
		Intent intent = new Intent(MycollectActivity.this,
				NewsMainActivity.class);
		startActivity(intent);
		finish();
	}
}