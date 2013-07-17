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
 * @see 意见反馈界面
 * 
 * */
public class SuggestionFeedbackActivity extends Activity {
	/** 返回按钮 */
	private static ImageButton callBackBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggestion_feedback);
		callBackBt = (ImageButton) findViewById(R.id.suggestion_feedback_ibt_back);

		callBackBt.setOnClickListener(new OnClickListener() {
			/** 返回至设置界面 */
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
		Intent intent = new Intent(SuggestionFeedbackActivity.this,
				SystemSetActivity.class);
		startActivity(intent);
		finish();
	}
}
