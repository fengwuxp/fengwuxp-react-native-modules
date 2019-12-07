import {SocialType} from "./SocialType";
import {NativeModules} from 'react-native';
import {MobSDKInterface} from "./MobShareSDK";

export interface MobAuthenticateInterface {
    /**
     * 授权
     * @param platform
     */
    authorize: (platform: SocialType) => Promise<any>;

    /**
     * ali pay 登陆
     */
    authorizeByAliPay: () => Promise<any>

    /**
     * weChat 登陆
     */
    authorizeByWeChat: () => Promise<any>

    /**
     * qq 登陆
     */
    authorizeByQQ: () => Promise<any>
}

const MobShareSDK: MobSDKInterface = NativeModules.MobShareSDK;

const MobAuthenticateModule: MobAuthenticateInterface = {

    authorize: (platform: SocialType) => {
        return MobShareSDK.authorize(platform);
    },
    authorizeByAliPay: () => {
        return MobShareSDK.authorize(SocialType.ALI_PAY);
    },
    authorizeByQQ: () => {
        return MobShareSDK.authorize(SocialType.QQ);
    },
    authorizeByWeChat: () => {
        return MobShareSDK.authorize(SocialType.WE_CHAT);
    }

};

export default MobAuthenticateModule;
