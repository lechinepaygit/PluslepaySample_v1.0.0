package com.lepay.pluslepaydemo.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.lepay.pluslepaydemo.PayChannel;
import com.lepay.pluslepaydemo.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Utils {

	/** 订单生成格式 */
	private static final String ORDER_ID_CREATE = "order%s%02d";
	private static Random mRandom = new Random();

	/**
	 * @return 生成订单号
	 */
	public static String mockOrderId(String reqTime){
		return String.format(Locale.US, ORDER_ID_CREATE, reqTime, mRandom.nextInt(100));
	}

	/**
	 * @return 加工金额 分 -> int型
	 */
	public static String amount2int(String str){
		return String.valueOf((int)(Double.parseDouble(str) * 100));
	}

	/**
	 * 获得请求参数 如name1=value1&name2=value2
	 *
	 * @param requestParam 请求参数
	 * @param coder 进行url编码的字符集
	 * @param urlEncode 对value是否进行url编码
	 * @return 拼接后的请求参数
	 */
	public static String getRequestParamString(Map<String, String> requestParam, String coder, boolean urlEncode) {
		if (null == coder || "".equals(coder)) {
			coder = "UTF-8";
		}
		StringBuilder sb = new StringBuilder();
		String reqStr = null;
		if (null != requestParam && 0 != requestParam.size()) {
			for (Map.Entry<String, String> en : requestParam.entrySet()) {
				if (urlEncode) {
					try {
						sb.append(en.getKey()).append('=').append(null == en.getValue() || "".equals(en.getValue()) ? "" : URLEncoder.encode(String.valueOf(en.getValue()), coder)).append('&');
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				} else {
					sb.append(en.getKey()).append('=').append(null == en.getValue() || "".equals(en.getValue()) ? "" : en.getValue()).append('&');
				}
			}
			reqStr = sb.substring(0, sb.length() - 1);
		}
		return reqStr == null ? "" : reqStr;
	}

	public static PayChannel getPayChannel(int resId){
		PayChannel type = null;
		switch (resId) {
			case R.id.rBtnWxPay:{
				type = PayChannel.WXPAY;
			} break;
			case R.id.rBtnZfbPay:{
				type = PayChannel.ALIPAY;
			} break;
			case R.id.rBtnYhkPayCard:
			case R.id.rBtnYhkPayCardNo:{
				type=  PayChannel.QUICK_PAY;
			} break;
		}
		return type;
	}


	/**
	 * 获取当前设备的IMIE
	 * <p>需与上面的isPhone一起使用</p>
	 * <p>需添加权限 android.permission.READ_PHONE_STATE</p>
	 *
	 * @param context 上下文
	 * @return IMIE码
	 */
	public static String getDeviceIMEI(Context context) {
		String deviceId;
		if (isPhone(context)) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = tm.getDeviceId();
		} else {
			deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		}
		return deviceId;
	}

	/**
	 * 判断设备是否是手机
	 *
	 * @param context 上下文
	 * @return true: 是<br>false: 否
	 */
	public static boolean isPhone(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
	}
}
