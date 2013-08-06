package com.pushnews.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.pushnews.app.R;
import com.pushnews.app.adapter.ChannelMangerListAdapter;
import com.pushnews.app.cofig.Constants;
import com.pushnews.app.db.ChannelDbManger;
import com.pushnews.app.model.Channel;

/**
 * @author LZB
 * @see 频道管理界面，主要是对频道的选择和删除功能
 * 
 * */

public class ChannelMangerActivity extends Activity {
	/** 频道选择列表 */
	private ListView channelListView;
	/** 返回按钮 */
	private static Button callBackBt;
	private List<Channel> cList = new ArrayList<Channel>();
	private ChannelDbManger channelDbManger;
	private String[] test = { "广州", "娱乐", "体育", "汽车", "科技", "头条", "紧急新闻", "财经",
			"趣图", "国内", "国际" };
	private int[] mark = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel_manger_listselector);
		channelListView = (ListView) findViewById(R.id.cmls_list_v);
		channelDbManger = new ChannelDbManger(this);
		channelDbManger.open();
		setData();
		getData();
		/** 这个是自定义的适配器 */
		ChannelMangerListAdapter channelMangerListAdapter = new ChannelMangerListAdapter(
				this, cList);
		channelListView.setAdapter(channelMangerListAdapter);

		callBackBt = (Button) findViewById(R.id.cmls_ibt_back);
		/** 返回至主界面 */
		callBackBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goBack();
			}
		});
	}

	/** 退出按钮 */
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {

				finish();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	/** 返回上一个界面 */
	private void goBack() {
		Intent intent = new Intent(ChannelMangerActivity.this,
				NewsMainActivity.class);
		startActivity(intent);
		finish();
	}

	private void setData() {
		if (channelDbManger.getdiaries().getCount() == 0) {
			for (int i = 0; i < test.length; i++) {
				Channel channel = new Channel(test[i], mark[i]);
				channelDbManger.addChannel(channel);
			}
		}
		

	}

	private void getData() {
		Cursor cursor = channelDbManger.getdiaries();
		startManagingCursor(cursor);
		if (cursor.moveToFirst()) {

			do {
				Channel channel = new Channel();
				channel.setnewsType(cursor.getString(cursor
						.getColumnIndex(Constants.ChannelTable.NEWS_TYPE)));
				channel.setMark(cursor.getInt(cursor
						.getColumnIndex(Constants.ChannelTable.MARK)));
				cList.add(channel);
			} while (cursor.moveToNext());
		}
		channelDbManger.close();
	}

}
