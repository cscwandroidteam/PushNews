package com.pushnews.app.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 对左右两边的view进行自定义的listvew设计
 * @author LZB
 * */

public class FixListViewLinearLayout extends LinearLayout {

	/* 自定义手势 变量 */
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;
	/* 判断是否该左右滑动 */
	private boolean isLock = true;

	/* 用来判断是不是在执行当前touch事件 */
	private boolean te;
	/*定义一个接口*/
	private OnScrollListener onScrollListener;
	
	public OnScrollListener getOnScrollListener() {
		return onScrollListener;
	}

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	public FixListViewLinearLayout(Context context) {
		super(context);
	}

	public FixListViewLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		/* 自定义手势 */
		mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {

			@Override
			public boolean onDown(MotionEvent e) {
				/*按下时就可以移动界面*/
				isLock = true;
				return super.onDown(e);
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				if (!isLock) {
					onScrollListener.doScroll(distanceX);
				}
				// 垂直大于水平
				if (Math.abs(distanceY) > Math.abs(distanceX)) {
					return false;
				} else {
					return true;
				}
			}
		});
	}

	// ==================================================================================================
	/***
	 * dispatchTouchEvent(MotionEvent ev) 事件分发，主要是对当前事件的获取。
	 * 
	 * onInterceptTouchEvent(MotionEvent ev) 事件拦截处理，主要目的不是让这个事件继续传递下去，拦截在当前界面。
	 * （要明白机制，如果返回ture的话，那就是进行拦截，处理自己的ontouch. 返回false的话，那么就会向下传递...）
	 * onTouchEvent(MotionEvent event) 事件处理，在上面拦截后，就在这个函数中处理。
	 * 
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		te = mGestureDetector.onTouchEvent(ev);// 获取手势返回值.
		/***
		 * 松开时记得处理缩回...
		 */
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			onScrollListener.doLoosen();
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		super.onInterceptTouchEvent(ev);
		return te;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		isLock = false;
		return super.onTouchEvent(event);
	}

	// ===============================================================================================
	/**
	 * 定义一个接口用来实现滑动和回收
	 * */
	public interface OnScrollListener {
		void doScroll(float distanceX);// 滑动...

		void doLoosen();// 手指松开后执行...
	}

}
