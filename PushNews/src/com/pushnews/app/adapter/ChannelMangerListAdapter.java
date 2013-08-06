package com.pushnews.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pushnews.app.R;
import com.pushnews.app.adapter.holder.ChannelMangerHolder;
import com.pushnews.app.cofig.Constants;
import com.pushnews.app.db.ChannelDbManger;
import com.pushnews.app.model.Channel;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @author LZB
 * @see 下面将对上述代码，做详细的解释，listView在开始绘制的时候，系统首先调用getCount（）函数，根据他的返回值得到listView的长度
 *      （这也是为什么在开始的第一张图特别的标出列表长度），然后根据这个长度，调用getView（）逐一绘制每一行。如果你的getCount（）
 *      返回值是0的话，列表将不显示同样return 1，就只显示一行。
 *      系统显示列表时，首先实例化一个适配器（这里将实例化自定义的适配器）。当手动完成适配时，必须手动映射数据，这需要重写getView（）
 *      方法。系统在绘制列表的每一行的时候将调用此方法。getView()有三个参数，position表示将显示的是第几行，
 *      covertView是从布局文件中inflate来的布局
 *      。我们用LayoutInflater的方法将定义好的xml文件提取成View实例用来显示。
 *      然后将xml文件中的各个组件实例化（简单的findViewById()方法）。这样便可以将数据对应到各个组件上了。但是按钮为了响应点击事件
 *      ，需要为它添加点击监听器，这样就能捕获点击事件
 *      。至此一个自定义的listView就完成了，现在让我们回过头从新审视这个过程。系统要绘制ListView了
 *      ，他首先获得要绘制的这个列表的长度，然后开始绘制第一行
 *      ，怎么绘制呢？调用getView()函数。在这个函数里面首先获得一个View（实际上是一个ViewGroup
 *      ），然后再实例并设置各个组件，显示之
 *      。好了，绘制完这一行了。那再绘制下一行，直到绘完为止。在实际的运行过程中会发现listView的每一行没有焦点了
 *      ，这是因为Button抢夺了listView的焦点，只要布局文件中将Button设置为没有焦点就OK了。
 * 
 * */

public class ChannelMangerListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Channel> channelList = new ArrayList<Channel>();
	private ChannelDbManger channelDbManger;
	private Channel channel;
	private String newsType;

	public ChannelMangerListAdapter(Context context, List<Channel> list) {
		this.mInflater = LayoutInflater.from(context);
		channelDbManger = new ChannelDbManger(context);		
		this.channelList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return channelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 这个函数在每次点击列表事件时都会触发一次
	 * 
	 * @param position
	 *            相对应列表的位置
	 * @param convertView
	 *            列表视图
	 * @param parent
	 *            这个最后添加在父类视图容器内
	 * 
	 * @return convertView
	 * */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ChannelMangerHolder holder = null;		
		if (convertView == null) {
			holder = new ChannelMangerHolder();
			convertView = mInflater.inflate(
					R.layout.channel_manger_listelector_item, null);
			holder.channelListTv = (TextView) convertView
					.findViewById(R.id.cmls_item_tv);
			holder.channelListCb = (CheckBox) convertView
					.findViewById(R.id.cmls_item_cb);
			convertView.setTag(holder);

		} else {
			holder = (ChannelMangerHolder) convertView.getTag();

		}
		newsType = channelList.get(position).getnewsType();
		holder.channelListTv.setText(newsType);
		if (channelList.get(position).getMark()==1) {
			holder.channelListCb.setChecked(true);
			System.out.println(position+"======1{}");
		}else{
			holder.channelListCb.setChecked(false);
			System.out.println(position+"======0{}");
		}		
		holder.channelListCb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						channelDbManger.open();
						String newstyle = channelList.get(position).getnewsType();
						if (isChecked != false) {
							channel = new Channel(newstyle, 1);
						int i =	channelDbManger.updateChannel(channel,
									Constants.ChannelTable.MARK + "=?" + " and "
											+ Constants.ChannelTable.NEWS_TYPE
											+ "=?", new String[] {"0",newstyle});
							System.out.println("1======="+position+"======"+i);
						} else {
							channel = new Channel(newstyle, 0);
							int i =	channelDbManger.updateChannel(channel,
									Constants.ChannelTable.MARK + "=?" + " and "
											+ Constants.ChannelTable.NEWS_TYPE
											+ "=?", new String[] {"1",newstyle});
							System.out.println("0======="+position+"======"+i);
						}
						channelDbManger.open();						
					}
				});
		return convertView;
	}
	
}