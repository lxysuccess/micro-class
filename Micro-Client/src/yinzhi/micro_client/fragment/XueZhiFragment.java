package yinzhi.micro_client.fragment;

import java.util.ArrayList;
import java.util.List;

import yinzhi.micro_client.bean.XueZhiMessageInfo;
import yinzhi.micro_client.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class XueZhiFragment extends Fragment {

	public static XueZhiFragment fragment;
	
	@ViewInject(R.id.xuezhi_listview)
	public ListView listView;
	
	private List<XueZhiMessageInfo> infos = new ArrayList<XueZhiMessageInfo>();

	public synchronized static XueZhiFragment newInstance() {
		if (fragment == null) {
			fragment = new XueZhiFragment();
		}
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
			XueZhiMessageInfo info = new XueZhiMessageInfo();
			info.setTitle("未发布");
			info.setDate("2015-11-12");
			info.setContent("基础会计：模拟测试1");
			info.setClassInfo("会计系-2015");
			infos.add(info);
			
			XueZhiMessageInfo info1 = new XueZhiMessageInfo();
			info1.setTitle("已完成");
			info1.setDate("2015-11-9");
			info1.setContent("成本会计：在线测试");
			info1.setClassInfo("会计系-2015");
			infos.add(info1);
			
			XueZhiMessageInfo info2 = new XueZhiMessageInfo();
			info2.setTitle("未完成");
			info2.setDate("2015-11-9");
			info2.setContent("财务管理：模拟测试");
			info2.setClassInfo("会计系-2015");
			infos.add(info2);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_xuezhi, null);
		ViewUtils.inject(this,rootView);
		
		listView.setAdapter(new MyCourseAdapter());
		return rootView;
	}
	
	class MyCourseAdapter extends BaseAdapter {

		public int getCount() {
			return infos.size();
		}

		public XueZhiMessageInfo getItem(int position) {
			return infos.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.xuezhi_list_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			XueZhiMessageInfo item = getItem(position);
			LogUtils.i("getView Position" + position);
			holder.title.setText(item.getTitle().toString());
			holder.date.setText(item.getDate().toString());
			holder.content.setText(item.getContent().toString());
			holder.classInfo.setText(item.getClassInfo().toString());
			return convertView;
		}

	}

	public static class ViewHolder {
		TextView title;
		TextView date;
		TextView content;
		TextView classInfo;
		
		public ViewHolder(View view) {
			title = (TextView) view.findViewById(R.id.xuezhi_item_title);
			date = (TextView) view.findViewById(R.id.xuezhi_item_date);
			content = (TextView) view.findViewById(R.id.xuezhi_item_content);
			classInfo = (TextView) view.findViewById(R.id.xuezhi_item_class); 
		}
	}
}
