package com.pushnews.app.ui;

import com.pushnews.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NewsDetailsActivity extends Activity
{   
	/**
	 * 新闻详细内容的返回按钮
	 */
	private Button newsdetails_backButton;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsdetails);
		newsdetails_backButton = (Button) findViewById(R.id.newsdetails_back_bn);
		newsdetails_backButton.setOnClickListener(new ButtonOnClickListener());
	}
	private class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.newsdetails_back_bn) {
				Intent intent = new Intent(NewsDetailsActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}

		}
	}
}