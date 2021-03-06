import {NativeModules} from 'react-native';
import {UnifiedPayModuleSDKInterface, WeChatPreOrderInfo} from "./UnifiedPayModuleSDK";
import {PayMethod} from "./PayMethod";
import {Platform} from "react-native"


export interface PayInfo {

    /**
     * 是否使用沙箱环境
     */
    useSandboxEnv?: boolean;

    /**
     * 支付方式
     */
    method: PayMethod;

    /**
     * 预下单信息
     */
    preOrderInfo: WeChatPreOrderInfo | string;
}

export interface ReactNativeStandardizeThirdPartyPaymentModule {


    /**
     * 支付
     * 如果支付失败 返回对应平台的错误码和错误消息 {errorCode,errorMessage}
     * @param param
     */
    readonly pay: (param: PayInfo) => Promise<void>;
}

const UnifiedPay: UnifiedPayModuleSDKInterface = NativeModules.UnifiedPay;
// ios初始化状态
let INIT_IOS_STATUS: boolean = false;
const IS_IOS = Platform.OS === "ios";

const UnifiedPayModule: ReactNativeStandardizeThirdPartyPaymentModule = {
    pay: (payInfo: PayInfo) => {

        if (payInfo.method === PayMethod.ALI_PAY) {
            // UnifiedPay.setSandboxEnv(payInfo.useSandboxEnv || false);
            return UnifiedPay.aliPay(payInfo.preOrderInfo as string);
        }

        if (payInfo.method === PayMethod.WE_CHAT_PAY) {
            const preOrderInfo = payInfo.preOrderInfo as WeChatPreOrderInfo;
            if (IS_IOS && !INIT_IOS_STATUS) {
                // init only once
                // TODO for ios native 这个控制不太安全，后面应该调整到ios端去做
                INIT_IOS_STATUS = true;
                UnifiedPay.registerApp(preOrderInfo.appId);
            }
            return UnifiedPay.weChatPay(preOrderInfo);
        }

        return Promise.reject(`not support pay method: ${payInfo.method}`);
    }


};

export default UnifiedPayModule;
