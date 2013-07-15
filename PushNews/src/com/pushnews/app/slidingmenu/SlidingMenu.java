package com.pushnews.app.slidingmenu;

import com.pushnews.app.slidingmenu.FixListViewLinearLayout.OnScrollListener;
import android.view.View.OnTouchListener;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 这个是整体界面，包含左右两个Menu;还有一个中间的
 * 
 * @author LZB
 * 
 * */

public class SlidingMenu extends RelativeLayout implements OnGestureListener,
		OnTouchListener {

	/** 空白界面 */
	private SlidingView slidingView;
	/** 左界面 */
	private ViewGroup leftView;
	/** 右界面 */
	private ViewGroup rightView;
	/** 中间界面 */
	private ViewGroup centerView;
	/** 左边菜单的宽度 */
	private int leftViewWidth;
	/** 右边菜单的宽度 */
	private int rightViewWidth;
	/** 屏幕的宽度 */
	private int screenWidth;
	/** 屏幕的移动速率 */
	private final static int SPEED = 30;
	/** 屏幕当前的速率 */
	private int currentSpeed;
	/** 手势 */
	private GestureDetector mGestureDetector;
	/** 当前触摸的view */
	private View currentOnTouchView;
	/** 判断是否正在滚动 */
	private boolean isScrolling;
	/** 横向滚动的X */
	private int mScrollX;
	/** 下次的界面状态 */
	private SlidingState currentUIState;
	private boolean hasMeasured;

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);

		slidingView = new SlidingView(context, attrs);
		addView(slidingView);
		/* 这里是获得一个图层，然后在这图层中进行绘画自己的界面,就是说在我原来设置的那个空白界面开始构造自己的界面了 */
		ViewTreeObserver viewTreeObserver = slidingView.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					screenWidth = centerView.getWidth();
					rightViewWidth = rightView.getWidth();
					leftViewWidth = leftView.getWidth();

					RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView
							.getLayoutParams();
					centerLayoutParams.leftMargin = 0;
					setMenusLatyouParamsWithCenterLayoutParam(centerLayoutParams);
					hasMeasured = true;
					currentUIState = SlidingState.SHOWCENTER;

					initView();
				}
				return true;
			}
		});
	}

	// =================================================================================================
	/**
	 * 初始化
	 * 
	 * @author LZB
	 * 
	 */
	protected void initView() {
		mGestureDetector = new GestureDetector(this);
		leftView.setOnTouchListener(this);
		rightView.setOnTouchListener(this);
		centerView.setOnTouchListener(this);
		// 添加监听事件通过获取子View的个数对每个子View进行初始化
		int leftViewCount = leftView.getChildCount();
		for (int i = 0; i < leftViewCount; i++) {
			View eachChildView = leftView.getChildAt(i);
			if (eachChildView instanceof FixListViewLinearLayout) {
				((FixListViewLinearLayout) eachChildView)
						.setOnScrollListener(onScrollListener);
			}
		}
		int rightViewCount = rightView.getChildCount();
		for (int i = 0; i < rightViewCount; i++) {
			View eachChildView = rightView.getChildAt(i);
			if (eachChildView instanceof FixListViewLinearLayout) {
				((FixListViewLinearLayout) eachChildView)
						.setOnScrollListener(onScrollListener);
			}
		}
		int centerViewCount = (centerView).getChildCount();
		for (int i = 0; i < centerViewCount; i++) {
			View eachChildView = (centerView).getChildAt(i);
			if (eachChildView instanceof FixListViewLinearLayout) {
				((FixListViewLinearLayout) eachChildView)
						.setOnScrollListener(onScrollListener);
			}
		}
	}

	/**
	 * 实现OnScrollListener接口
	 * */
	private OnScrollListener onScrollListener = new OnScrollListener() {

		@Override
		public void doScroll(float distanceX) {
			doScrolling(distanceX);
		}

		@Override
		public void doLoosen() {
			asynMove();
		}
	};

	// =====================================================================================================
	@Override
	public boolean onDown(MotionEvent e) {
		if (null != currentOnTouchView
				&& (currentOnTouchView instanceof ListView)) {
			ListView listView = (ListView) currentOnTouchView;

			int position = listView.pointToPosition((int) e.getX(),
					(int) e.getY());
			if (position != ListView.INVALID_POSITION) {
				View child = listView.getChildAt(position
						- listView.getFirstVisiblePosition());
				if (child != null)
					child.setPressed(true);

			}
		}
		mScrollX = 0;
		isScrolling = false;
		// 将之改为true，才会传递给onSingleTapUp,不然事件不会向下传递.
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// 执行滑动.
		doScrolling(distanceX);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// 在这里做平时控件的按键事件
		if (currentOnTouchView.isClickable()) {
			currentOnTouchView.performClick();
		}
		onclickViewById(currentOnTouchView.getId(), e);
		return false;
	}

	// =================================================================================================
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		currentOnTouchView = v;// 记录点击的控件

		// 松开的时候要判断，如果不到半屏幕位子则缩回去，
		if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
			asynMove();
			return false;
		}
		// Log.d(TAG, "onTouch view=" + v.getClass().getSimpleName());
		return mGestureDetector.onTouchEvent(event);

	}

	/**
	 * 设置左边的view
	 * 
	 * @author LZB
	 * @param view
	 * @param leftWidth
	 */
	public void setLeftView(ViewGroup view, int leftWidth) {
		leftView = view;
		/**
		 * public LayoutParams(int w, int h) { super(w, h); } 设置界面的宽度和高度
		 * 宽度：leftWidth。高度：FILL_PARENT；
		 * */
		RelativeLayout.LayoutParams lp = new LayoutParams(leftWidth,
				LayoutParams.FILL_PARENT);
		addView(view, lp);
	}

	/**
	 * 设置右边的view
	 * 
	 * @author LZB
	 * @param view
	 * @param rightWidth
	 */
	public void setRightView(ViewGroup view, int rightWidth) {
		rightView = view;
		RelativeLayout.LayoutParams lp = new LayoutParams(rightWidth,
				LayoutParams.FILL_PARENT);
		addView(view, lp);
	}

	/**
	 * 设置中间的view
	 * 
	 * @author LZB
	 * @param view
	 */
	public void setCenterView(ViewGroup view) {
		centerView = view;
		RelativeLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		addView(view, lp);
	}

	/**
	 * 获取当前界面的状态
	 * 
	 * @author LZB
	 * @return
	 */
	public SlidingState getCurrentUIState() {
		return currentUIState;
	}

	/**
	 * 根据界面状态显示界面
	 * 
	 * @author carlos carlosk@163.com
	 * @version 创建时间：2013-4-16 上午11:12:33
	 * @param nextState
	 */
	public void showViewState(SlidingState nextState) {
		if (currentUIState == nextState) {
			return;
		}
		RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView
				.getLayoutParams();
		int centerMargin = centerLayoutParams.leftMargin;
		switch (nextState) {
		case SHOWLEFT:
			// 大于右边菜单的一半则是3，其他是2
			currentSpeed = SPEED;
			break;
		case SHOWCENTER:
			// 如果当前的状态是左边或右边的菜单显示的话，则显示为首页
			if (centerMargin == -rightViewWidth) {
				currentSpeed = SPEED;
			} else if (centerMargin == leftViewWidth) {
				currentSpeed = -SPEED;
			}

			break;
		case SHOWRIGHT:
			currentSpeed = -SPEED;
			break;

		default:
			break;
		}
		currentUIState = nextState;
		new AsynMove().execute();
	}

	/**
	 * 根据首页的leftMargin调整左右菜单的margin 在调整了margin后对界面重新构造
	 * 
	 * @author LZB
	 * 
	 * @param centerLayoutParams
	 */
	private void setMenusLatyouParamsWithCenterLayoutParam(
			LayoutParams centerLayoutParams) {

		RelativeLayout.LayoutParams rightLayoutParams = (RelativeLayout.LayoutParams) rightView
				.getLayoutParams();
		RelativeLayout.LayoutParams leftLayoutParams = (RelativeLayout.LayoutParams) leftView
				.getLayoutParams();
		// 重新设置左边菜单的rightMargin，不然会变形
		rightLayoutParams.leftMargin = screenWidth
				+ centerLayoutParams.leftMargin;
		rightLayoutParams.rightMargin = -rightLayoutParams.leftMargin;

		// 不加这个，往右拉的时候首页会变形
		centerLayoutParams.rightMargin = -centerLayoutParams.leftMargin;

		leftLayoutParams.leftMargin = -leftViewWidth
				+ centerLayoutParams.leftMargin;
		leftLayoutParams.rightMargin = -rightViewWidth
				- leftLayoutParams.leftMargin;

		centerView.setLayoutParams(centerLayoutParams);
		rightView.setLayoutParams(rightLayoutParams);
		leftView.setLayoutParams(leftLayoutParams);

	}

	/**
	 * 异步移动
	 * 
	 * @author LZB
	 */
	protected void asynMove() {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) centerView
				.getLayoutParams();
		// 缩回去
		int centerMargin = layoutParams.leftMargin;
		// 如果目前是左边出现或右边出现，或只是 首页出现，则不继续执行
		if (centerMargin == -rightViewWidth || centerMargin == 0
				|| centerMargin == leftViewWidth) {
			return;
		}
		// 大于 左边菜单的一半则是1，
		// 大于右边菜单的一半则是3，其他是2
		int seed = 0;
		if (centerMargin < -rightViewWidth / 2) {
			currentUIState = SlidingState.SHOWRIGHT;
			seed = -SPEED;
		} else if (centerMargin > leftViewWidth / 2) {
			currentUIState = SlidingState.SHOWLEFT;
			seed = SPEED;
		} else {
			seed = centerMargin > 0 ? -SPEED : SPEED;
			currentUIState = SlidingState.SHOWCENTER;
		}
		currentSpeed = seed;
		new AsynMove().execute();
	}

	/***
	 * listview 正在滑动时执行.
	 */
	void doScrolling(float distanceX) {
		if (!isScrolling) {
			isScrolling = true;
		}
		mScrollX += distanceX;// distanceX:向左为正，右为负

		RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView
				.getLayoutParams();
		centerLayoutParams.leftMargin -= mScrollX;

		if (centerLayoutParams.leftMargin >= leftViewWidth) {
			isScrolling = false;// 拖过头了不需要再执行AsynMove了
			// 左边拖到头了
			currentUIState = SlidingState.SHOWLEFT;
			centerLayoutParams.leftMargin = leftViewWidth;

		} else if (centerLayoutParams.leftMargin <= -rightViewWidth) {
			// 拖过头了不需要再执行AsynMove了
			currentUIState = SlidingState.SHOWRIGHT;
			isScrolling = false;

			centerLayoutParams.leftMargin = -leftViewWidth;
		}

		setMenusLatyouParamsWithCenterLayoutParam(centerLayoutParams);
	}

	/**
	 * 基本的点击事件放在这里
	 * 
	 * @author LZB
	 * @param id
	 * @param e
	 */
	private void onclickViewById(int id, MotionEvent e) {

		if (centerView.getId() == id) {
			showViewState(SlidingState.SHOWCENTER);
		}
	}

	/**
	 * 这里是异步移动建立一个多任务
	 * 
	 * @author LZB
	 * 
	 * */
	class AsynMove extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// 计算time
			int moveX = 0;
			RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView
					.getLayoutParams();

			int currentCenterMarginLeft = centerLayoutParams.leftMargin;
			switch (currentUIState) {
			// 如果是往左
			case SHOWLEFT:
				moveX = leftViewWidth - currentCenterMarginLeft;
				break;
			case SHOWCENTER:
				moveX = Math.abs(currentCenterMarginLeft);
				break;
			case SHOWRIGHT:
				moveX = currentCenterMarginLeft + rightViewWidth;
			default:
				break;
			}

			int times = moveX % SPEED > 0 ? moveX / SPEED + 1 : moveX / SPEED;
			// getCurrentUIState();
			for (int i = 0; i < times; i++) {
				publishProgress(0);
				// publishProgress(values)
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;

		}

		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView
					.getLayoutParams();

			// 右移动
			// 这里只是设置center的leftMargin，其他的根据该值调整
			switch (currentUIState) {
			case SHOWLEFT:
				// 往左边移
				centerLayoutParams.leftMargin = Math.min(
						centerLayoutParams.leftMargin + currentSpeed,
						leftViewWidth);

				break;
			case SHOWCENTER:
				if (currentSpeed > 0) {
					centerLayoutParams.leftMargin = Math.min(
							centerLayoutParams.leftMargin + currentSpeed, 0);
				} else {
					centerLayoutParams.leftMargin = Math.max(
							centerLayoutParams.leftMargin + currentSpeed, 0);
				}

				break;
			case SHOWRIGHT:
				centerLayoutParams.leftMargin = Math.max(
						centerLayoutParams.leftMargin + currentSpeed,
						-rightViewWidth);

				break;

			default:
				break;
			}
			setMenusLatyouParamsWithCenterLayoutParam(centerLayoutParams);

		}

	}
}
