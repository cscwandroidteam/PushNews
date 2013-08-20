package com.pushnews.app.adapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pushnews.app.https.AsyncImageLoader;
import com.pushnews.app.https.AsyncImageLoader.Callback;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 图片异步加载以及newsItem的显示
 * @author LZB
 * */

public class NewListAdapter extends BaseAdapter implements Filterable {
	private static final String TAG = "NewListAdapter";
	/**对应newItem的控件ID*/
	private int[] mTo;
	/**对应newItem的控件内容*/
	private String[] mFrom;
	private ViewBinder mViewBinder;
	/**新闻列表的加载List内容*/
	private List<? extends Map<String, ?>> mData;
	
	/**新闻的ID*/
	private String news_id;
	/**异步加载类*/
	private AsyncImageLoader imageLoader;

	/**新闻Item的显示界面*/
	private int mResource;
	private int mDropDownResource;
	private LayoutInflater mInflater;

	private SimpleFilter mFilter;
	private ArrayList<Map<String, ?>> mUnfilteredData;

	/**
	 * 注意：
	 * 
	 * from的格式是第一个是参数一定要是对应新闻的id,然后再试跟控件一一对应的内容
	 * @param context
	 *            The context where the View associated with this SimpleAdapter
	 *            is running
	 * @param data
	 *            A List of Maps. Each entry in the List corresponds to one row
	 *            in the list. The Maps contain the data for each row, and
	 *            should include all the entries specified in "from"
	 * @param resource
	 *            Resource identifier of a view layout that defines the views
	 *            for this list item. The layout file should include at least
	 *            those named views defined in "to"
	 * @param from
	 *            A list of column names that will be added to the Map
	 *            associated with each item.
	 * @param to
	 *            The views that should display column in the "from" parameter.
	 *            These should all be TextViews. The first N views in this list
	 *            are given the values of the first N columns in the from
	 *            parameter.
	 *            
	 *
	 */
	public NewListAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		mData = data;
		mResource = mDropDownResource = resource;
		mFrom = from;
		mTo = to;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new AsyncImageLoader(context);
	}

	/**
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return mData.size();
	}

	/**
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return mData.get(position);
	}

	/**
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * @see android.widget.Adapter#getView(int, View, ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mResource);
	}

	private View createViewFromResource(int position, View convertView,
			ViewGroup parent, int resource) {
		View v;
		if (convertView == null) {
			v = mInflater.inflate(resource, parent, false);
		} else {
			v = convertView;
		}
		bindView(position, v);

		return v;
	}

	/**
	 * <p>
	 * Sets the layout resource to create the drop down views.
	 * </p>
	 * 
	 * @param resource
	 *            the layout resource defining the drop down views
	 * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
	 */
	public void setDropDownViewResource(int resource) {
		this.mDropDownResource = resource;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent,
				mDropDownResource);
	}

	private void bindView(int position, View view) {
		final Map dataSet = mData.get(position);
		if (dataSet == null) {
			return;
		}

		final ViewBinder binder = mViewBinder;
		final String[] from = mFrom;
		final int[] to = mTo;
		final int count = to.length;
		final Object data2 = dataSet.get(from[0]);
		this.news_id = (String) data2;

		for (int i = 0; i < count; i++) {
			final View v = view.findViewById(to[i]);
			if (v != null) {
				final Object data = dataSet.get(from[i + 1]);
				String text = data == null ? "" : data.toString();
				if (text == null) {
					text = "";
				}
				boolean bound = false;
				if (binder != null) {
					bound = binder.setViewValue(v, data, text);
				}
				if (!bound) {
					if (v instanceof Checkable) {
						if (data instanceof Boolean) {
							((Checkable) v).setChecked((Boolean) data);
						} else if (v instanceof TextView) {
									
							setViewText((TextView) v, text);
						} else {
							throw new IllegalStateException(v.getClass()
									.getName()
									+ " should be bound to a Boolean, not a "
									+ (data == null ? "<unknown type>"
											: data.getClass()));
						}
					} else if (v instanceof TextView) {
							if (data instanceof Long) {
								setViewLong((TextView)v, (Long) data);
							} else {							
								setViewText((TextView) v, text);
							}		
					} else if (v instanceof ImageView) {
						setViewImage((ImageView) v, text);
					} else {
						throw new IllegalStateException(
								v.getClass().getName()
										+ " is not a "
										+ " view that can be bounds by this SimpleAdapter");
					}
				}
			}
		}

	}

	/**
	 * Returns the {@link ViewBinder} used to bind data to views.
	 * 
	 * @return a ViewBinder or null if the binder does not exist
	 * 
	 * @see #setViewBinder(android.widget.SimpleAdapter.ViewBinder)
	 */
	public ViewBinder getViewBinder() {
		return mViewBinder;
	}

	/**
	 * Sets the binder used to bind data to views.
	 * 
	 * @param viewBinder
	 *            the binder used to bind data to views, can be null to remove
	 *            the existing binder
	 * 
	 * @see #getViewBinder()
	 */
	public void setViewBinder(ViewBinder viewBinder) {
		mViewBinder = viewBinder;
	}

	/**
	 * Called by bindView() to set the image for an ImageView but only if there
	 * is no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an ImageView.
	 * 
	 * This method is called instead of {@link #setViewImage(ImageView, String)}
	 * if the supplied data is an int or Integer.
	 * 
	 * @param v
	 *            ImageView to receive an image
	 * @param value
	 *            the value retrieved from the data set
	 * 
	 * @see #setViewImage(ImageView, String)
	 */

	public void setViewImage(ImageView v, Bitmap bitmap) {
		v.setImageBitmap(bitmap);
	}
	/**执行异步加载的函数*/
	public void setViewImage(final ImageView v, String url) {
		Bitmap bitmap = imageLoader.imageLoad(url, news_id, new Callback() {
			@Override
			public void imageloaded(String path, Bitmap bitmap) {
				v.setImageBitmap(bitmap);
				Log.i(TAG, path);
			}
		});
		v.setImageBitmap(bitmap);

	}

	/*
	 * private void setImage(final ImageView v,String url){ uiHandler = new
	 * Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) { switch (msg.what) {
	 * case Messages.TASK_SUCCESSS: v.setImageBitmap((Bitmap)msg.obj);
	 * executorService.shutdown(); Log.i(TAG, msg.obj.toString()); break; } } };
	 * uiUpdatethread = new ImageLoadHandler(mcontext, uiHandler, news_id, url);
	 * uiUpdatethread.start(); showRunable = new Runnable() {
	 * 
	 * @Override public void run() { // 給线程发送一个Message
	 * uiUpdatethread.getHandler().sendEmptyMessage( Messages.TASK_BEGIN); } };
	 * executorService.submit(showRunable); }
	 */
	public void setViewImage(ImageView v, int value) {
		v.setImageResource(value);
	}

	/**
	 * Called by bindView() to set the text for a TextView but only if there is
	 * no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an TextView.
	 * 
	 * @param v
	 *            TextView to receive text
	 * @param text
	 *            the text to be set for the TextView
	 */
	public void setViewText(TextView v, String text) {
		v.setText(text);
	}
	
	public void setViewLong(TextView v, Long data) {
		DateFormat dateFormat = DateFormat.getDateInstance();
		String newsTime = dateFormat
				.format(new Date(data).getTime());
		v.setText(newsTime);
	}

	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new SimpleFilter();
		}
		return mFilter;
	}

	/**
	 * This class can be used by external clients of SimpleAdapter to bind
	 * values to views.
	 * 
	 * You should use this class to bind values to views that are not directly
	 * supported by SimpleAdapter or to change the way binding occurs for views
	 * supported by SimpleAdapter.
	 * 
	 * @see SimpleAdapter#setViewImage(ImageView, int)
	 * @see SimpleAdapter#setViewImage(ImageView, String)
	 * @see SimpleAdapter#setViewText(TextView, String)
	 */
	public static interface ViewBinder {
		/**
		 * Binds the specified data to the specified view.
		 * 
		 * When binding is handled by this ViewBinder, this method must return
		 * true. If this method returns false, SimpleAdapter will attempts to
		 * handle the binding on its own.
		 * 
		 * @param view
		 *            the view to bind the data to
		 * @param data
		 *            the data to bind to the view
		 * @param textRepresentation
		 *            a safe String representation of the supplied data: it is
		 *            either the result of data.toString() or an empty String
		 *            but it is never null
		 * 
		 * @return true if the data was bound to the view, false otherwise
		 */
		boolean setViewValue(View view, Object data, String textRepresentation);
	}

	/**
	 * <p>
	 * An array filters constrains the content of the array adapter with a
	 * prefix. Each item that does not start with the supplied prefix is removed
	 * from the list.
	 * </p>
	 */
	private class SimpleFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mUnfilteredData == null) {
				mUnfilteredData = new ArrayList<Map<String, ?>>(mData);
			}

			if (prefix == null || prefix.length() == 0) {
				ArrayList<Map<String, ?>> list = mUnfilteredData;
				results.values = list;
				results.count = list.size();
			} else {
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<Map<String, ?>> unfilteredValues = mUnfilteredData;
				int count = unfilteredValues.size();

				ArrayList<Map<String, ?>> newValues = new ArrayList<Map<String, ?>>(
						count);

				for (int i = 0; i < count; i++) {
					Map<String, ?> h = unfilteredValues.get(i);
					if (h != null) {

						int len = mTo.length;

						for (int j = 0; j < len; j++) {
							String str = (String) h.get(mFrom[j]);

							String[] words = str.split(" ");
							int wordCount = words.length;

							for (int k = 0; k < wordCount; k++) {
								String word = words[k];

								if (word.toLowerCase().startsWith(prefixString)) {
									newValues.add(h);
									break;
								}
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// noinspection unchecked
			mData = (List<Map<String, ?>>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

}
