package com.clothing.widget;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.HashMap;

public class BitmapCache {

	public static HashMap<Uri, Bitmap> cache = new HashMap<Uri, Bitmap>();
	
	public static Bitmap getBitmap(Uri key, int width, int height) {
		Bitmap bitmap = null;
		if(cache.containsKey(key)) {
			bitmap = cache.get(key);
			if (bitmap.getWidth() == width && bitmap.getHeight() == height) {
				return bitmap;
			}
		}
		return bitmap;
	}

	public static void putBitmap(Uri key, Bitmap bitmap) {
		cache.put(key, bitmap);
	}

	public static void clearCache() {
		cache.clear();
	}
}
