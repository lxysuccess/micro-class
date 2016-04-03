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
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

		subtitleInput.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		// 响应回车键
		subtitleInput
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH
								|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
							// TODO 搜索
							fetchDatas(0, 50);
							// TODO 搜索
							Toast.makeText(getActivity(), "正在搜索",
									Toast.LENGTH_SHORT).show();

							View view = getActivity().getWindow()
									.peekDecorView();
							if (view != null) {
								InputMethodManager inputmanger = (InputMethodManager) getActivity()
										.getSystemService(
												Context.INPUT_METHOD_SERVICE);
								inputmanger.hideSoftInputFromWindow(
										view.getWindowToken(), 0);
							}
							return true;
						}
						return false;
					}
				});

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

				Toast.makeText(getActivity(), "" + millsTime,
						Toast.LENGTH_SHORT).show();

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

		String startTime = "00:00:00";

		SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");

		Date d1 = null;
		Date d2 = null;
		try {
			d1 = sf.parse(startTime);
			d2 = sf.parse(stTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (d2.getTime() - d1.getTime());

	}

	@OnClick(R.id.search_start)
	public void searchClick(View v) {
		if (subtitleInput.getText().length() == 0) {
			Toast.makeText(getActivity(), "关键词不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		isReset = true;
		fetchDatas(0, 50);
		View view = getActivity().getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

}
