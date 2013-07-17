package com.pushnews.app.ui;

import com.pushnews.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MycollectActivity extends Activity {
	/**
	 * 我的收藏的返回按钮
	 */
	private Button cllect_backButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycollect);
		cllect_backButton = (Button) findViewById(R.id.mycollect_back_bn);
		cllect_backButton.setOnClickListener(new ButtonOnClickListener());
	}

	private class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.mycollect_back_bn) {
				Intent intent = new Intent(MycollectActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}

		}
	}
}