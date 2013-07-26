package com.pushnews.app.model;

/** 用户实体类 */
public class User {
	/** 用户ID */
	private int user_id;
	/** 用户账号 */
	private String username;
	/** 用户密码 */
	private String password;
	/** 用户配置标签 */
	private int mark;

	public User() {
		super();
	}

	public User(String username, String password, int mark) {
		super();
		this.username = username;
		this.password = password;
		this.mark = mark;
	}

	public int getId() {
		return user_id;
	}

	public void setId(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	@Override
	public String toString() {
		return "user_id=" + user_id + ";username=" + username + ";password="
				+ password + ";mark=" + mark;
	}
}
