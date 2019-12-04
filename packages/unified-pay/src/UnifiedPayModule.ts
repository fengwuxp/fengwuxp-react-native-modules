import {NativeModules} from 'react-native';
import {UnifiedPayModuleSDKInterface, WeChatPreOrderInfo} from "./UnifiedPayModuleSDK";
import {PayMethod} from "./PayMethod";


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


const UnifiedPayModule: ReactNativeStandardizeThirdPartyPaymentModule = {
    pay: (payInfo: PayInfo) => {

        if (payInfo.method === PayMethod.ALI_PAY) {
            UnifiedPay.setSandboxEnv(payInfo.useSandboxEnv || false);
            return UnifiedPay.aliPay(payInfo.preOrderInfo as string);
        }
        if (payInfo.method === PayMethod.WE_CHAT_PAY) {
            return UnifiedPay.weChatPay(payInfo.preOrderInfo as WeChatPreOrderInfo);
        }

        return Promise.reject(`not support pay method: ${payInfo.method}`);
    }


};

export default UnifiedPayModule;
