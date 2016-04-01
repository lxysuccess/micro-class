package yinzhi.micro_client.adapter;


import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class LxyViewHolder {
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private Context mContext;

	public LxyViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.setmPosition(position);
		this.mViews = new SparseArray<View>();
		this.mContext = context;

		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
		;
	}

	public static LxyViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {

		if (convertView == null) {
			return new LxyViewHolder(context, parent, layoutId, position);
		} else {
			LxyViewHolder holder = (LxyViewHolder) convertView.getTag();
			holder.setmPosition(position);
			return holder;
		}
	}

	/**
	 * 通过viewid获取控件
	 *
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 设置TextView的值
	 *
	 * @param viewId
	 * @param text
	 * @return
	 */
	public LxyViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	/**
	 * 设置ImageView的resource
	 *
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public LxyViewHolder setImageResource(int viewId, int resId) {
		ImageView view = getView(viewId);
		view.setImageResource(resId);
		return this;
	}

	/**
	 * 设置ImageView的Bitmap
	 *
	 * @param viewId
	 * @param bitmap
	 * @return
	 */
	public LxyViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bitmap);
		return this;
	}

	/**
	 * 加载网络图片，设置ImageButton的URL
	 *
	 * @param viewId
	 * @param url
	 * @return
	 */
	public LxyViewHolder setImageButtonUrl(int viewId, String url) {
		ImageButton view = getView(viewId);
		// Imageloader.getInstance().loadImage(view,url);
		BitmapUtils bitmapUtils = new BitmapUtils(mContext);
		// 加载网络图片
		bitmapUtils.display(view, url);
		return this;
	}

	/**
	 * 加载网络图片，设置ImageView的URL
	 *
	 * @param viewId
	 * @param url
	 * @return
	 */
	public LxyViewHolder setImageViewUrl(int viewId, String url) {
		ImageView view = getView(viewId);
		BitmapUtils bitmapUtils = new BitmapUtils(mContext);
		// 加载网络图片
		bitmapUtils.display(view, url);
		return this;
	}

	public int getmPosition() {
		return mPosition;
	}

	public void setmPosition(int mPosition) {
		this.mPosition = mPosition;
	}
}
