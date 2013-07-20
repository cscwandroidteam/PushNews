package com.pushnews.app.https;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;


public class AsyncImageLoader {
	/**
	 * @param args
	 */
	// 为了加快速度，在内存中开启缓存（主要应用于重复图片较多时，或者同一个图片要多次被访问，比如在ListView时来回滚动）
	public static Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	// 固定五个线程来执行任务
	private static ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	//
	
	
	private final static Handler handler=new Handler();
	/**
    *
    * @param imageUrl     图像url地址
    * @param callback     回调接口
    * @return     返回内存中缓存的图像，第一次加载返回null
    */
	public static Bitmap loadBitmap(final String imageUrl,  final ImageCallBack callback){
		//如果缓存过就从缓存中取出数据
		 if (imageCache.containsKey(imageUrl)) {
	            SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
	            if (softReference.get() != null) {
	                return softReference.get();
	            }
	        }
		 //缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
		 executorService.submit(new Runnable() {
				@Override
				public void run() {
					try {		
						
						final Bitmap Image = getHttpBitmap(imageUrl);
						
						imageCache.put(imageUrl, new SoftReference<Bitmap>(Image));						
						handler.post(new Runnable() {							
							@Override
							public void run() {								
								callback.imageLoaded(Image);
							}
						});
						
					} catch (Exception e) {
						 throw new RuntimeException(e);
					}
				}
			});
		return null;
			
		}
	
	/**
	 * 获取网落图片资源函数
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
