package com.pushnews.app.https;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.pushnews.app.entity.BitMapTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 异步加载图片类
 * 
 * @author LZB
 * 
 * */

public class AsyncImageLoader {
	private final String TAG = "ImageLoader";
	private Context mContext;
	// 线程轮询的控制变量
	private boolean isLoop = true;
	// 图片缓存集合
	private HashMap<String, SoftReference<Bitmap>> mHashMap_caches;
	// 下载任务队列
	public static ArrayList<ImageLoadTask> maArrayList_taskQueue;
	// 用于回调callback中的方法，更新界面
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.i(TAG, "启动");
			ImageLoadTask _task = (ImageLoadTask) msg.obj;
			Log.i(TAG, "" + _task);
			// 调用callback中的imageloaded方法，通知界面更新数据
			_task.callback.imageloaded(_task.path, _task.bitmap);
		};
	};
	// 创建工作线程，用于轮询任务队列从而下载图片
	private Thread mThread = new Thread() {
		public void run() {
			while (isLoop) {
				// 当任务队列中有任务时开始执行下载
				while (maArrayList_taskQueue.size() > 0) {
					try {
						Log.i(TAG, "开始任务");
						// 从任务队列中移除任务时会返回任务对象即得到任务下载对象
						ImageLoadTask task = maArrayList_taskQueue.remove(0);
						// 从网络端获取图片
						task.bitmap = getHttpBitmap(task.path);
						// 如果图片下载成功就向内存缓存和文件中放置缓存信息，以便之后从双缓存中读取图片信息
						if (task.bitmap != null) {
							// 向集合缓存中添加缓存
							mHashMap_caches.put(task.path,
									new SoftReference<Bitmap>(task.bitmap));
							// 向文件中添加缓存信息
							// 获取文件存储路径
							File dir = mContext
									.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
							// 如果文件路径不存在 ,则创建该路径
							if (!dir.exists()) {
								dir.mkdirs();
							}
							// 创建图片存储路径
							File file = new File(dir, task.newsId + ".png");
							// 向该文件路径中存储图片
							Log.i(TAG, file.getAbsolutePath());
							BitMapTools.saveBitmap(file.getAbsolutePath(),
									task.bitmap);
							// 下载完成后发送消息回主线程
							Message msg = Message.obtain();
							msg.obj = task;
							mHandler.sendMessage(msg);
							Log.i(TAG, msg.toString());
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.e(TAG, e.getLocalizedMessage() + "======");
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.e(TAG, e.getLocalizedMessage());
						e.printStackTrace();
					}
					// 当前线程处于等待状态
					synchronized (this) {
						try {
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							Log.e(TAG, e.getLocalizedMessage());
							e.printStackTrace();
						}
					}
				}
			}
		};
	};

	// 构造方法，
	public AsyncImageLoader(Context context) {
		this.mContext = context;
		mHashMap_caches = new HashMap<String, SoftReference<Bitmap>>();
		maArrayList_taskQueue = new ArrayList<AsyncImageLoader.ImageLoadTask>();
		mThread.start();
	}

	/**
	 * 图片下载任务类
	 * 
	 */
	private class ImageLoadTask {
		/** 图片下载地址 */
		String path;
		/** 下载的图片 */
		Bitmap bitmap;
		/** 新闻ID */
		String newsId;
		/** 回调接口 */
		Callback callback;
	}

	/**
	 * 回调接口。在调用loadimage方法时，需要传入回调接口的实现类对象
	 * 
	 */
	public interface Callback {
		void imageloaded(String path, Bitmap bitmap);
	}

	// 结束工作线程，终止线程轮询
	public void quit() {
		isLoop = false;
	}

	/**
	 * 利用图片路径下载图片 利用内存缓存和文件缓存双缓存机制优化下载
	 * 
	 * @param path
	 *            图片下载地址
	 * @param callback
	 *            回调接口
	 * @param newsId
	 *            新闻ID
	 * @return
	 */
	public Bitmap imageLoad(String path, String newsId, Callback callback) {
		Bitmap bitmap = null;
		// 如果内存缓存中存在该路径，则从内存中直接获取该图片
		if (mHashMap_caches.containsKey(path)) {
			bitmap = mHashMap_caches.get(path).get();
			// 如果缓存中的图片已经被释放，则从该缓存中移除图片路径
			if (bitmap == null) {
				mHashMap_caches.remove(path);
			} else {
				return bitmap;
			}
		}
		// 如果缓存中未得到缓存需要的图片，则从文件中读取该图片
		// 获取本文件的图片类的文件存储路径
		File dir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		Log.i(TAG, "dir==============" + dir);
		// 创建要获取的图片路径
		File file = new File(dir, newsId + ".png");
		// 从文件中读取指定路径的图片
		bitmap = BitMapTools.getBitMap(file.getAbsolutePath());
		// 如果文件中存在该图片，则直接从文件中获取图片
		if (bitmap != null) {
			return bitmap;
		}
		// 如果两个缓存中都未获取图片，则直接从服务器端下载图片
		// 创建下载人任务
		ImageLoadTask task = new ImageLoadTask();
		// 设置下载路径
		task.path = path;
		// 设置下载的id
		task.newsId = newsId;
		// 设置下载任务的callback对象
		task.callback = callback;
		// 将下载任务添加到任务队列 进入任务轮询状态
		maArrayList_taskQueue.add(task);
		// 唤醒线程，开始下载
		synchronized (mThread) {
			mThread.notify();
		}
		return null;
	}

	/**
	 * 获取网落图片资源函数
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			conn.setRequestMethod("GET");
			// 连接设置获得数据流
			conn.setDoInput(true);
			/*
			 * 不使用缓存 conn.setUseCaches(false); // 这句可有可无，没有影响 conn.connect();
			 */
			// 得到数据流
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode===" + responseCode);
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;

	}

}
