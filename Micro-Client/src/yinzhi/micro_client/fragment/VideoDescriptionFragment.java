package yinzhi.micro_client.fragment;

import yinzhi.micro_client.network.constants.INetworkConstants;
import yinzhi.micro_client.network.vo.YZCourseVO;
import yinzhi.micro_client.utils.ImageUtil;
import yinzhi.micro_client.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VideoDescriptionFragment extends Fragment {
	/**
	 * 课程名称
	 */
	@ViewInject(R.id.video_description_course_title)
	private TextView courseTitle;

	/**
	 * 教师姓名
	 */
	@ViewInject(R.id.video_description_teacher)
	private TextView teacherName;

	/**
	 * 教师简介
	 */
	@ViewInject(R.id.video_description_teacher_desc)
	private TextView teacherDesc;

	/**
	 * 课程简介
	 */
	@ViewInject(R.id.video_description_course_description)
	private TextView description;

	/**
	 * 教师头像
	 */
	@ViewInject(R.id.video_description_lecturer_icon)
	private ImageView teacherPic;

	/**
	 * 更新简介回调方法
	 */
	IUpdateData iUpdateData;

	public static VideoDescriptionFragment fragment;

	public synchronized static VideoDescriptionFragment newInstance() {
		if (fragment == null) {
			fragment = new VideoDescriptionFragment();
		}
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_video_description, null);
		ViewUtils.inject(this, rootView);

		// 请求更新课程简介信息
		iUpdateData.onUpdateDescription();
		return rootView;
	}

	public void updateDataCompleted(YZCourseVO course) {

		try {
			courseTitle.setText(course.getTitle());
			teacherName.setText(course.getTeacherName());
			teacherDesc.setText(course.getTeacherIntroduction());
			description.setText(course.getCourseIntroduction());

			ImageLoader.getInstance().displayImage(INetworkConstants.YZMC_SERVER + course.getTeacherPicPath(),
					teacherPic, ImageUtil.getDisplayOption(5));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setIUpdateData(IUpdateData iUpdateData) {
		this.iUpdateData = iUpdateData;
	}

	public interface IUpdateData {
		public void onUpdateDescription();
	}

}
