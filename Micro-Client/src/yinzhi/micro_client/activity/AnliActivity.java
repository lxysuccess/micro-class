package yinzhi.micro_client.activity;

import yinzhi.micro_client.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class AnliActivity extends Activity {
	private ImageButton close;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anli);
		close = (ImageButton) findViewById(R.id.anli_close);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AnliActivity.this.finish();
			}
		});
	}
}
