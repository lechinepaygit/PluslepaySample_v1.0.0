package com.lepay.pluslepaydemo.utils;

import android.text.Spanned;

/**
 * 文字过滤(数字) <br/>&nbsp;&nbsp;
 * 默认位数 999999.99 <br/>&nbsp;&nbsp;
 * 整数6位, 小数2位
 */
public class NumInputFilter extends InputFilterBase {

	/** 整数部位长度(默认) */
	private static final int LEN1 = 6;
	/** 小数部位长度(默认) */
	private static final int LEN2 = 2;
	
	/** 数字正则(含小数) */
	private static final String EGX_NUM			= "^(([1-9]\\d{0,%s})|0)(\\.\\d{0,%s})?$";
	/** 数字正则(不含小数) */
	private static final String EGX_NUM_NOPOINT	= "^([1-9]\\d{0,%s})|0$";
	
	/** 整数位 */
	private int mLen1;
	/** 小数位 */
	private int mLen2;
	
	public NumInputFilter(){
		this(LEN1, LEN2);
	}
	
	public NumInputFilter(int len1, int len2){
		mLen1 = len1 < 0 ? LEN1 : len1;
		mLen2 = len2 < 0 ? LEN2 : len2;

		setDecimalsLen(len2);
	}

	public int getDecimalsLen(){
		return mLen2;
	}

	/**
	 * 定制正则(正则扩展)
	 * <br/>子类扩展后应返回true
	 * @return true:自定义正则; false:本类中两种规则 {@link #EGX_NUM}, {@link #EGX_NUM_NOPOINT}
	 */
	protected boolean customRegexExt(int len1, int len2) {
		return false;
	}

	public void setDecimalsLen(int len){
		mLen2 = len;
		if (customRegexExt(mLen1, mLen2)) return;
		if (mLen2 == 0) {
			mRegex = String.format(EGX_NUM_NOPOINT, mLen1 - 1); // 整数首位[1-9]占一位
		} else {
			mRegex = String.format(EGX_NUM, mLen1 - 1, mLen2); // 整数首位[1-9]占一位
		}
	}

	@Override
	public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
		if (mLen2 != 0 && dest.length() == 0 && ".".equals(src.toString())) return "0.";
		if ("0".equals(dest.toString())) return src;
		return super.filter(src, start, end, dest, dstart, dend);
	}
}