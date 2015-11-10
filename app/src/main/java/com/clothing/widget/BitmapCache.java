package com.clothing.widget;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import java.util.HashMap;

public class BitmapCache {
	
	public static HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();
	
	public static Bitmap getBitmap(String key, int width, int height) {
		Bitmap bitmap;
		if(cache.containsKey(key)) {
			bitmap = cache.get(key);
			if(bitmap.getWidth() == width && bitmap.getHeight() == height) {
				return bitmap;
			}
		}
		
		bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		cache.put(key, bitmap);
		return bitmap;
	}
	
	public static void clearCache() {
		cache.clear();
	}
}
