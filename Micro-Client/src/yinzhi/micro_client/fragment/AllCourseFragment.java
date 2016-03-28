package yinzhi.micro_client.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.MainActivity;
import yinzhi.micro_client.activity.SearchActivity;
import yinzhi.micro_client.bean.CourseInfo;

public class AllCourseFragment extends Fragment {

	@ViewInject(R.id.all_search_imgbtn)
	private ImageButton searchBtn;

	private ImageButton mImageButton;

	// 所有课程页课程列表
	private List<CourseInfo> mCourseInfos = new ArrayList<CourseInfo>();
	// 课程图片
	public int[] imgs = { R.drawable.book1, R.drawable.book2, R.drawable.book3, R.drawable.book4, R.drawable.book5,
			R.drawable.book6, R.drawable.book7 };
	private String courseTitle[] = { "电子报税实训", "基础会计", "成本会计", "财务会计", "财务管理", "会计电算化", "会计基础实训" };
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
		// !!!!!!!!!!!!!!!!!!!!!!!!!
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		for (int i = 0; i < 7; i++) {
			CourseInfo tempCourse = new CourseInfo();
			tempCourse.setIconName("icon" + i);
			tempCourse.setSubTitle("这是简介" + i);
			tempCourse.setTitle(courseTitle[i]);
			tempCourse.setVisitCount((i + 1) * 552);
			tempCourse.setVideoDuration("2015年11月" + (i + 1) + "日");
			mCourseInfos.add(tempCourse);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_all_course, null);

		ViewUtils.inject(this, rootView);

		mImageButton = (ImageButton) rootView.findViewById(R.id.all_course_menu_imgbtn);
		mImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.toggle();
			}
		});
		return rootView;
	}

	@OnClick(R.id.all_search_imgbtn)
	public void searchClick(View v) {
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.activity_anim_left_in, 0);
	}

}
