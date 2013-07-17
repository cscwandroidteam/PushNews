package com.pushnews.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.pushnews.app.R;
import com.pushnews.app.adapter.ChannelMangerListAdapter;

/**
 * @author LZB 
 * @see 频道管理界面，主要是对频道的选择和删除功能
 * 
 * */

public class ChannelMangerActivity extends Activity {
	/**频道选择列表*/
	private ListView channelListView;
	/**返回按钮*/
	private static ImageButton callBackBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel_manger_listselector);
		channelListView = (ListView) findViewById(R.id.cmls_list_v);
		/** 这个是自定义的适配器 */
		ChannelMangerListAdapter channelMangerListAdapter = new ChannelMangerListAdapter(
				this);
		channelListView.setAdapter(channelMangerListAdapter);

		callBackBt = (ImageButton) findViewById(R.id.cmls_ibt_back);
		/**返回至主界面*/
		callBackBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				goBack();
			}
		});

	}
	
	/**退出按钮*/
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
	
	/**返回上一个界面*/
	private void goBack(){
		Intent intent = new Intent(ChannelMangerActivity.this,
				NewsMainActivity.class);
		startActivity(intent);
		finish();
	}


}

