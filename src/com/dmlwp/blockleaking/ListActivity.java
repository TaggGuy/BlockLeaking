package com.dmlwp.blockleaking;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class ListActivity extends AppCompatActivity {

	List<PackageInfo> tempList = new ArrayList<PackageInfo>();

	List<Integer> positionList;
	List<PackageInfo> data;
	ListView listView;
	CustomAdapter myAdapter;
	
	boolean isAllChecked = true;
	KillProcess kp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 전체화면 설정
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		final Button button1 = (Button) findViewById(R.id.list_all_button);
		Button button2 = (Button) findViewById(R.id.list_check_button);

		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myAdapter.setAllChecked(isAllChecked);
				isAllChecked = !isAllChecked;
				// Adapter에 Data에 변화가 생겼을때 Adapter에 알려준다.
				myAdapter.notifyDataSetChanged();

			}
		});

		PackageManager pm = getPackageManager();
		tempList = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);

		data = new ArrayList<PackageInfo>();

		for (PackageInfo temp : tempList) {
			if(temp.packageName.equals("com.dmlwp.blockleaking"))
				continue;
			if (isSystemPackage(temp))
				data.add(temp);
		}

		myAdapter = new CustomAdapter(data, getLayoutInflater(), pm);

		button2.setOnClickListener(listener1);

		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(listener2);
		listView.setAdapter(myAdapter);
	}

	OnClickListener listener1 = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			KillProcess(true);
		}
	};

	AdapterView.OnItemClickListener listener2 = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			myAdapter.setChecked(position); // 체크박스
											// 반전
			myAdapter.notifyDataSetChanged(); // commit
		}
	};

	private boolean isSystemPackage(PackageInfo pkgInfo) {
		return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? false : true;
	}

	public void KillProcess(boolean logic) {
		if(logic)
		{
			positionList = myAdapter.getChecked();
			kp = new KillProcess((ActivityManager) getSystemService(ACTIVITY_SERVICE), data, positionList);
			kp.start();
			ListActivity.this.finish();
		}
		else
		{
			KillProcess.myStop();
			// sry TT
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
