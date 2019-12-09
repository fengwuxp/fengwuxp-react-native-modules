package com.fengwuxp.reactnative.pay;

import com.alipay.sdk.app.EnvUtils;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.xgr.alipay.alipay.AliPay;
import com.xgr.alipay.alipay.AlipayInfoImpli;
import com.xgr.easypay.EasyPay;
import com.xgr.easypay.callback.IPayCallback;
import com.xgr.wechatpay.wxpay.WXPay;
import com.xgr.wechatpay.wxpay.WXPayInfoImpli;


/**
 * 统一支付模块
 * use EasyPay
 *
 * @link https://github.com/kingofglory/EasyPay
 */
public class UnifiedPayModule extends ReactContextBaseJavaModule {


    private ReactApplicationContext reactContext;


    public UnifiedPayModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "UnifiedPay";
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    /**
     * 支付宝支付
     *
     * @param orderInfo 服务端预下单的信息
     * @param promise
     */
    @ReactMethod
    public void aliPay(String orderInfo, Promise promise) {
        //实例化支付宝支付策略
        AliPay aliPay = new AliPay();
        //构造支付宝订单实体。一般都是由服务端直接返回。
        AlipayInfoImpli alipayInfoImpli = new AlipayInfoImpli();
        alipayInfoImpli.setOrderInfo(orderInfo);
        //策略场景类调起支付方法开始支付，以及接收回调。
        EasyPay.pay(aliPay, this.reactContext.getCurrentActivity(), alipayInfoImpli, this.payCallback(promise));
    }

    /**
     * 设置沙箱模式，目前只有支付宝支持
     *
     * @param sandboxEnv
     * @param payMethod
     */
    @ReactMethod
    public void setSandboxEnv(boolean sandboxEnv, String payMethod) {
        if (payMethod == null) {
            payMethod = PayMethod.ALI_PAY.name();
        }
        if (PayMethod.ALI_PAY.name().equals(payMethod)) {
            EnvUtils.setEnv(sandboxEnv ? EnvUtils.EnvEnum.SANDBOX : EnvUtils.EnvEnum.ONLINE);
        }
//        if (PayMethod.WE_CHAT.name().equals(payMethod)) {
//            EnvUtils.setEnv(sandboxEnv ? EnvUtils.EnvEnum.SANDBOX : EnvUtils.EnvEnum.ONLINE);
//        }
    }

    /**
     * 微信支付
     *
     * @param orderInfo
     * @param promise
     */
    @ReactMethod
    public void weChatPay(ReadableMap orderInfo, Promise promise) {
        checkCalled(promise);

        // 实例化微信支付策略
        WXPay wxPay = WXPay.getInstance();
        //构造微信订单实体。一般都是由服务端直接返回。
        WXPayInfoImpli wxPayInfoImpli = new WXPayInfoImpli();
        wxPayInfoImpli.setTimestamp(orderInfo.getString("timeStamp"));
        wxPayInfoImpli.setSign(orderInfo.getString("sign"));
        wxPayInfoImpli.setPrepayId(orderInfo.getString("prepayId"));
        wxPayInfoImpli.setPartnerid(orderInfo.getString("partnerId"));
        wxPayInfoImpli.setAppid(orderInfo.getString("appId"));
        wxPayInfoImpli.setNonceStr(orderInfo.getString("nonceStr"));
        wxPayInfoImpli.setPackageValue(orderInfo.getString("packageValue"));

        //策略场景类调起支付方法开始支付，以及接收回调。
        EasyPay.pay(wxPay, this.reactContext.getCurrentActivity(), wxPayInfoImpli, this.payCallback(promise));

    }

    private void checkCalled(Promise promise) {
        if (promise == null) {
            throw new RuntimeException("Must be called using a promise");
        }
    }

    private IPayCallback payCallback(Promise promise) {
        return new IPayCallback() {
            @Override
            public void success() {
                promise.resolve(PaymentResultStatus.SUCCESS.name());
            }

            @Override
            public void failed(int code, String msg) {
                promise.reject(PaymentResultStatus.FAILURE.name(), msg + " error code:" + code);
            }

            @Override
            public void cancel() {
                promise.reject(PaymentResultStatus.CANCEL.name(), "取消支付");
            }
        };
    }


}
