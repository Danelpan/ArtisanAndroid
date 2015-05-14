package com.artisan.android.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.util.Base64;
import android.view.View;

public final class BitmapUtility {
	private BitmapUtility() {
	}

	public static final Drawable resizeDrawable(Context context, int resId, int w,int h) {
		Bitmap source = resizeBitmap(context, resId, w, h);
		return new BitmapDrawable(context.getResources(), source);
	}

	public static final Bitmap resizeBitmap(Context context, int resId, int w, int h) {
		Bitmap source = decodeFromResource(context.getResources(), resId);
		return ThumbnailUtils.extractThumbnail(source, w, h);
	}

	public static final Bitmap merge(Bitmap bmp1, Bitmap bmp2) {
		if (null == bmp1 || bmp1.isRecycled() || null == bmp2 || bmp2.isRecycled())
			return null;
		Paint paint_comm = new Paint(Paint.ANTI_ALIAS_FLAG);
		Bitmap bmOverlay = Bitmap.createBitmap(bmp2.getWidth(),bmp2.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmOverlay);

		float x = (bmp2.getWidth() - bmp1.getWidth()) / 2f;
		float y = (bmp2.getHeight() - bmp1.getHeight()) / 2f;

		x = x > 0 ? x : 0;
		y = y > 0 ? y : 0;

		canvas.drawBitmap(bmp1, x, y, paint_comm);

		canvas.drawBitmap(bmp2, 0, 0, paint_comm);
		return bmOverlay;
	}

	public static final int bitmapSize(Bitmap bitmap) {
		if (bitmap == null) {
			return 0;
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	public static final InputStream bitmap2Stream(Bitmap bitmap) {
		return bitmap2Stream(bitmap, CompressFormat.JPEG, 100);
	}

	public static final InputStream bitmap2Stream(Bitmap bitmap,CompressFormat format, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(format, quality, baos);
		InputStream stream = new ByteArrayInputStream(baos.toByteArray());
		return stream;
	}

	public static final Bitmap view2Bitmap(View view) {
		return view2Bitmap(view, false);
	}
	
	public static final Bitmap view2Bitmap(View view,boolean cache) {
		view.setDrawingCacheEnabled(cache);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	public static final String base64Bitmap(Bitmap bitmap) {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			byte[] imgBytes = out.toByteArray();
			return Base64.encodeToString(imgBytes, Base64.DEFAULT);
		} catch (Exception e) {
			LogUtility.d(e);
		} finally {
			StreamUtility.closeStream(out);
		}
		return "";
	}

	public static final String base64Bitmap(File file) {
		return base64Bitmap(BitmapFactory.decodeFile(file.getPath()));
	}

	public static final void bitmap2File(Bitmap bitmap, File file) {
		bitmap2File(bitmap, file, CompressFormat.JPEG, 100);
	}
	
	public static final void bitmap2File(Bitmap bitmap, File file,CompressFormat format, int quality) {
		if (bitmap == null) {
			throw new NullPointerException("source bitmap is null...");
		}
		if (file == null) {
			throw new NullPointerException("targe file is null...");
		}
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			LogUtility.d(e);
		}
		if (outputStream == null) {
			return;
		}
		bitmap.compress(format, quality, outputStream);
		try {
			outputStream.flush();
		} catch (IOException e) {
			LogUtility.d(e);
		} finally {
			StreamUtility.closeStream(outputStream);
		}
	}

	public static final Bitmap decodeFromBytes(byte[] bs,int reqWidth, int reqHeight) {
		if (null == bs) {
			return null;
		}

		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		Bitmap bitmap = null;
		decodeOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bs, 0, bs.length, decodeOptions);
		int actualWidth = decodeOptions.outWidth;
		int actualHeight = decodeOptions.outHeight;
		int desiredWidth = getResizedDimension(reqWidth, reqHeight,actualWidth, actualHeight);
		int desiredHeight = getResizedDimension(reqHeight, reqWidth,actualHeight, actualWidth);
		decodeOptions.inJustDecodeBounds = false;

		decodeOptions.inSampleSize = inSampleSize(decodeOptions,desiredWidth, desiredHeight);
		Bitmap tempBitmap = BitmapFactory.decodeByteArray(bs, 0, bs.length,decodeOptions);

		if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
			bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth,desiredHeight, true);
			tempBitmap.recycle();
		} else {
			bitmap = tempBitmap;
		}
		return bitmap;
	}

	public static final Bitmap decodeFromResource(Resources res,int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inSampleSize = inSampleSize(options, reqWidth,reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static final Bitmap decodeFromResource(Resources res, int resId) {
		return BitmapFactory.decodeResource(res, resId);
	}

	public static final Bitmap decodeFromDescriptor(FileDescriptor descriptor) {
		return decodeFromDescriptor(descriptor, 0, 0);
	}
	
	public static final Bitmap decodeFromDescriptor(FileDescriptor descriptor, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(descriptor, null, options);
		options.inSampleSize = inSampleSize(options, reqWidth,reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeFileDescriptor(descriptor, null,options);
		} catch (OutOfMemoryError error) {
			options.inSampleSize = options.inSampleSize * 2;
			bitmap = BitmapFactory.decodeFileDescriptor(descriptor, null,options);
		}
		return bitmap;
	}

	public static final Bitmap decodeFromFile(String path) {
		return decodeFromFile(path, 0, 0);
	}

	public static final Bitmap decodeFromFile(String path,int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = inSampleSize(options, reqWidth,reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static final int inSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

			final float totalPixels = width * height;
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		LogUtility.d("inSampleSize---->" + inSampleSize);
		return inSampleSize;
	}

	public final static int rotationAngle(String path) {
		int degree = 0;
		ExifInterface exifInterface = null;
		try {
			exifInterface = new ExifInterface(path);
		} catch (IOException e) {
			LogUtility.d(e);
		}
		if (exifInterface != null) {
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				return degree;
			}
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		}
		return degree;
	}

	public final static Bitmap rotation(Bitmap source,int degrees) {
		if (null == source || source.isRecycled()) {
			return null;
		}

		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		Bitmap rotation = Bitmap.createBitmap(source, 0, 0, source.getWidth(),source.getHeight(), matrix, true);
		if (rotation != null) {
			source.recycle();
		}
		return rotation;
	}
	
	public static final int[] obtainBitmapSize(String pathName) {
		int[] size = new int[2];
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		size[0] = options.outWidth;
		size[1] = options.outHeight;
		return size;
	}

	static final int getResizedDimension(int maxPrimary, int maxSecondary,int actualPrimary, int actualSecondary) {
		if (maxPrimary == 0 && maxSecondary == 0) {
			return actualPrimary;
		}

		if (maxPrimary == 0) {
			double ratio = (double) maxSecondary / (double) actualSecondary;
			return (int) (actualPrimary * ratio);
		}

		if (maxSecondary == 0) {
			return maxPrimary;
		}

		double ratio = (double) actualSecondary / (double) actualPrimary;
		int resized = maxPrimary;
		if (resized * ratio > maxSecondary) {
			resized = (int) (maxSecondary / ratio);
		}
		return resized;
	}
}
