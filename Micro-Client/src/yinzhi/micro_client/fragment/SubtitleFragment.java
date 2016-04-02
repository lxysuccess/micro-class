package yinzhi.micro_client.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.video.IjkVideoActicity;
import yinzhi.micro_client.adapter.LxyCommonAdapter;
import yinzhi.micro_client.adapter.LxyViewHolder;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.vo.YZSubtitleVO;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class SubtitleFragment extends Fragment {

	@ViewInject(R.id.subtitle_search_result)
	private ListView subtitleResult;
	@ViewInject(R.id.search_start)
	private Button startSearch;
	@ViewInject(R.id.subtitle_search_input)
	private EditText subtitleInput;

	private Boolean isReset = true;

	private List<YZSubtitleVO> sutitleDatas = new ArrayList<YZSubtitleVO>();

	private LxyCommonAdapter<YZSubtitleVO> stAdapter;

	private IjkVideoActicity activity;

	private String itemResourceId;

	public SubtitleFragment(String itemResourceId) {
		// TODO Auto-generated constructor stub
		this.itemResourceId = itemResourceId;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		this.activity = (IjkVideoActicity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_subtitle_search,
				null);
		ViewUtils.inject(this, rootView);
		initView();

		return rootView;
	}

	private void initView() {
		// 初始化适配器
		stAdapter = new LxyCommonAdapter<YZSubtitleVO>(getActivity(),
				sutitleDatas, R.layout.item_subtitle_result) {

			@Override
			public void convert(LxyViewHolder holder, YZSubtitleVO t) {
				try {
					holder.setText(R.id.subtitle_content, t.getSubtitle());
					holder.setText(R.id.subtitle_time, t.getTime());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LogUtils.e("error! video view when show subtile list");
				}
			}
		};

		// 设置字幕搜索结果list 的适配器
		subtitleResult.setAdapter(stAdapter);

		subtitleResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				LogUtils.i("position------>" + position);

				// 将时间转化为viewView可用格式

				String time = sutitleDatas.get(position).getTime();
				Long millsTime = getMillsTime(time);

				// TODO 视频跳至某个时间点
				activity.seekTo(millsTime);

			}
		});
	}

	/**
	 * 获取字幕搜索数据
	 * 
	 * @param page
	 * @param size
	 */
	private void fetchDatas(int page, int size) {
		YZNetworkUtils.searchVideoSubtitle("", subtitleInput.getText()
				.toString(), itemResourceId, page, size,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						String response = arg0.result;

						LogUtils.i(response + "------------------subtitle");

						if (!YZNetworkUtils.isAllowedContinue(getActivity(),
								response)) {
							return;
						}

						List<YZSubtitleVO> results = new ArrayList<YZSubtitleVO>();
						results = YZResponseUtils.parseArray(response,
								YZSubtitleVO.class);

						if (isReset) {
							// 如果用户点击了搜索按钮，则清空之前的数据重新刷新列表
							sutitleDatas.clear();
						}
						sutitleDatas.addAll(results);

						stAdapter.notifyDataSetChanged();

					}
				});

	}

	/**
	 * 将视频的时间转换为毫秒
	 * 
	 * @param videoTimeLong2
	 * @return
	 */
	private Long getMillsTime(String stTime) {

		SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
		Date d1 = null;
		try {
			d1 = sf.parse(stTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d1.getTime();

	}

	@OnClick(R.id.search_start)
	public void searchClick() {
		if (subtitleInput.getText().length() == 0) {
			Toast.makeText(getActivity(), "关键词不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		isReset = true;
		fetchDatas(0, 20);
	}

}
