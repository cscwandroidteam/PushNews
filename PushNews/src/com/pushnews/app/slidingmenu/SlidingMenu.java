package com.pushnews.app.slidingmenu;

import com.example.slidingmenu.view.FixListViewLinearLayout.OnScrollListener;
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
 * �����������棬������������Menu;����һ���м��
 * 
 * @author LZB
 * 
 * */

public class SlidingMenu extends RelativeLayout implements OnGestureListener,
		OnTouchListener {

	/** �հ׽��� */
	private SlidingView slidingView;
	/** ����� */
	private ViewGroup leftView;
	/** �ҽ��� */
	private ViewGroup rightView;
	/** �м���� */
	private ViewGroup centerView;
	/** ��߲˵��Ŀ�� */
	private int leftViewWidth;
	/** �ұ߲˵��Ŀ�� */
	private int rightViewWidth;
	/** ��Ļ�Ŀ�� */
	private int screenWidth;
	/** ��Ļ���ƶ����� */
	private final static int SPEED = 30;
	/** ��Ļ��ǰ������ */
	private int currentSpeed;
	/** ���� */
	private GestureDetector mGestureDetector;
	/** ��ǰ������view */
	private View currentOnTouchView;
	/** �ж��Ƿ����ڹ��� */
	private boolean isScrolling;
	/** ���������X */
	private int mScrollX;
	/** �´εĽ���״̬ */
	private SlidingState currentUIState;
	private boolean hasMeasured;

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);

		slidingView = new SlidingView(context, attrs);
		addView(slidingView);
		/* �����ǻ��һ��ͼ�㣬Ȼ������ͼ���н��л滭�Լ��Ľ���,����˵����ԭ�����õ��Ǹ��հ׽��濪ʼ�����Լ��Ľ����� */
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
	 * ��ʼ��
	 * 
	 * @author LZB
	 * 
	 */
	protected void initView() {
		mGestureDetector = new GestureDetector(this);
		leftView.setOnTouchListener(this);
		rightView.setOnTouchListener(this);
		centerView.setOnTouchListener(this);
		// ��Ӽ����¼�ͨ����ȡ��View�ĸ�����ÿ����View���г�ʼ��
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
	 * ʵ��OnScrollListener�ӿ�
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
		if (null != currentOnTouchView && (currentOnTouchView instanceof ListView)) {
			ListView listView = (ListView) currentOnTouchView;

			int position = listView.pointToPosition((int) e.getX(), (int) e.getY());
			if (position != ListView.INVALID_POSITION) {
				View child = listView.getChildAt(position - listView.getFirstVisiblePosition());
				if (child != null)
					child.setPressed(true);

			}
		}
		mScrollX = 0;
		isScrolling = false;
		// ��֮��Ϊtrue���Żᴫ�ݸ�onSingleTapUp,��Ȼ�¼��������´���.
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
		// ִ�л���.
		doScrolling(distanceX);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// ��������ƽʱ�ؼ��İ����¼�
		if (currentOnTouchView.isClickable()) {
			currentOnTouchView.performClick();
		}
		onclickViewById(currentOnTouchView.getId(), e);
		return false;
	}

	// =================================================================================================
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		currentOnTouchView = v;// ��¼����Ŀؼ�

		// �ɿ���ʱ��Ҫ�жϣ������������Ļλ��������ȥ��
		if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
			asynMove();
			return false;
		}
		// Log.d(TAG, "onTouch view=" + v.getClass().getSimpleName());
		return mGestureDetector.onTouchEvent(event);
		
	}
	
	/**
	 * ������ߵ�view
	 * 
	 * @author LZB
	 * @param view
	 * @param leftWidth
	 */
	public void setLeftView(ViewGroup view, int leftWidth) {
		leftView = view;
		/**public LayoutParams(int w, int h) {
            super(w, h);
        }
		 * ���ý���Ŀ�Ⱥ͸߶�
		 * ��ȣ�leftWidth���߶ȣ�FILL_PARENT��
		 * */
		RelativeLayout.LayoutParams lp = new LayoutParams(leftWidth, LayoutParams.FILL_PARENT);
		addView(view, lp);
	}
	
	/**
	 * �����ұߵ�view
	 * 
	 * @author LZB
	 * @param view
	 * @param rightWidth
	 */
	public void setRightView(ViewGroup view, int rightWidth) {
		rightView = view;
		RelativeLayout.LayoutParams lp = new LayoutParams(rightWidth, LayoutParams.FILL_PARENT);
		addView(view, lp);
	}
	
	/**
	 * �����м��view
	 * 
	 * @author LZB
	 * @param view
	 */
	public void setCenterView(ViewGroup view) {
		centerView = view;
		RelativeLayout.LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		addView(view, lp);
	}
	
	/**
	 * ��ȡ��ǰ�����״̬
	 * 
	 * @author LZB
	 * @return
	 */
	public SlidingState getCurrentUIState() {
		return currentUIState;
	}
	
	/**
	 * ���ݽ���״̬��ʾ����
	 * 
	 * @author carlos carlosk@163.com
	 * @version ����ʱ�䣺2013-4-16 ����11:12:33
	 * @param nextState
	 */
	public void showViewState(SlidingState nextState) {
		if (currentUIState == nextState) {
			return;
		}
		RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
		int centerMargin = centerLayoutParams.leftMargin;
		switch (nextState) {
		case SHOWLEFT:
			// �����ұ߲˵���һ������3��������2
			currentSpeed = SPEED;
			break;
		case SHOWCENTER:
			// �����ǰ��״̬����߻��ұߵĲ˵���ʾ�Ļ�������ʾΪ��ҳ
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
	 * ������ҳ��leftMargin�������Ҳ˵���margin
	 * �ڵ�����margin��Խ������¹���
	 * @author LZB
	 * 
	 * @param centerLayoutParams
	 */
	private void setMenusLatyouParamsWithCenterLayoutParam(LayoutParams centerLayoutParams) {
		
		RelativeLayout.LayoutParams rightLayoutParams = (RelativeLayout.LayoutParams) rightView.getLayoutParams();
		RelativeLayout.LayoutParams leftLayoutParams = (RelativeLayout.LayoutParams) leftView.getLayoutParams();
		// ����������߲˵���rightMargin����Ȼ�����
		rightLayoutParams.leftMargin = screenWidth + centerLayoutParams.leftMargin;
		rightLayoutParams.rightMargin = -rightLayoutParams.leftMargin;

		// �����������������ʱ����ҳ�����
		centerLayoutParams.rightMargin = -centerLayoutParams.leftMargin;
		
		leftLayoutParams.leftMargin = -leftViewWidth + centerLayoutParams.leftMargin;
		leftLayoutParams.rightMargin = -rightViewWidth - leftLayoutParams.leftMargin;

		centerView.setLayoutParams(centerLayoutParams);
		rightView.setLayoutParams(rightLayoutParams);
		leftView.setLayoutParams(leftLayoutParams);

	}
	
	/**
	 * �첽�ƶ�
	 * 
	 * @author LZB
	 */
	protected void asynMove() {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
		// ����ȥ
		int centerMargin = layoutParams.leftMargin;
		// ���Ŀǰ����߳��ֻ��ұ߳��֣���ֻ�� ��ҳ���֣��򲻼���ִ��
		if (centerMargin == -rightViewWidth || centerMargin == 0 || centerMargin == leftViewWidth) {
			return;
		}
		// ���� ��߲˵���һ������1��
		// �����ұ߲˵���һ������3��������2
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
	 * listview ���ڻ���ʱִ��.
	 */
	void doScrolling(float distanceX) {
		if (!isScrolling) {
			isScrolling = true;
		}
		mScrollX += distanceX;// distanceX:����Ϊ������Ϊ��

		RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
		centerLayoutParams.leftMargin -= mScrollX;
		
		if (centerLayoutParams.leftMargin >= leftViewWidth) {
			isScrolling = false;// �Ϲ�ͷ�˲���Ҫ��ִ��AsynMove��
			// ����ϵ�ͷ��
			currentUIState = SlidingState.SHOWLEFT;
			centerLayoutParams.leftMargin = leftViewWidth;

		} else if (centerLayoutParams.leftMargin <= -rightViewWidth) {
			// �Ϲ�ͷ�˲���Ҫ��ִ��AsynMove��
			currentUIState = SlidingState.SHOWRIGHT;
			isScrolling = false;
			
			centerLayoutParams.leftMargin = -leftViewWidth;
		}

		setMenusLatyouParamsWithCenterLayoutParam(centerLayoutParams);
	}
	
	/**
	 * �����ĵ���¼���������
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
	 * �������첽�ƶ�����һ��������
	 * @author LZB
	 * 
	 * */
	class AsynMove extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			//  ����time
			int moveX = 0;
			RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();

			int currentCenterMarginLeft = centerLayoutParams.leftMargin;
			switch (currentUIState) {
			// ���������
			case SHOWLEFT:
				moveX = leftViewWidth - currentCenterMarginLeft ;
				break;
			case SHOWCENTER:
				moveX = Math.abs(currentCenterMarginLeft);
				break;
			case SHOWRIGHT:
				moveX = currentCenterMarginLeft +rightViewWidth;
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
			RelativeLayout.LayoutParams centerLayoutParams = (RelativeLayout.LayoutParams) centerView.getLayoutParams();

			// ���ƶ�
			// ����ֻ������center��leftMargin�������ĸ��ݸ�ֵ����
			switch (currentUIState) {
			case SHOWLEFT:
				// �������
				centerLayoutParams.leftMargin = Math.min(centerLayoutParams.leftMargin + currentSpeed, leftViewWidth);

				break;
			case SHOWCENTER:
				if (currentSpeed > 0) {
					centerLayoutParams.leftMargin = Math.min(centerLayoutParams.leftMargin + currentSpeed, 0);
				} else {
					centerLayoutParams.leftMargin = Math.max(centerLayoutParams.leftMargin + currentSpeed, 0);
				}

				break;
			case SHOWRIGHT:
				centerLayoutParams.leftMargin = Math.max(centerLayoutParams.leftMargin + currentSpeed, -rightViewWidth);

				break;

			default:
				break;
			}
			setMenusLatyouParamsWithCenterLayoutParam(centerLayoutParams);

		}

	}
}
