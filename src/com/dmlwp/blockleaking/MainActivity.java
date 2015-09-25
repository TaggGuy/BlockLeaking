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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	boolean isModeOn;
	float curBrightness;

	@Override
	public void onBackPressed() {
		if (isModeOn) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			
			alert.setIcon(R.drawable.warning);
			alert.setTitle("������带 ���ּ���");
			alert.setPositiveButton("Ȯ��", null);
			alert.show();
		} else
			this.finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��üȭ�� ����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		isModeOn = false;

		final ImageView ivOn = (ImageView) findViewById(R.id.imageOn);
		final ImageView ivOff = (ImageView) findViewById(R.id.imageOff);

		SharedPreferences mode = getSharedPreferences("mode", 0);
		final SharedPreferences.Editor setting = mode.edit();

		if (mode.getBoolean("isModeOn", false) == false) {
			ivOn.setVisibility(View.GONE);
			ivOff.setVisibility(View.VISIBLE);
			// ��尡 ���������� off��ư��������
		} else {
			ivOn.setVisibility(View.VISIBLE);
			ivOff.setVisibility(View.GONE);
			// ��尡 ���������� on��ư��������
		}

		ivOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMode(true, ivOn, ivOff);

				setting.putBoolean("isModeOn", isModeOn);
				setting.commit();
			}
		});

		ivOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMode(false, ivOn, ivOff);
				
				setting.remove("isModeOn");
				setting.clear();
				setting.commit();
			}
		});
		// viewProcessList();
	}

	public void setAirplaneMode(boolean mode) {
		if (mode) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
				// API level�� 17�̸��̶��
				Settings.System.putInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 1);
				Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
				intent.putExtra("state", !mode);
				sendBroadcast(intent);
			} else { // API level�� 17�̻��̶��
				Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
				startActivity(intent);
				Toast.makeText(this, "����� ž�� ��带 ���ּ���", Toast.LENGTH_SHORT).show();
				// ����â�����
			}
		} else {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
				// API level�� 17�̸��̶��
				Settings.System.putInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0);
				Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
				intent.putExtra("state", !mode);
				sendBroadcast(intent);
			} else { // API level�� 17�̻��̶��
				Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
				startActivity(intent);
				Toast.makeText(this, "����� ž�� ��带 ���ּ���", Toast.LENGTH_SHORT).show();
				// ����â�����
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
				Toast.makeText(this, "�����Ͱ� �������", Toast.LENGTH_SHORT).show();
			} else {
				dataMtd.invoke(cm, false);
				Toast.makeText(this, "�����Ͱ� �������", Toast.LENGTH_SHORT).show();
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
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
		
		alert.setIcon(R.drawable.wifi);
		alert.setTitle("��������");
		alert.setMessage("�������̸� ų���?");

		alert.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				wifi.setWifiEnabled(true);
				dialog.dismiss();
			}
		});
		alert.setNegativeButton("���", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				wifi.setWifiEnabled(false);
				dialog.dismiss();
			}
		});

		alert.show();
	}

	public void alertData() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setIcon(R.drawable.data);
		alert.setTitle("������");
		alert.setMessage("�����͸� ų���?");

		alert.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setData(true);
				dialog.dismiss();
			}
		});
		alert.setNegativeButton("���", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setData(false);
			}
		});

		alert.show();
	}

	public void killActivity() {
		startActivity(new Intent(this, ListActivity.class));
	}

	public void setMode(boolean logic, ImageView on, ImageView off) {
		WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
		ListActivity la = new ListActivity();
		if (logic) {
			off.setVisibility(View.GONE);
			on.setVisibility(View.VISIBLE);
			
			killActivity();
			setAirplaneMode(true);
			setBrightness(true);
			wifi.setWifiEnabled(false);
			

			isModeOn = true;
		} else {
			on.setVisibility(View.GONE);
			off.setVisibility(View.VISIBLE);
			
			setAirplaneMode(false);
			alertWifi();
			alertData();
			setBrightness(false);
			la.KillProcess(false);

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