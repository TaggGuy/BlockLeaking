package com.dmlwp.blockleaking;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

	List<PackageInfo> data = new ArrayList<PackageInfo>();
	LayoutInflater inflater;
	PackageManager pm;
	boolean[] isChecked;

	CustomAdapter(List<PackageInfo> data, LayoutInflater inflater, PackageManager pm) {
		this.data = data;
		this.inflater = inflater;
		this.pm = pm;

		this.isChecked = new boolean[data.size()];
	}

	public void setAllChecked(boolean ischeked) { // 모두 체크
		int size = isChecked.length;
		for (int a = 0; a < size; a++) {
			isChecked[a] = ischeked;
		}
	}

	public void setChecked(int position) { // 체크박스 반전
		isChecked[position] = !isChecked[position];
	}

	public ArrayList<Integer> getChecked(){
        int size = isChecked.length;
        ArrayList<Integer> mArrayList = new ArrayList<Integer>();
        for(int i=0 ; i<size ; i++){
            if(isChecked[i]){
                mArrayList.add(i);
            }
        }
        return mArrayList;
    }
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * public void viewProcessList() { ActivityManager am = (ActivityManager)
	 * getSystemService(Context.ACTIVITY_SERVICE);
	 * List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList =
	 * am.getRunningAppProcesses();
	 * 
	 * List<String> processList = new ArrayList<String>();
	 * 
	 * for (ActivityManager.RunningAppProcessInfo rapi :
	 * runningAppProcessInfoList) { processList.add(rapi.processName); // 저장한
	 * 리스트의 크기만큼 문자열로 프로세스 이름을 저장
	 * 
	 * 
	 * StringBuilder processNames = new StringBuilder(); // 크기가 클수있으니
	 * 
	 * for (String processName : processList) {
	 * processNames.append(processName); processNames.append('\n'); }
	 * 
	 * } }
	 */

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_list_item, null);
		}

		ImageView image = (ImageView) convertView.findViewById(R.id.appImage);
		TextView text = (TextView) convertView.findViewById(R.id.appName);
		CheckBox box = (CheckBox) convertView.findViewById(R.id.appCheck);
		
		// CheckBox는 기본적으로 이벤트를 가지고 있기 때문에 ListView의 아이템
        // 클릭리즈너를 사용하기 위해서는 CheckBox의 이벤트를 없애 주어야 한다.
        box.setClickable(false);
        box.setFocusable(false);

		PackageInfo info = data.get(position);
		Drawable img = pm.getApplicationIcon(info.applicationInfo);
		String title = pm.getApplicationLabel(info.applicationInfo).toString();

		image.setImageDrawable(img);
		text.setText(title);
		box.setChecked(isChecked[position]);

		return convertView;
	}

}
