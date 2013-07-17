package com.pushnews.app.ui;

import com.pushnews.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {
	/**
	 * 登录界面返回按钮
	 */
	private Button login_backButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		login_backButton = (Button) findViewById(R.id.login_back_bn);
		login_backButton.setOnClickListener(new ButtonOnClickListener());
	}

	private class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.login_back_bn) {
				Intent intent = new Intent(LoginActivity.this,
						NewsMainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}

		}
	}
}