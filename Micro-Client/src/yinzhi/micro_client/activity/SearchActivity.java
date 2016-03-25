package yinzhi.micro_client.activity;

import yinzhi.micro_client.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class SearchActivity extends Activity {
	private TextView cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		cancel = (TextView) findViewById(R.id.search_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SearchActivity.this.finish();
			}
		});
	}
}
