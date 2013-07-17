package com.pushnews.app.ui;

import com.pushnews.app.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author LZB
 * 
 * @see 设置界面
 * 
 *      主要功能有：字体设置，消息推送，清理缓存，意见反馈，使用帮助
 * 
 * */

public class SystemSetActivity extends Activity {
	/** 设置内容列表 */
	private static ListView setListView;
	/** 测试数据 */
	private static String[] test = { "字体设置", "消息推送", "清理缓存", "意见反馈", "使用帮助" };
	/** 返回按钮 */
	private static ImageButton callBackBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_set_list);
		setListView = (ListView) findViewById(R.id.sys_setlist_lv);

		setListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.system_set_list_item, R.id.sys_setList_item_tv, test));

		setListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					showScriptChosDia();
					break;
				case 1:
					pushMessageDia();
					break;
				case 2:
					showMemoryDia();
					break;
				case 3:
					toFeedbackActivity();
					break;
				case 4:
					toUseIfoActivity();
					break;
				default:
					break;
				}

			}
		});

		/** 返回至主界面 */
		callBackBt = (ImageButton) findViewById(R.id.sys_setlist_ibt_back);
		callBackBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goBack();

			}
		});

	}

	/** 设置字体大小 */
	private void showScriptChosDia() {
		final String[] mList = { "小", "中", "大" };
		AlertDialog.Builder scriptDia = new AlertDialog.Builder(
				SystemSetActivity.this);
		scriptDia.setTitle("字体大小选择");
		/** 使用单选列表 */
		scriptDia.setSingleChoiceItems(mList, 0,
				new DialogInterface.OnClickListener() {
					/**
					 * 
					 * @params which 表示选择第几个
					 * */
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						System.out.println("选择第几个" + which);
					}
				});

		scriptDia.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		scriptDia.create().show();
	}

	/** 消息推送的确定 */
	private void pushMessageDia() {
		final String[] mList = { "是", "否" };
		AlertDialog.Builder pMessageDia = new AlertDialog.Builder(
				SystemSetActivity.this);
		pMessageDia.setTitle("请选择是否消息推送");
		/** 设置单选列表 */
		pMessageDia.setSingleChoiceItems(mList, 0,
				new DialogInterface.OnClickListener() {
					/**
					 * @params which 表示选择第几个
					 * */
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						System.out.println("选择第几个" + which);
					}
				});

		pMessageDia.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		pMessageDia.create().show();
	}

	/** 设置清理缓存功能 */
	private void showMemoryDia() {
		// AlertDialog.Builder normalDialog=new
		// AlertDialog.Builder(getApplicationContext());
		AlertDialog.Builder memoryDia = new AlertDialog.Builder(
				SystemSetActivity.this);
		memoryDia.setTitle("清理缓存");
		memoryDia.setMessage("是否清理缓存");
		/** 确定响应事件 */
		memoryDia.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		memoryDia.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		memoryDia.create().show();
	}

	/** 跳转至意见反馈界面 */
	private void toFeedbackActivity() {
		Intent intent = new Intent(SystemSetActivity.this,
				SuggestionFeedbackActivity.class);
		startActivity(intent);
		finish();

	}

	/** 跳转至使用帮助界面 */
	private void toUseIfoActivity() {
		Intent intent = new Intent(SystemSetActivity.this,
				UsingHelpActivity.class);
		startActivity(intent);
		finish();

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
		Intent intent = new Intent(SystemSetActivity.this,
				NewsMainActivity.class);
		startActivity(intent);
		finish();
	}

}
