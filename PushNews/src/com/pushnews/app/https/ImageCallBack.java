package com.pushnews.app.https;

import android.graphics.Bitmap;

/**
 * 对外界开放的回调接口
 * 
 * */
public interface ImageCallBack {
	//注意 此方法是用来设置目标对象的图像资源
    public void imageLoaded(Bitmap bitmap);
}
