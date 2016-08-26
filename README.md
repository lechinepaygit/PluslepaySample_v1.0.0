 # PluslepaySample_v1.0.0
+LePay 接入指南（Android Studio）
================================

一、快速体验
------------

下载[PluslepaySample](https://github.com/lechinepaygit/PluslepaySample_v1.0.0)到您的电脑并使用Android Studio 打开该工程，或者直接在Android Studio中连接GitHub进行导入:

`Gradle→File→New→Project from Versin Control→GitHub→https://github.com/lechinepaygit/PluslepaySample_v1.0.0.git`


> 运行该项目工程就可快速体验+LePay V1.0.0中的支付宝、微信、银行卡快捷支付等方式。

二、快速集成
----------------

* 导入+LePay SDK

> 在您的项目工程的build.gradle的dependencies方法中加入`compile 'com.pluslepay.android:pluslepaylib:1.0.2'`导入SDK

          dependencies {

          compile 'com.pluslepay.android:pluslepaylib:1.0.2'

         }
 * 清单文件Androidmanifest.xml 配置

> +LePay SDK需要使用到一些权限所以需要在清单文件Androidmanifest.xml 的manifest标签下配置如下权限

          <!--网络权限 -->
          <uses-permission android:name="android.permission.INTERNET"/>
          <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
          <!--手机状态权限 -->
          <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
          <!--SD卡写入权限 -->
          <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>



> 在application标签中 对所需的Activity进行注册(如果不是用微信支付可不进行添加)


         <activity
         android:name="com.lechinepay.pluslepay.sdk.activity.wx.WXPayEntryActivity"
         android:configChanges="orientation|screenSize"
         android:launchMode="singleTop"
         android:theme="@android:style/Theme.Translucent.NoTitleBar" />
         <activity-alias
         android:name="${applicationId}.wxapi.WXPayEntryActivity"
         android:exported="true"
         android:launchMode="singleTop"
         android:targetActivity="com.lechinepay.pluslepay.sdk.activity.wx.WXPayEntryActivity"/>


* 初始化SDK

> 在您的项目app入口如Application类中调用`LePayController.initLePayController(Context, 商户ID, AppID, boolean);`对SDK进行初始化,其中商户ID和AppID是您在+LePay注册应用时系统生成的,可在网站中点击您的用户名进行查看如果您还没有应用可以根据+LePay接入指南进行应用创建,如果不进行初始化SDK将调用失败。

* 支付调用

> > 获取Charge订单数据：Serve端进行SDK接入，下单后返回的Charge数据，获取后调用LePayController.lePaySendPay(Activity activity, String Charge, String BuyerId, LePaySendPayCallBack lePaySendPayCallBack)方法进行调起支付。注意：调起支付方法的Activity需要实现`LePaySendPayCallBack`回调接口或者在调用支付方法时对`LePaySendPayCallBack`回调方法进行实例化，并实现`OnPayStart(int payType)`和 `OnPayFinished(int payType, boolean isSuccess, String payResult)`这两个回调方法


          LePayController.lePaySendPay(this, Charge, buyerId, this);
          @Override
          public void OnPayStart(int payType) {
          /**
          * payType 为调用支付的方式 支付宝=1,微信=2,快捷支付=3
          */

          System.out.print("payType="+payType);

          }
         @Override
         public void OnPayFinished(int payType, boolean isSuccess, String payResult) {
         /**
         * payType 为调用支付的方式 支付宝=1,微信=2,快捷支付=3
         * isSuccess 为支付成功与否
         * payResult 是回调数据
         * 其中数据中的channelCode为通道返回结果码,Channel为支付方式，respCode为支付状态，respMsg为结果信息
         * respCode=0为成功，respCode=-1为失败，respCode=-2为其他或者取消，respCode=1为其他状态
         */

         System.out.print("payType="+payType+"isSuccess="+isSuccess+"payResult"+payResult);

         }


*支付结果

> 支付结果格式示例payResult:{"channel":"chanpay","channelCode":"0","respMsg":"支付处理中","respCode":0}


         * 代码混淆

> 如果您的工程进行了代码混淆，请添加以下配置


         -keepattributes Signature
         -keepattributes *Annotation* # 已存在
         -keep class okhttp3.** { *; }
         -keep interface okhttp3.** { *; }
         -dontwarn okhttp3.**

         -dontwarn java.nio.file.*
         -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
         -dontwarn okio.**


         -dontwarn javax.crypto.**
         -dontwarn org.bouncycastle.**

         -keep class com.lechinepay.pluslepay.controller.LePayController { *; }
         -keep interface com.lechinepay.pluslepay.listener.LePaySendPayCallBack { *; }
         -keep class com.lechinepay.pluslepay.sdk.Entity.** { *; }
         -keep class com.lechinepay.pluslepay.sdk.json.resultentity.** { *; }
         -keep class org.bouncycastle.**

> 如果与您的配置有冲突的可自行去重 。


+LePay 接入指南（Eclipse）
================================


一、快速集成
----------------

* 导入+LePay SDK

> 在您的Eclipse中导入[Pluslepaylibrary](https://github.com/missqikang2016/pluslepaylibrary.git)工程，并对该library进行依赖：`右键点击您的工程→选择Properties→Android→library选框中选择Add→点击Apply→Ok`。


* 清单文件Androidmanifest.xml 配置

> +LePay SDK需要使用到一些权限所以需要在清单文件Androidmanifest.xml 的manifest标签下配置如下权限

         <!--网络权限 -->
         <uses-permission android:name="android.permission.INTERNET"/>
         <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
         <!--手机状态权限 -->
         <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
         <!--SD卡写入权限 -->
         <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>



> 在application标签中 对所需的Activity进行注册


         <!-- 微信支付 -->
         <activity
         android:name="com.lechinepay.pluslepay.sdk.activity.wx.WXPayEntryActivity"
         android:configChanges="orientation|screenSize"
         android:launchMode="singleTop"
         android:theme="@android:style/Theme.Translucent.NoTitleBar" />

         <activity-alias
         android:name=".wxapi.WXPayEntryActivity"
         android:exported="true"
         android:launchMode="singleTop"
         android:targetActivity="com.lechinepay.pluslepay.sdk.activity.wx.WXPayEntryActivity" />

         <!-- 快捷支付、支付宝支付 -->

         <activity
         android:name="com.lechinepay.pluslepay.sdk.activity.lepayactivity.LePayPaymentResultActivity"
         android:windowSoftInputMode="stateHidden|adjustResize" />
         <activity
         android:name="com.lechinepay.pluslepay.sdk.activity.lepayactivity.LePayPaymentGetCodeActivity"
         android:windowSoftInputMode="stateHidden|adjustResize" />
         <activity
         android:name="com.lechinepay.pluslepay.sdk.activity.lepayactivity.LePayPaymentCardAddActivity"
         android:windowSoftInputMode="stateHidden|adjustResize" />
         <activity
         android:name="com.lechinepay.pluslepay.sdk.activity.lepayactivity.LePayPaymentInputCardNumberActivity"
         android:windowSoftInputMode="stateHidden|adjustResize" />
         <activity
         android:name="com.lechinepay.pluslepay.sdk.activity.lepayactivity.LePayPayMentCardListActivity"
         android:windowSoftInputMode="stateHidden|adjustResize" />


* 导入jar包

> 复制library中的lib目录下的所有jar包，粘贴到您的项目lib目录下。导入编译即可

* 初始化SDK

> 在您的项目app入口如Application类中调用`LePayController.initLePayController(Context, 商户ID, AppID, boolean);`对SDK进行初始化,其中商户ID和AppID是您在+LePay注册应用时系统生成的,可在网站中点击您的用户名进行查看如果您还没有应用可以根据+LePay接入指南进行应用创建,如果不进行初始化SDK将调用失败。

* 支付调用

> > 获取Charge订单数据：Serve端进行SDK接入，下单后返回的Charge数据，获取后调用LePayController.lePaySendPay(Activity activity, String Charge, String BuyerId, LePaySendPayCallBack lePaySendPayCallBack)方法进行调起支付。注意：调起支付方法的Activity需要实现`LePaySendPayCallBack`回调接口或者在调用支付方法时对`LePaySendPayCallBack`回调方法进行实例化，并实现`OnPayStart(int payType)`和 `OnPayFinished(int payType, boolean isSuccess, String payResult)`这两个回调方法


         LePayController.lePaySendPay(this, Charge, buyerId, this);
         @Override
         public void OnPayStart(int payType) {
         /**
         * payType 为调用支付的方式 支付宝=1,微信=2,快捷支付=3
         */

         System.out.print("payType="+payType);

         }
         @Override
         public void OnPayFinished(int payType, boolean isSuccess, String payResult) {
        /**
         * payType 为调用支付的方式 支付宝=1,微信=2,快捷支付=3
         * isSuccess 为支付成功与否
         * payResult 是回调数据
         * 其中数据中的channelCode为通道返回结果码,Channel为支付方式，respCode为支付状态，respMsg为结果信息
         * respCode=0为成功，respCode=-1为失败，respCode=-2为其他或者取消，respCode=1为其他状态
         */

         System.out.print("payType="+payType+"isSuccess="+isSuccess+"payResult"+payResult);

         }


*支付结果

> 支付结果格式示例payResult:{"channel":"chanpay","channelCode":"0","respMsg":"支付处理中","respCode":0}



* 代码混淆

> 如果您的工程进行了代码混淆，请添加以下配置


         -keepattributes Signature
         -keepattributes *Annotation* # 已存在
         -keep class okhttp3.** { *; }
         -keep interface okhttp3.** { *; }
         -dontwarn okhttp3.**

         -dontwarn java.nio.file.*
         -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
         -dontwarn okio.**


         -dontwarn javax.crypto.**
         -dontwarn org.bouncycastle.**

         -keep class com.lechinepay.pluslepay.controller.LePayController { *; }
         -keep interface com.lechinepay.pluslepay.listener.LePaySendPayCallBack { *; }
         -keep class com.lechinepay.pluslepay.sdk.Entity.** { *; }
         -keep class com.lechinepay.pluslepay.sdk.json.resultentity.** { *; }
         -keep class org.bouncycastle.**

> 如果与您的配置有冲突的可自行去重 。



谢谢
--------------------

    @+LePay Android研发小组
