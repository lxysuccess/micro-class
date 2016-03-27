package yinzhi.micro_client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class LxyCommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	private int mLayoutId;

	public LxyCommonAdapter(Context context, List<T> datas,  int layoutId) {
		this.mContext = context;
		this.mDatas = datas;
		this.mLayoutId = layoutId;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LxyViewHolder holder = LxyViewHolder.get(mContext, convertView, parent, mLayoutId, position);

		convert(holder, getItem(position));
		return holder.getConvertView();
	}

	public abstract void convert(LxyViewHolder holder, T t);

}
