package com.pushnews.app.ui;

import org.json.JSONException;
import org.json.JSONObject;
import com.pushnews.app.R;
import com.pushnews.app.db.MycollectDbManger;
import com.pushnews.app.https.HttpUtils;
import com.pushnews.app.model.Mycollect;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class NewsDetailsActivity extends Activity {

	private static final String TAG = "NewsDetailsActivity";
	/** 新闻详细内容的返回按钮 */
	private Button backButton;
	/** 用来存储新闻详细信息 */
	private String content;
	/** 用来显示新闻详细信息的视图 */
	private LayoutInflater NewsBodyInflater;
	private ViewFlipper NewsBodyFlipper;
	private MycollectDbManger mycollectDbManger;
	/** 新闻ID */
	private String newsId;
	/** 新闻类型 */
//	private String newsType;
	/** 新闻标题 */
	private String newsTitle;
	/** 新闻概要 */
	private String newsSummary;
	/** 新闻来源 */
//	private String newsFrom;
	/** 新闻时间 */
	private long newsTime;
	/** 新闻图片地址 */
	private String imageUrl;
	/** 新闻详细内容地址 */
	private String detialUrl;
	/** 0：表示不是头条，1：表示头条*/
//	private int topLine;
	/** 记录从哪个Activity跳到NewsDetailsActivity详细新闻界面的 String
	 *  主要作用是当用户点击返回按钮时，就跳到上一个界面
	 *  上一个界面有可能是NewsMainActivity，也可能是MycollectActivity
	 */
	private String comefrom;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsdetails);
		NewsDetailsOnClickListener newsDetailsOnClickListener = new NewsDetailsOnClickListener();
		// 返回,上一条，下一条按钮并绑定监听器
		backButton = (Button) findViewById(R.id.newsdetails_back_bn);
		backButton.setOnClickListener(newsDetailsOnClickListener);
		Button lastButton = (Button) findViewById(R.id.newsdetails_last_btn);
		lastButton.setOnClickListener(newsDetailsOnClickListener);
		Button nextButton = (Button) findViewById(R.id.newsdetails_next_btn);
		nextButton.setOnClickListener(newsDetailsOnClickListener);
		Button collectButton = (Button) findViewById(R.id.newsdetails_collect_btn);
		collectButton.setOnClickListener(newsDetailsOnClickListener);

		// 获取 从NewsMainActivity传过来的数据
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 设置标题栏名称
		String newstpye = bundle.getString("newsType");
		TextView title_tv = (TextView) findViewById(R.id.newsdetails_titlebar_title);
		title_tv.setText(newstpye);
		// 获取新闻的所有信息
		detialUrl = bundle.getString("detialUrl");
		newsTitle = bundle.getString("newsTitle");
		newsTime = bundle.getInt("newsTime");
		newsSummary = bundle.getString("newsSummary");
		imageUrl = bundle.getString("imageUrl");
		newsId = bundle.getString("newsId");
/*		topLine = bundle.getInt("topLine");
		newsType = bundle.getString("newsType");
		newsFrom = bundle.getString("newsFrom");*/
		comefrom = bundle.getString("comefrom");
		Log.i(TAG, "接收信息成功");
		mycollectDbManger = new MycollectDbManger(this);
		Log.i(TAG, comefrom);
		// 动态创建新闻视图，并赋值
		NewsBodyInflater = getLayoutInflater();
		inflateView();
	}

	/**
	 * 处理NewsDetailsTitleBar点击事件
	 */
	class NewsDetailsOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 上一条新闻
			case R.id.newsdetails_last_btn:
				break;
			// 下一条新闻
			case R.id.newsdetails_next_btn:
				break;
			// 返回按钮
			case R.id.newsdetails_back_bn:
				goBack();
				break;
			// 收藏按钮
			case R.id.newsdetails_collect_btn:
				collect();
				break;
			}
		}
	}
    /**
     * 收藏事件的处理
     */
	private void collect() {
		//打开数据库
		mycollectDbManger.open();
		//根据newsTitle从收藏列表查找是否有这条新闻
		Cursor cursor = mycollectDbManger.getcollect(newsTitle);
		startManagingCursor(cursor);
		cursor.moveToFirst();
		//如果在收藏列表没有找到这条新闻，说明这条新闻没有被收藏，此时用户想收藏此新闻。
		if (cursor.getCount() == 0) {
			Mycollect item = new Mycollect(newsId, newsTitle,
					newsSummary, newsTime, imageUrl);
			//将新闻add进收藏的列表
			mycollectDbManger.addCollect(item);
			mycollectDbManger.close();
			Toast.makeText(NewsDetailsActivity.this, "收藏成功", Toast.LENGTH_SHORT)
					.show();
		} 
		//如果在收藏列表找到这条新闻，说明这条新闻已被收藏，此时用户想取消收藏次新闻。
		else {
			//将新闻从收藏的列表删除
			mycollectDbManger.deleteCollect(newsTitle);
			mycollectDbManger.close();
			Toast.makeText(NewsDetailsActivity.this, "取消收藏成功",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 初始化界面
	 * 
	 * @author linyongan
	 */
	public void inflateView() {
		NewsBodyInflater = getLayoutInflater();
		// 动态创建新闻视图，并赋值
		View newsBodyLayout = NewsBodyInflater
				.inflate(R.layout.news_body, null);
		// 获取点击新闻基本信息
		GetNew();
		// 解析html
		WebView newsdetails = (WebView) newsBodyLayout
				.findViewById(R.id.news_body_newsdetails);
		// newsdetails.setText(Html.fromHtml(content));
		newsdetails.getSettings().setDefaultTextEncodingName("UTF-8");
		newsdetails.loadData(content, "text/html", "UTF-8");
		//设置可以两点触控，实现更改字体大小
		newsdetails.getSettings().setBuiltInZoomControls(true);
		// 把新闻视图添加到Flipper中
		NewsBodyFlipper = (ViewFlipper) findViewById(R.id.newsdetails_ViewFlipper);
		NewsBodyFlipper.addView(newsBodyLayout);
	}

	// 解析新闻详细内容
	private void GetNew() {
		try {
			//解析新闻详细信息的json
			String json = HttpUtils.postByHttpURLConnection(detialUrl, null);
			JSONObject jsonObject = new JSONObject(json);
			content = jsonObject.getString("content");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// private void ShowNext() {
	// // 判断是否是最后一篇新闻
	// if (newsId < newsData.size() - 1) {
	// // 设置下一屏动画
	// newsId++;
	// inflateView();
	// // 显示下一屏
	// NewsBodyFlipper.showNext();
	// } else {
	// Toast.makeText(this, "没有下一条新闻", Toast.LENGTH_SHORT).show();
	// }
	// }
	//
	// private void ShowPrevious() {
	// if (newsId > 0) {
	// newsId--;
	// inflateView();
	// // 设置下一屏动画
	// NewsBodyFlipper.showPrevious();
	// } else {
	// Toast.makeText(this, "没有上一条新闻", Toast.LENGTH_SHORT).show();
	// }
	// }

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
		String a = new String("NewsMainActivity");
		String b = new String("MycollectActivity");
		if (a.equals(comefrom)) {
			Intent intent = new Intent(NewsDetailsActivity.this,
					NewsMainActivity.class);
			startActivity(intent);
		}
		if (b.equals(comefrom)) {
			Intent intent = new Intent(NewsDetailsActivity.this,
					MycollectActivity.class);
			startActivity(intent);
		}
	}

}
