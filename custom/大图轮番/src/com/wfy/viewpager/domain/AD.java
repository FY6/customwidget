package com.wfy.viewpager.domain;

public class AD {
	private int resID;
	private String intro;

	public AD(int resID, String intro) {
		super();
		this.resID = resID;
		this.intro = intro;
	}

	public int getResID() {
		return resID;
	}

	public void setResID(int resID) {
		this.resID = resID;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

}
