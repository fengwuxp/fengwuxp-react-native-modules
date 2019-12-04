import {PayMethod} from "./PayMethod";


export interface UnifiedPayModuleSDKInterface {

    /**
     * 支付宝支付
     * @param orderInfo
     */
    aliPay: (orderInfo: string) => Promise<void>;

    /**
     * 设置 沙箱模式，目前只有支付宝才支持
     * @param sandboxEnv
     * @param payMethod
     */
    setSandboxEnv: (sandboxEnv: boolean, payMethod?: PayMethod) => void;

    /**
     * 微信支付
     * @param orderInfo
     */
    weChatPay: (orderInfo: WeChatPreOrderInfo) => Promise<void>;
}

/**
 *微信支付预下单
 */
export interface WeChatPreOrderInfo {

    appId: string;

    timeStamp: string;
    sign: string;
    prepayId: string;
    partnerId: string;
    nonceStr: string;
    packageValue: string;
}
