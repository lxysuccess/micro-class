package yinzhi.micro_client.fragment;

import java.util.ArrayList;
import java.util.List;

import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.ExerciseActivity;
import yinzhi.micro_client.activity.MainActivity;
import yinzhi.micro_client.bean.CourseInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;

public class AllCourseFragment extends Fragment {
	private ImageButton mImageButton;

	// 所有课程页课程列表
	private List<CourseInfo> mCourseInfos = new ArrayList<CourseInfo>();
	// 课程图片
	public int[] imgs = { R.drawable.book1, R.drawable.book2, R.drawable.book3,
			R.drawable.book4, R.drawable.book5,R.drawable.book6,R.drawable.book7 };
	private String courseTitle[] = {"电子报税实训","基础会计","成本会计","财务会计","财务管理","会计电算化","会计基础实训"};
	// 列表
	private ListView listView;

	private static AllCourseFragment allCourseFragment;
	
	private MainActivity activity;

	public synchronized static AllCourseFragment getInstance() {
		if (allCourseFragment == null) {
			allCourseFragment = new AllCourseFragment();
		}
		return allCourseFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//!!!!!!!!!!!!!!!!!!!!!!!!!
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		for (int i = 0; i < 7; i++) {
			CourseInfo tempCourse = new CourseInfo();
			tempCourse.setIconName("icon" + i);
			tempCourse.setSubTitle("这是简介" + i);
			tempCourse.setTitle(courseTitle[i]);
			tempCourse.setVisitCount((i +1)* 552);
			tempCourse.setVideoDuration("2015年11月"+(i+1) + "日");
			mCourseInfos.add(tempCourse);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_all_course, null);
		mImageButton = (ImageButton) rootView
				.findViewById(R.id.all_course_menu_imgbtn);
		mImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.toggle();
			}
		});
		listView = (ListView) rootView.findViewById(R.id.all_course_listview);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						ExerciseActivity.class);
				intent.putExtra("choice", arg2+1);
				startActivity(intent);
				getActivity().overridePendingTransition(
						R.anim.activity_anim_left_in, 0);
			}
		});
		listView.setAdapter(new MyCourseAdapter());
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
