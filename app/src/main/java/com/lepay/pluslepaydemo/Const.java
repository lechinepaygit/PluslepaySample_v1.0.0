package com.lepay.pluslepaydemo;

public interface Const {
	// 测试环境，测试用户下单 (api提供签名)
	String SITE_SIGNED = "https://openapi.lechinepay.com/lepay.appapi";

	/** 订单添加 */
	String ORDER_ADD = SITE_SIGNED + "/order/add.json";

	// 测试商户 -------------------------
	/** 测试商户ID */
	String TEST_MCHID = "32002";
	/** 测试AppID */
	String TEST_CMPAPPID = "35002";

	/** 支付宝 */
	int PAY_TYPE_ZFB		= 1;
	/** 微信 */
	int PAY_TYPE_WX		= 2;
	/** 快捷支付 */
	int PAY_TYPE_QUICK	= 3;
}
