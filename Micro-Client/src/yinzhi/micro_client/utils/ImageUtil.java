package yinzhi.micro_client.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import yinzhi.micro_client.R;

public class ImageUtil {
	/**
	 * 旋转Bitmap
	 * 
	 * @param b
	 * @param rotateDegree
	 * @return
	 */
	public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
		Matrix matrix = new Matrix();
		matrix.postRotate((float) rotateDegree);
		Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
		return rotaBitmap;
	}

	public static DisplayImageOptions getDisplayOption(int round) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(false) // default
				// 设置图片在加载前是否重置、复位
				.showImageOnLoading(R.drawable.image_loading) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_empty_page) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_error_page) // 设置图片加载或解码过程中发生错误显示的图片
				.delayBeforeLoading(1000) // 下载前的延迟时间
				.cacheInMemory(true) // default 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // default 设置下载的图片是否缓存在SD卡中
				.considerExifParams(false) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default 设置图片的解码类型
				.imageScaleType(ImageScaleType.EXACTLY) // 设置图片的缩放方式
				.displayer(new RoundedBitmapDisplayer(round)) //
				// 还可以设置圆角图片new
				// RoundedBitmapDisplayer(20)
				.handler(new Handler()) // default
				.build();
		return options;
	}
}
