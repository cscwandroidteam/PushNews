package com.pushnews.app.entity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.pushnews.app.https.HttpUtils;
import com.pushnews.app.https.ImageCallBack;

public class SetImageBitmap implements ImageCallBack {
	
	
	private static Bitmap image;
	
	public SetImageBitmap() {
		// TODO Auto-generated constructor stub
				
	}
	
	public static Bitmap setImage(String url) {
		// TODO Auto-generated constructor stub
		Bitmap bitmapcahe;
		bitmapcahe = HttpUtils.AsyncLoadPic(url, new SetImageBitmap());
		if (bitmapcahe!=null) {
			return bitmapcahe;
		}
		return image;
	}

	@Override
	public void imageLoaded(Bitmap bitmap) {
		// TODO Auto-generated method stub
		this.image = bitmap;
	}

}
