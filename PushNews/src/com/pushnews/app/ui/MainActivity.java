package com.pushnews.app.ui;

import com.pushnews.app.R;
import com.pushnews.app.slidingmenu.SlidingMenu;
import com.pushnews.app.slidingmenu.SlidingState;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	private SlidingMenu slidingMenu;
	private String lf_title[] = { "广州", "娱乐", "体育", "汽车", "科技", "头条", "紧急新闻",
			"财经", "趣图", "国内", "国际", "常见问题", "退出当前帐号" };
	private String lr_title[] = { "设置", "消息推送", "我的收藏", "改变字体大小", "改变亮度",
			"常见问题", "退出当前帐号" };
	private String lc_title[] = { "广东工业大学重大新闻", "广东工业大学重大新闻", "广东工业大学重大新闻",
			"广东工业大学重大新闻", "广东工业大学重大新闻", "广东工业大学重大新闻", "广东工业大学重大新闻",
			"广东工业大学重大新闻", "广东工业大学重大新闻", "广东工业大学重大新闻", "广东工业大学重大新闻",
			"广东工业大学重大新闻", "广东工业大学重大新闻", "广东工业大学重大新闻", "广东工业大学重大新闻" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 设置全屏 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sliding_menu);
		slidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		/**
		 * getLayoutInflater().inflate(int resource, ViewGroup root);
		 * 这个是对自定义的xml布局文件进行解释，然后获取后android能够对其变为一个layout;
		 */
		ViewGroup leftView = (ViewGroup) getLayoutInflater().inflate(
				R.layout.sl_left_v, null);
		ViewGroup rightView = (ViewGroup) getLayoutInflater().inflate(
				R.layout.sl_right_v, null);
		ViewGroup centerView = (ViewGroup) getLayoutInflater().inflate(
				R.layout.sl_center_v, null);
		/* 对这个自定义的coSlidingMenu添加中间画面的组件 */
		slidingMenu.setCenterView(centerView);
		/* 这里获取自定义左右界面的宽度 */
		/* 这里获取自定义左右界面的宽度 */
		int leftWidth = (int) getResources()
				.getDimension(R.dimen.leftViewWidth);
		int rightWidth = (int) getResources().getDimension(
				R.dimen.rightViewWidth);
		/*
		 * 对这个自定义的SlidingMenu添加左边和右边画面的组件 主要是确定他们的宽度
		 */
		slidingMenu.setLeftView(leftView, leftWidth);
		slidingMenu.setRightView(rightView, rightWidth);
		/* 设置右边ImageView的响应事件 */
		View ivRight = centerView.findViewById(R.id.ivRight);
		ivRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * 获取当前界面的状态 总共有三种:SHOWRIGHT,SHOWCENTER,SHOWLEFT
				 * 主要是跟SHOWCENTER相对比
				 */
				if (slidingMenu.getCurrentUIState() == SlidingState.SHOWRIGHT) {
					slidingMenu.showViewState(SlidingState.SHOWCENTER);
				} else {
					slidingMenu.showViewState(SlidingState.SHOWRIGHT);
				}
			}
		});

		/* 同上 */
		View ivLeft = centerView.findViewById(R.id.ivLeft);

		ivLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (slidingMenu.getCurrentUIState() == SlidingState.SHOWLEFT) {
					slidingMenu.showViewState(SlidingState.SHOWCENTER);
				} else {
					slidingMenu.showViewState(SlidingState.SHOWLEFT);
				}
			}
		});
		/* 这里同右ImageView */
		View btnLeft = leftView.findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingMenu.showViewState(SlidingState.SHOWCENTER);

			}
		});

		/* 设置左边的listview */
		ListView lvLeft = (ListView) leftView.findViewById(R.id.lvLeft);
		lvLeft.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.tv_item, lf_title));
		lvLeft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});
		/* 设置右边的listview */
		ListView lvRight = (ListView) rightView.findViewById(R.id.lvRight);
		lvRight.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.tv_item, lr_title));
		lvRight.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});
		/* 设置中间的listview */
		ListView lvcenter = (ListView) centerView.findViewById(R.id.lvCenter);
		lvcenter.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.tv_item, lc_title));
		lvcenter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});

	}

}