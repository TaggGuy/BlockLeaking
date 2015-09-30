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

	public void setAllChecked(boolean ischeked) { // ��� üũ
		int size = isChecked.length;
		for (int a = 0; a < size; a++) {
			isChecked[a] = ischeked;
		}
	}

	public void setChecked(int position) { // üũ�ڽ� ����
		isChecked[position] = !isChecked[position];
	}

	public ArrayList<Integer> getChecked(){
        int size = isChecked.length;
        ArrayList<Integer> checkedList = new ArrayList<Integer>();
        for(int i=0 ; i<size ; i++){
            if(isChecked[i]){
            	checkedList.add(i);
            }
        }
        return checkedList;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_list_item, null);
		}

		ImageView image = (ImageView) convertView.findViewById(R.id.appImage);
		TextView text = (TextView) convertView.findViewById(R.id.appName);
		CheckBox box = (CheckBox) convertView.findViewById(R.id.appCheck);
		
		// CheckBox�� �⺻������ �̺�Ʈ�� ������ �ֱ� ������ ListView�� ������
        // Ŭ������ʸ� ����ϱ� ���ؼ��� CheckBox�� �̺�Ʈ�� ���� �־�� �Ѵ�.
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
