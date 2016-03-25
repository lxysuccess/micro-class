package yinzhi.micro_client.fragment;

import yinzhi.micro_client.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DiscussionFragment extends Fragment {

	public static DiscussionFragment fragment;
	private ListView listView;
	private String[] strings = new String[] {
		    "1.零税率：代表有纳税义务，只不过由于税率是“0”，计算出的因纳税额是0。", "2.免税：是指免除了纳税义务。", "3.两者有重要的差别：虽然两种方法看似都是对某些纳税人或征税对象给予鼓励、扶持或照顾的特殊规定。但是，由于增值税有一个链条的原则，免税因为不征收销项税额，同时进项税额不可抵扣应该转出。而0税率，链条并没有断，所以是不必要进行进项转出的，为了生产0税率而购入的产品等的进项是可以抵扣的。", "4.举例：出口货物。由于是0税率，出口是可以获得退税的。"
		    };
	private Button comment;

	public synchronized static DiscussionFragment newInstance() {
		if (fragment == null) {
			fragment = new DiscussionFragment();
		}
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_discussion, null);
		listView = (ListView) rootView.findViewById(R.id.discussion_list);
		listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,strings));
		comment = (Button) rootView.findViewById(R.id.btn_discussion_send);
		comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO
			}
		});
		return rootView;
	}
}
