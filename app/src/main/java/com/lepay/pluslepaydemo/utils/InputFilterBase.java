package com.lepay.pluslepaydemo.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterBase implements InputFilter {

	private StringBuilder sb;
	/** 正则匹配 */
	protected String mRegex;
//	/** 不匹配时显示信息 */
//	protected String mMsg;

	protected InputFilterBase(){
		sb = new StringBuilder();
	}

	public InputFilterBase(String regex, String msg){
		sb = new StringBuilder();
		this.mRegex = regex;
//		this.mMsg = msg;
	}

	public InputFilter[] toFilterArr(){
		 return new InputFilter[]{this};
	}

	public void setRegex(String regex){
		this.mRegex = regex;
	}

//	public void setMsg(String msg){
//		this.mMsg = msg;
//	}

	@Override
	public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
		sb.setLength(0);
		sb.append(dest);
		sb.replace(dstart, dend, src.toString());
		if (!sb.toString().matches(mRegex)) {
//			if (mMsg != null && mMsg.length() != 0) MyToast.showToast(true, mMsg);
			return "";
		}
		return src;
	}
}