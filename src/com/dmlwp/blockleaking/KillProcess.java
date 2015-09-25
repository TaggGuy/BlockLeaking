package com.dmlwp.blockleaking;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.util.Log;

public class KillProcess extends Thread {

	ActivityManager am;
	List<Integer> position;
	List<PackageInfo> data;
	Method mtd;

	static boolean _FLAG = false;

	KillProcess(ActivityManager am, List<PackageInfo> data, List<Integer> position) {
		this.am = am;
		this.data = data;
		this.position = position;
	}

	public static void myStop() {
		_FLAG = true;
	}

	@Override
	public void run() {
		while (true) {
			if (_FLAG)
				break;
			try {
				// 1/1000�� ������ �Է��ϸ� �ش� �ð����� non-runnable���·� ������ �ȴ�.
				// 1000�� �Է��ϸ� 1�� ����.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i : position) {
				try {
					mtd = ActivityManager.class.getDeclaredMethod("restartPackage", String.class);
					mtd.invoke(data.get(i).packageName);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				android.os.Process.killProcess(i);
				am.killBackgroundProcesses(data.get(i).packageName);
				Log.d("ABC", data.get(i).packageName + " is Killed");
			}
		}
	}
}
