package com.sigseg.android.view;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class InputStreamScene extends Scene {
//	private static final String TAG="InputStreamScene";
	
	private final BitmapFactory.Options options = new BitmapFactory.Options();
	private BitmapRegionDecoder decoder;
	private Bitmap sampleBitmap;
	/** What is the downsample size for the sample image? */
	private final int downShift = 3;
	
	public InputStreamScene(){
		options.inPreferredConfig = Bitmap.Config.RGB_565;
	}
	
	
	/**
	 * Set the Scene to the named asset
	 * @param context
	 * @param assetName
	 * @throws IOException
	 */
	public void setInputStream(InputStream inputStream) throws IOException {
		BitmapFactory.Options tmpOptions = new BitmapFactory.Options();

		this.decoder = BitmapRegionDecoder.newInstance(inputStream, false);

		// Grab the bounds for the scene dimensions
		tmpOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(inputStream, null, tmpOptions);
		width = tmpOptions.outWidth;
		height = tmpOptions.outHeight;
		
		// Create the sample image
		tmpOptions.inJustDecodeBounds = false;
		tmpOptions.inSampleSize = (1<<downShift);
		sampleBitmap = BitmapFactory.decodeStream(inputStream, null, tmpOptions);
		
		initialize();
	}


	@Override
	protected Bitmap fillCache(Rect origin) {
		Bitmap bitmap = decoder.decodeRegion( origin, options );
		return bitmap;
	}


	@Override
	protected void drawSampleIntoBitmapAtPoint(Bitmap bitmap, Point point) {
		Canvas c = new Canvas(bitmap);
		int left   = (point.x>>downShift);
		int top    = (point.y>>downShift);
		int right  = left + (c.getWidth()>>downShift);
		int bottom = top + (c.getHeight()>>downShift);
		Rect srcRect = new Rect( left, top, right, bottom );
		Rect identity= new Rect(0,0,c.getWidth(),c.getHeight());
		c.drawBitmap(
			sampleBitmap,
			srcRect,
			identity,
			null
			);

	}
}
