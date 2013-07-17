package com.pushnews.app.ui;

import com.pushnews.app.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * @author LZB
 * 
 * @see 使用帮助界面
 * 
 * 
 * */
public class UsingHelpActivity extends Activity {

	private static ImageButton callBackBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.using_help);

		callBackBt = (ImageButton) findViewById(R.id.using_help_ibt_back);

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
				goBack();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	/** 返回上一个界面 */
	private void goBack() {
		Intent intent = new Intent(UsingHelpActivity.this,
				SystemSetActivity.class);
		startActivity(intent);
		finish();
	}
}
