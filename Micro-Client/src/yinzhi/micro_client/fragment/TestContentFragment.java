package yinzhi.micro_client.fragment;

import yinzhi.micro_client.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestContentFragment extends Fragment {
	private static final String TAG = TestContentFragment.class.getSimpleName();
	private String title = "Hello";
	
	public static TestContentFragment newInstance(String s) {
		TestContentFragment newFragment = new TestContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", s);
        newFragment.setArguments(bundle);
        return newFragment;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "TestContentFragment-----onCreate");
        Bundle args = getArguments();
        if(args!=null){
        	title = args.getString("title");
        }
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		 View rootView = inflater.inflate(R.layout.fragment_test_content, container, false);
         
		 findView(rootView);
		 
	     return rootView;
	}

	private void findView(View rootView) {
		
		TextView txtLabel = (TextView) rootView.findViewById(R.id.txtLabel);
		txtLabel.setText(title);
	}
}
