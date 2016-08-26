package com.lepay.pluslepaydemo;

// 支付渠道编码[支付宝:PT0001; 微信支付:PT0003; 快捷支付:PT0010]
public enum PayChannel {
	ALIPAY("PT0001"), WXPAY("PT0003"), QUICK_PAY("PT0010");

	String payTypeCode;

	PayChannel(String payTypeCode){
		this.payTypeCode = payTypeCode;
	}
}
