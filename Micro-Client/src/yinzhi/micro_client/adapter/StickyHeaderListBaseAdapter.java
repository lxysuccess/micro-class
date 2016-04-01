package yinzhi.micro_client.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.IntroductionActivity;
import yinzhi.micro_client.network.vo.YZCatalogVO;
import yinzhi.micro_client.network.vo.YZChapterVO;
import yinzhi.micro_client.network.vo.YZItemResourceVO;

public class StickyHeaderListBaseAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

	private final Context mContext;
	private List<String> mCombinedCatalog = new ArrayList<String>();
	private int[] mSectionIndices;
	private Character[] mSectionLetters;
	private LayoutInflater mInflater;
	private YZCatalogVO catalogVO;

	public StickyHeaderListBaseAdapter(Context context, YZCatalogVO catalogVO) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		this.catalogVO = catalogVO;
		combineData();
		mSectionIndices = getSectionIndices();
		mSectionLetters = getSectionLetters();

	}

	/**
	 * 分别将各章节中的资源整合到一个List中
	 */
	public void combineData() {

		try {
			mCombinedCatalog.clear();

			List<YZChapterVO> chapters = catalogVO.getChapters();
			List<YZItemResourceVO> resources = new ArrayList<YZItemResourceVO>();
			for (YZChapterVO chapter : chapters) {
				resources = chapter.getResourceList();
				for (YZItemResourceVO resource : resources) {
					mCombinedCatalog.add(resource.getTitle());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int[] getSectionIndices() {
		try {

			ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
			char lastFirstChar = mCombinedCatalog.get(0).charAt(0);
			sectionIndices.add(0);
			for (int i = 1; i < mCombinedCatalog.size(); i++) {
				if (mCombinedCatalog.get(i).charAt(0) != lastFirstChar) {
					lastFirstChar = mCombinedCatalog.get(i).charAt(0);
					sectionIndices.add(i);
				}
			}
			int[] sections = new int[sectionIndices.size()];
			for (int i = 0; i < sectionIndices.size(); i++) {
				sections[i] = sectionIndices.get(i);
			}
			return sections;
		} catch (Exception e) {
			return null;
		}
	}

	private Character[] getSectionLetters() {
		try {
			Character[] letters = new Character[mSectionIndices.length];
			for (int i = 0; i < mSectionIndices.length; i++) {
				letters[i] = mCombinedCatalog.get(mSectionIndices[i]).charAt(0);
			}

			return letters;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public int getCount() {
		return mCombinedCatalog.size();
	}

	@Override
	public Object getItem(int position) {
		return mCombinedCatalog.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		LogUtils.i("**************getView" + position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_catalog_resource, parent, false);
			holder.text = (TextView) convertView.findViewById(R.id.catalog_restitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String itemResourceId = IntroductionActivity.itemResourceId;

		if (IntroductionActivity.fromActivity == "CaptureActivity") {
			// IntroductionActivity的来源页是二维码扫描界面，将制定位置的资源名称标红
			if (IntroductionActivity.itemResourceId != "-1") {
				if (position == mCombinedCatalog.indexOf(itemResourceId)) {

					LogUtils.i("itemResourceId==" + itemResourceId + ";列表位置--》" + position
							+ ";标红的资源mCombinedCatalog.indexOf(itemResourceId)是--》"
							+ mCombinedCatalog.indexOf(itemResourceId));
					holder.text.setTextColor(mContext.getResources().getColor(R.color.main));
				}
			}
		}

		holder.text.setText(mCombinedCatalog.get(position));

		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		LogUtils.i("**************getHeaderView" + position);
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.header, parent, false);
			holder.text = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}
		// 章节视图 的 位置
		Integer headPos = Integer.valueOf(mCombinedCatalog.get(position).substring(0, 1)) - 1;

		String chapterTitle = "";
		if (headPos < catalogVO.getChapters().size()) {
			chapterTitle = catalogVO.getChapters().get(headPos).getChapterTitle();
		} else {
			chapterTitle = catalogVO.getChapters().get(catalogVO.getChapters().size() - 1).getChapterTitle();
		}
		holder.text.setText(chapterTitle);

		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		return mCombinedCatalog.get(position).subSequence(0, 1).charAt(0);
	}

	@Override
	public int getPositionForSection(int section) {
		if (section >= mSectionIndices.length) {
			section = mSectionIndices.length - 1;
		} else if (section < 0) {
			section = 0;
		}
		return mSectionIndices[section];
	}

	@Override
	public int getSectionForPosition(int position) {
		for (int i = 0; i < mSectionIndices.length; i++) {
			if (position < mSectionIndices[i]) {
				return i - 1;
			}
		}
		return mSectionIndices.length - 1;
	}

	@Override
	public Object[] getSections() {
		return mSectionLetters;
	}

	public void clear() {
		mCombinedCatalog.clear();
		mSectionIndices = new int[0];
		mSectionLetters = new Character[0];
		notifyDataSetChanged();
	}

	public void restore(YZCatalogVO catalog) {
		this.catalogVO = catalog;
		combineData();
		mSectionIndices = getSectionIndices();
		mSectionLetters = getSectionLetters();
		notifyDataSetChanged();
	}

	class HeaderViewHolder {
		TextView text;
	}

	class ViewHolder {
		TextView text;
	}

}
