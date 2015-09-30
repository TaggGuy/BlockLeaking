package com.dmlwp.blockleaking;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {

	boolean isModeOn;
	float curBrightness;
	
	ImageView ivOn;
	ImageView ivOff;
	
	SharedPreferences mode;
	SharedPreferences.Editor setting;
	
	AlertDialog.Builder alert;
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.imageOn:
			setMode(false);
			
			setting.remove("isModeOn");
			setting.clear();
			setting.commit();
			break;
		case R.id.imageOff:
			setMode(true);

			setting.putBoolean("isModeOn", isModeOn);
			setting.commit();
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (isModeOn) {
			alert = new AlertDialog.Builder(this);
			
			alert.setIcon(R.drawable.warning);
			alert.setTitle("절전모드를 꺼주세요");
			alert.setPositiveButton("확인", null);
			alert.show();
		} else
			this.finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//setting full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		isModeOn = false;

		//setting view
		ivOn  = (ImageView) findViewById(R.id.imageOn);
		ivOff = (ImageView) findViewById(R.id.imageOff);
		ivOn.setOnClickListener(this);
		ivOff.setOnClickListener(this);
		
		//setting preference
		mode = getSharedPreferences("mode", 0);
		setting = mode.edit();

		if (mode.getBoolean("isModeOn", false) == false) {
			ivOn.setVisibility(View.GONE);
			ivOff.setVisibility(View.VISIBLE);
			//If the mode is off, it will show off image
		} else {
			ivOn.setVisibility(View.VISIBLE);
			ivOff.setVisibility(View.GONE);
			//If the mode is on, it will show on image
		}

	}

	public void setAirplaneMode(boolean mode) {
		if (mode) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
				// API level이 17미만이라면
				Settings.System.putInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 1);
				Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
				intent.putExtra("state", !mode);
				sendBroadcast(intent);
			} else { // API level이 17이상이라면
				Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
				startActivity(intent);
				Toast.makeText(this, "비행기 탑숭 모드를 켜주세요", Toast.LENGTH_SHORT).show();
				// 설정창을띄움
			}
		} else {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
				// API level이 17미만이라면
				Settings.System.putInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0);
				Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
				intent.putExtra("state", !mode);
				sendBroadcast(intent);
			} else { // API level이 17이상이라면
				Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
				startActivity(intent);
				Toast.makeText(this, "비행기 탑숭 모드를 꺼주세요", Toast.LENGTH_SHORT).show();
				// 설정창을띄움
			}
		}
	}

	public void setBrightness(boolean logic) {
		Window window = getWindow();
		WindowManager.LayoutParams wl = window.getAttributes();
		if (logic) {
			curBrightness = wl.screenBrightness;
			wl.screenBrightness = 0.03f;
			window.setAttributes(wl);
		} else {
			wl.screenBrightness = curBrightness;
			window.setAttributes(wl);
		}
	}

	public void setData(boolean logic) {
		try {
			Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if (logic) {
				dataMtd.invoke(cm, true);
				Toast.makeText(this, "데이터가 켜졌어요", Toast.LENGTH_SHORT).show();
			} else {
				dataMtd.invoke(cm, false);
				Toast.makeText(this, "데이터가 꺼졌어요", Toast.LENGTH_SHORT).show();
			}

		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public void alertWifi() {
		alert = new AlertDialog.Builder(this);
		final WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
		
		alert.setIcon(R.drawable.wifi);
		alert.setTitle("와이파이");
		alert.setMessage("와이파이를 킬까요?");

		alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				wifi.setWifiEnabled(true);
				dialog.dismiss();
			}
		});
		alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				wifi.setWifiEnabled(false);
				dialog.dismiss();
			}
		});

		alert.show();
	}

	public void alertData() {
		alert = new AlertDialog.Builder(this);
		
		alert.setIcon(R.drawable.data);
		alert.setTitle("데이터");
		alert.setMessage("데이터를 킬까요?");

		alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setData(true);
				dialog.dismiss();
			}
		});
		alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setData(false);
			}
		});

		alert.show();
	}

	public void setMode(boolean logic) {
		WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
		ListActivity la = new ListActivity();
		if (logic) {
			ivOff.setVisibility(View.GONE);
			ivOn.setVisibility(View.VISIBLE);
			
			startActivity(new Intent(this, ListActivity.class));
			setAirplaneMode(true);
			setBrightness(true);
			wifi.setWifiEnabled(false);
			
			/* off숨기고 on보여줌
			 * ListActivity로이동
			 * 비행기탑승모드 사용
			 * 화면 밝기 낮춤
			 * 와이파이 중지
			 */

			isModeOn = true;
		} else {
			ivOn.setVisibility(View.GONE);
			ivOff.setVisibility(View.VISIBLE);
			
			setAirplaneMode(false);
			alertWifi();
			alertData();
			setBrightness(false);
			la.KillProcess(false);
			
			/* off보여주고 on숨김
			 * 비행기탑승모드 중지
			 * 화면 밝기 복구
			 * 와이파이,데이터 통신 알람
			 * 프로세스 중지쓰레드 종료
			 */
			
			isModeOn = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
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