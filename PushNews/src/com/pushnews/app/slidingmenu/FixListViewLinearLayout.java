package com.pushnews.app.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * ���������ߵ�view�����Զ����listvew���
 * @author LZB
 * */

public class FixListViewLinearLayout extends LinearLayout {

	/* �Զ������� ���� */
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;
	/* �ж��Ƿ�����һ��� */
	private boolean isLock = true;

	/* �����ж��ǲ�����ִ�е�ǰtouch�¼� */
	private boolean te;
	/*����һ���ӿ�*/
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
		/* �Զ������� */
		mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {

			@Override
			public boolean onDown(MotionEvent e) {
				/*����ʱ�Ϳ����ƶ�����*/
				isLock = true;
				return super.onDown(e);
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				if (!isLock) {
					onScrollListener.doScroll(distanceX);
				}
				// ��ֱ����ˮƽ
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
	 * dispatchTouchEvent(MotionEvent ev) �¼��ַ�����Ҫ�ǶԵ�ǰ�¼��Ļ�ȡ��
	 * 
	 * onInterceptTouchEvent(MotionEvent ev) �¼����ش�����ҪĿ�Ĳ���������¼�����������ȥ�������ڵ�ǰ���档
	 * ��Ҫ���׻��ƣ��������ture�Ļ����Ǿ��ǽ������أ������Լ���ontouch. ����false�Ļ�����ô�ͻ����´���...��
	 * onTouchEvent(MotionEvent event) �¼��������������غ󣬾�����������д���
	 * 
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		te = mGestureDetector.onTouchEvent(ev);// ��ȡ���Ʒ���ֵ.
		/***
		 * �ɿ�ʱ�ǵô�������...
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
	 * ����һ���ӿ�����ʵ�ֻ����ͻ���
	 * */
	public interface OnScrollListener {
		void doScroll(float distanceX);// ����...

		void doLoosen();// ��ָ�ɿ���ִ��...
	}

}
