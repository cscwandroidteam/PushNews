package com.pushnews.app.ui;

import com.example.slidingmenu.view.SlidingMenu;
import com.example.slidingmenu.view.SlidingState;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {
	
	private SlidingMenu slidingMenu;
	private String lf_title[] = { "����", "����", "����", "����", "�Ƽ�",
			"ͷ��", "��������", "�ƾ�", "Ȥͼ", "����", "����", "��������", "�˳���ǰ�ʺ�" };
	private String lr_title[] = { "����", "��Ϣ����", "�ҵ��ղ�", "�ı������С", "�ı�����","��������",
			"�˳���ǰ�ʺ�" };
	private String lc_title[] = { "�㶫��ҵ��ѧ�ش�����", "�㶫��ҵ��ѧ�ش�����", "�㶫��ҵ��ѧ�ش�����", "�㶫��ҵ��ѧ�ش�����",
			"�㶫��ҵ��ѧ�ش�����","�㶫��ҵ��ѧ�ش�����","�㶫��ҵ��ѧ�ش�����","�㶫��ҵ��ѧ�ش�����","�㶫��ҵ��ѧ�ش�����","�㶫��ҵ��ѧ�ش�����",
			"�㶫��ҵ��ѧ�ش�����","�㶫��ҵ��ѧ�ش�����","�㶫��ҵ��ѧ�ش�����","�㶫��ҵ��ѧ�ش�����","�㶫��ҵ��ѧ�ش�����"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* ����ȫ�� */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sliding_menu);
        slidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
        /**
		 * getLayoutInflater().inflate(int resource, ViewGroup root);
		 * ����Ƕ��Զ����xml�����ļ����н��ͣ�Ȼ���ȡ��android�ܹ������Ϊһ��layout;
		 */
		ViewGroup leftView = (ViewGroup) getLayoutInflater().inflate(
				R.layout.sl_left_v, null);
		ViewGroup rightView = (ViewGroup) getLayoutInflater().inflate(
				R.layout.sl_right_v, null);
		ViewGroup centerView = (ViewGroup) getLayoutInflater().inflate(
				R.layout.sl_center_v, null);
		/* ������Զ����coSlidingMenu����м仭������ */
		slidingMenu.setCenterView(centerView);
		/* �����ȡ�Զ������ҽ���Ŀ�� */
		/* �����ȡ�Զ������ҽ���Ŀ�� */
		int leftWidth = (int) getResources()
				.getDimension(R.dimen.leftViewWidth);
		int rightWidth = (int) getResources().getDimension(
				R.dimen.rightViewWidth);
		/*
		 * ������Զ����SlidingMenu�����ߺ��ұ߻������� ��Ҫ��ȷ�����ǵĿ��
		 */
		slidingMenu.setLeftView(leftView, leftWidth);
		slidingMenu.setRightView(rightView, rightWidth);
		/* �����ұ�ImageView����Ӧ�¼� */
		View ivRight = centerView.findViewById(R.id.ivRight);
		ivRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * ��ȡ��ǰ�����״̬ �ܹ�������:SHOWRIGHT,SHOWCENTER,SHOWLEFT
				 * ��Ҫ�Ǹ�SHOWCENTER��Ա�
				 */
				if (slidingMenu.getCurrentUIState() == SlidingState.SHOWRIGHT) {
					slidingMenu.showViewState(SlidingState.SHOWCENTER);
				} else {
					slidingMenu.showViewState(SlidingState.SHOWRIGHT);
				}
			}
		});
		
		/* ͬ�� */
		View ivLeft = centerView.findViewById(R.id.ivLeft);

		ivLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (slidingMenu.getCurrentUIState() == SlidingState.SHOWLEFT) {
					slidingMenu.showViewState(SlidingState.SHOWCENTER);
				} else {
					slidingMenu.showViewState(SlidingState.SHOWLEFT);
				}
			}
		});
		/* ����ͬ��ImageView */
		View btnLeft = leftView.findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingMenu.showViewState(SlidingState.SHOWCENTER);

			}
		});
		
		/* ������ߵ�listview */
		ListView lvLeft = (ListView) leftView.findViewById(R.id.lvLeft);
		lvLeft.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.tv_item, lf_title));
		lvLeft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			

			}
		});
		/* �����ұߵ�listview */
		ListView lvRight = (ListView) rightView.findViewById(R.id.lvRight);
		lvRight.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.tv_item, lr_title));
		lvRight.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			

			}
		});
		/* �����м��listview */
		ListView lvcenter = (ListView) centerView.findViewById(R.id.lvCenter);
		lvcenter.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.tv_item, lc_title));
		lvcenter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {	

			}
		});
	
        
    }


}
