package com.pushnews.app.slidingmenu;

/**
 * ��ǰ����״̬
 * @author LZB
 * */
public enum SlidingState {
	SHOWLEFT("��ʾ���"),
	SHOWCENTER("��ʾ�м�"),
	SHOWRIGHT("��ʾ�ұ�");
	
	private final String desc;
	private  SlidingState(String desc) {
		this.desc = desc;		
	}
	public String getDesc() {
		return desc;
	}
	

}
