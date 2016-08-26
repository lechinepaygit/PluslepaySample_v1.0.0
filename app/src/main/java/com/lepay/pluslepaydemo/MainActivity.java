package com.lepay.pluslepaydemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lechinepay.pluslepay.sdk.controller.LePayController;
import com.lechinepay.pluslepay.sdk.listener.LePaySendPayCallBack;
import com.lepay.pluslepaydemo.utils.HttpsUtils;
import com.lepay.pluslepaydemo.utils.NumInputFilter;
import com.lepay.pluslepaydemo.utils.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity implements View.OnClickListener, Callback, LePaySendPayCallBack, RadioGroup.OnCheckedChangeListener {
	private static final String TAG = "PlusLePayDemo";

	// View 控件 -----------------------------------
	TextView txtGoods;
	TextView txtPrice;
	RadioGroup rBtnGroup;
	Button btnPay;
	// View 控件 -----------------------------------

	ProgressDialog dialog;

	/** 支付类别 */
	int mPayType;
	/** 请求时间 */
	String mReqTime;

	private static MediaType CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");

	private SimpleDateFormat mDateSdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		LePayController.initLePayController(this, Const.TEST_MCHID, Const.TEST_CMPAPPID, true,1);
		HttpsUtils.init(this);
		initView();
	}

	private void initView(){
		txtGoods = (TextView) findViewById(R.id.txtGoods);
		txtPrice = (TextView) findViewById(R.id.txtPrice);
		txtPrice.setFilters(new NumInputFilter().toFilterArr());
		rBtnGroup = (RadioGroup) findViewById(R.id.rBtnGroup);
		rBtnGroup.setOnCheckedChangeListener(this);
		btnPay = (Button) findViewById(R.id.btnPay);
		btnPay.setOnClickListener(this);

		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		mPayType = rBtnGroup.getCheckedRadioButtonId();

		// 模拟获取订单信息,
		// 实际应用中, 请通过服务端生成订单信息
		String req = Utils.getRequestParamString(getParams(mPayType), "utf-8", true);
		RequestBody body = RequestBody.create(CONTENT_TYPE, req);
		Request request = new Request.Builder()
				.url(Const.ORDER_ADD)
				.post(body)
				.build();

		HttpsUtils.getInstance().newCall(request).enqueue(this);
		dialog.show();
	}

	/**
	 * 模拟订单数据<br/>
	 * <strong>实际应用中, 请通过服务端生成订单信息</strong>
	 * @return 模拟订单数据
	 */
	private Map<String, String> getParams(int resId){
		Map<String, String> params = new HashMap<>();
		mReqTime = mDateSdf.format(new Date());

		params.put("version", "1.0.0"); // 版本号
		params.put("encoding", "UTF-8"); // 编码方式
//		params.put("signature", "1.0.0"); // 签名
//		params.put("reqReserved", "test_reqReserved"); // 签名
		params.put("mchId", Const.TEST_MCHID); // 商户号
		params.put("cmpAppId", Const.TEST_CMPAPPID); // 应用ID
		params.put("payTypeCode", Utils.getPayChannel(resId).payTypeCode); // 支付渠道编码[支付宝:PT0001; 微信支付:PT0003; 快捷支付:PT0010]
		params.put("outTradeNo", Utils.mockOrderId(mReqTime)); // 订单号
		params.put("tradeTime", mReqTime); // 交易发送时间
		params.put("amount", Utils.amount2int(txtPrice.getText().toString())); // 交易金额
		params.put("summary", txtGoods.getText().toString()); // 摘要
		params.put("summaryDetail", txtGoods.getText().toString() + "___"); // 摘要详情
//		params.put("summary", "test_summary"); // 摘要
//		params.put("summaryDetail", "test_summaryDetail"); // 摘要详情
//		params.put("deviceId", "192.168.123.234"); // 终端设备
		params.put("deviceIp", "192.168.123.234"); // 终端设备IP地址

		return params;
	}

	@Override
	public void onFailure(Call call, IOException e) {
		Log.d(TAG, "onFailure: " + call.request() + "_" + e);
		dialog.dismiss();
	}

	@Override
	public void onResponse(Call call, Response response) throws IOException {
		String string = response.body().string();
		Log.d(TAG, "onResponse: " + call.request() + "_" + response + "_" + string);
		String buyerId = mPayType == R.id.rBtnYhkPayCard ? Const.TEST_USERID : "";

		LePayController.lePaySendPay(this, string, buyerId, this);
		dialog.dismiss();
	}

	@Override
	public void OnPayStart(int payType) {
		switch (payType) {
			case Const.PAY_TYPE_ZFB:{
				Log.d(TAG, "OnPayStart: 支付宝");
			} break;
			case Const.PAY_TYPE_WX:{
				Log.d(TAG, "OnPayStart: 微信");
			} break;
			case Const.PAY_TYPE_QUICK:{
				Log.d(TAG, "OnPayStart: 银行卡");
			} break;
		}
	}

	@Override
	public void OnPayFinished(int payType, boolean isSuccess, String payResult) {
		/**
		 *
		 */
		System.out.print("payType="+payType);
		String type;
		switch (payType) {
			case Const.PAY_TYPE_ZFB:{
				type = "支付宝";
			} break;
			case Const.PAY_TYPE_WX:{
				type = "微信";
			} break;
			case Const.PAY_TYPE_QUICK:{
				type = "银行卡";
			} break;
			default:{
				type = "未知类型";
			} break;

		}

		final String msg = String.format("%s, 类型Code(%s)\n支付结果(%s)\n%s", type, payType, isSuccess ? "成功" : "失败", payResult);
		Log.d(TAG, msg);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
			}
		});
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.rBtnWxPay:
			case R.id.rBtnZfbPay:{
				txtPrice.setText("0.01");
				txtPrice.setEnabled(false);
			} return;
			case R.id.rBtnYhkPayCard:
			case R.id.rBtnYhkPayCardNo:{
				txtPrice.setEnabled(true);
			} return;
			default: break;
		}
	}
}
