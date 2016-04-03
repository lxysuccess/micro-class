package yinzhi.micro_client.activity;

import yinzhi.micro_client.R;
import yinzhi.micro_client.fragment.AllCourseFragment;
import yinzhi.micro_client.fragment.HomeFragment;
import yinzhi.micro_client.fragment.MenuFragment;
import yinzhi.micro_client.fragment.MenuFragment.SLMenuListOnItemClickListener;
import yinzhi.micro_client.fragment.MyFragment;
import yinzhi.micro_client.fragment.RankingFragment;
import yinzhi.micro_client.fragment.SettingFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lidroid.xutils.util.LogUtils;

public class MainActivity extends SlidingFragmentActivity implements
		SLMenuListOnItemClickListener {

	// 初始化一个侧边栏
	private SlidingMenu mSlidingMenu;

	// 初始化四个Fragment
	private Fragment mTab01;
	private Fragment mTab02;
	private Fragment mTab03;
	private Fragment mTab04;
	private Fragment mTab05;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.frame_left_menu);

		// ActionBar actionBar = getActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setDisplayShowTitleEnabled(false);
		// actionBar.setDisplayUseLogoEnabled(false);

		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.setShadowDrawable(R.drawable.drawer_shadow);// 设置阴影图片
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width); // 设置阴影图片的宽度
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset); // SlidingMenu划出时主页面显示的剩余宽度
		mSlidingMenu.setFadeDegree(0.35f);
		// 设置SlidingMenu 的手势模式
		// TOUCHMODE_FULLSCREEN 全屏模式，在整个content页面中，滑动，可以打开SlidingMenu
		// TOUCHMODE_MARGIN
		// 边缘模式，在content页面中，如果想打开SlidingMenu,你需要在屏幕边缘滑动才可以打开SlidingMenu
		// TOUCHMODE_NONE 不能通过手势打开SlidingMenu
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// 设置SlidingMenu内容
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.left_menu, MenuFragment.getInstance());
		fragmentTransaction.replace(R.id.content, HomeFragment.getInstance());
		fragmentTransaction.commit();
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		// Intent intent = getIntent();
		// String scheme = intent.getScheme();
		// LogUtils.i("scheme:"+scheme);
		// Uri uri = intent.getData();
		// if (uri != null) {
		// String host = uri.getHost();
		// String dataString = intent.getDataString();
		// String id = uri.getQueryParameter("d");
		// String path = uri.getPath();
		// String path1 = uri.getEncodedPath();
		// String queryString = uri.getQuery();
		// LogUtils.i("host:"+host);
		// LogUtils.i("dataString:"+dataString);
		// LogUtils.i("id:"+id);
		// LogUtils.i("path:"+path);
		// LogUtils.i("path1:"+path1);
		// LogUtils.i("queryString:"+queryString);
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			toggle(); // 动态判断自动关闭或开启SlidingMenu
			// getSlidingMenu().showMenu();//显示SlidingMenu
			// getSlidingMenu().showContent();//显示内容
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/**
	 * 根据选中的菜单显示不同界面
	 */
	@Override
	public void selectItem(int position, String title) {
		// update the main content by replacing fragments

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		hideFragment(transaction);

		Fragment fragment = null;
		switch (position) {
		case 0:
			// fragment = HomeFragment.getInstance();

			if (mTab01 == null) {
				mTab01 = new HomeFragment();
				transaction.add(R.id.content, mTab01);
			} else {
				transaction.show(mTab01);
			}
			break;
		case 1:
			// fragment = AllCourseFragment.getInstance();
			if (mTab02 == null) {
				mTab02 = new AllCourseFragment();
				transaction.add(R.id.content, mTab02);
			} else {
				transaction.show(mTab02);
			}
			break;
		case 2:
			// fragment = RankingFragment.getInstance();
			if (mTab03 == null) {
				mTab03 = new RankingFragment();
				transaction.add(R.id.content, mTab03);
			} else {
				transaction.show(mTab03);
			}
			break;
		case 3:
			// fragment = MyFragment.getInstance();
			if (mTab04 == null) {
				mTab04 = new MyFragment();
				transaction.add(R.id.content, mTab04);
			} else {
				transaction.show(mTab04);
			}
			break;
		case 5:
			// fragment = SettingFragment.getInstance();
			if (mTab05 == null) {
				mTab05 = new SettingFragment();
				transaction.add(R.id.content, mTab05);
			} else {
				transaction.show(mTab05);
			}
			break;
		default:
			break;
		}

		// if (fragment != null) {
		// FragmentManager fragmentManager = getSupportFragmentManager();
		// fragmentManager.beginTransaction().replace(R.id.content,
		// fragment).commit();
		// update selected item and title, then close the drawer
		setTitle(title);
		Handler h = new Handler();
		// 延时加载抽屉避免滑动卡顿的情况
		h.postDelayed(new Runnable() {
			public void run() {
				mSlidingMenu.showContent();
			}
		}, 50);

		transaction.commit();
	}

	/**
	 * 隐藏所有fragment
	 * 
	 * @param ts
	 */
	private void hideFragment(FragmentTransaction ts) {
		if (mTab01 != null) {
			ts.hide(mTab01);
		}
		if (mTab02 != null) {
			ts.hide(mTab02);
		}
		if (mTab03 != null) {
			ts.hide(mTab03);
		}
		if (mTab04 != null) {
			ts.hide(mTab04);
		}
		if (mTab05 != null) {
			ts.hide(mTab05);
		}
		LogUtils.i("hideFragment completed");
	}
}
