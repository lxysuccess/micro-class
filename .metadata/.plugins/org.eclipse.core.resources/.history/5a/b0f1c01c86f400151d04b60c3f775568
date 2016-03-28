package yinzhi.micro_client.fragment;

import java.util.ArrayList;
import java.util.List;

import yinzhi.micro_client.activity.ExerciseActivity;
import yinzhi.micro_client.bean.CourseInfo;
import yinzhi.micro_client.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;

public class MyCourseFragment extends Fragment {

	public static MyCourseFragment fragment;
	private ListView myCourseListView;

	// 所有课程页课程列表
	private List<CourseInfo> mCourseInfos = new ArrayList<CourseInfo>();

	// 课程图片
	public int[] imgs = { R.drawable.book1 };

	public synchronized static MyCourseFragment newInstance() {
		if (fragment == null) {
			fragment = new MyCourseFragment();
		}
		return fragment;
	}

	private void initData() {
		for (int i = 0; i < 1; i++) {
			CourseInfo tempCourse = new CourseInfo();
			tempCourse.setIconName("icon" + i);
			tempCourse.setSubTitle("这是简介" + i);
			tempCourse.setTitle("电子纳税实务：增值税中的零税率");
			tempCourse.setVisitCount(892);
			tempCourse.setVideoDuration("2015年10月20日");
			mCourseInfos.add(tempCourse);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_course, null);
		myCourseListView = (ListView) rootView.findViewById(R.id.my_course_listview);
		myCourseListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						ExerciseActivity.class);
				intent.putExtra("choice", 1);
				startActivity(intent);
				getActivity().overridePendingTransition(
						R.anim.activity_anim_left_in, 0);
			}
		});
		myCourseListView.setAdapter(new MyCourseAdapter());
		return rootView;
	}
	
	class MyCourseAdapter extends BaseAdapter {

		public int getCount() {
			return imgs.length;
		}

		public CourseInfo getItem(int position) {
			return mCourseInfos.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.course_list_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			CourseInfo item = getItem(position);
			LogUtils.i("getView Position" + position);
			holder.iv_icon.setBackgroundResource(imgs[position]);
			holder.tv_name.setText(item.getTitle().toString());
			holder.tv_video_duration
					.setText(item.getVideoDuration().toString());
			holder.tv_visit_count.setText(String.valueOf(item.getVisitCount()));
			return convertView;
		}
	}

	public static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_visit_count;
		TextView tv_video_duration;

		public ViewHolder(View view) {
			iv_icon = (ImageView) view.findViewById(R.id.iv_course_icon);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_visit_count = (TextView) view.findViewById(R.id.tv_visit_count);
			tv_video_duration = (TextView) view
					.findViewById(R.id.tv_video_duration);
		}
	}
}
