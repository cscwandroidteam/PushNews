package com.pushnews.app.slidingmenu;

/**
 * 当前界面状态
 * @author LZB
 * */
public enum SlidingState {
	SHOWLEFT("显示左边"),
	SHOWCENTER("显示中间"),
	SHOWRIGHT("显示右边");
	
	private final String desc;
	private  SlidingState(String desc) {
		this.desc = desc;		
	}
	public String getDesc() {
		return desc;
	}
	

}

