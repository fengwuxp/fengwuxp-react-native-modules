import {SocialType} from "./SocialType";
import {NativeModules} from 'react-native';
import {MobSDKInterface} from "./MobShareSDK";

export interface WeChatUserInfo {
    unionid: string;
    openid: string;
    country: string;
    nickname: string;
    city: string
    privilege: string[];
    userTags?: string;
    language: string;
    province: string;
    headimgurl: string;

    // 1或2
    sex: number;
}

export interface AliPayUserInfo {

    userId: string,
    aliayOpenId: string
    authCode: string,
    resultCode: string,

}


export interface MobAuthenticateInterface {
    /**
     * 授权
     * @param platform
     * @param authorizeInfo
     */
    authorize: (platform: SocialType, authorizeInfo?: string) => Promise<WeChatUserInfo | AliPayUserInfo>;

    /**
     * 获取用户信息
     * @param platform
     * @param account
     */
    doUserInfo: (platform: SocialType, account?: string) => Promise<WeChatUserInfo | AliPayUserInfo>

    /**
     * ali pay 登陆
     */
    authorizeByAliPay: (authorizeInfo?: string) => Promise<AliPayUserInfo>

    /**
     * weChat 登陆
     */
    authorizeByWeChat: (authorizeInfo?: string) => Promise<WeChatUserInfo>

    /**
     * qq 登陆
     */
    authorizeByQQ: (authorizeInfo?: string) => Promise<any>
}

const MobShareSDK: MobSDKInterface = NativeModules.MobShareSDK;

const MobAuthenticateModule: MobAuthenticateInterface = {

    authorize: (platform: SocialType, authorizeInfo?: string) => {
        return MobShareSDK.authorize(platform, authorizeInfo);
    },

    authorizeByAliPay: (authorizeInfo?: string) => {
        return MobShareSDK.authorize(SocialType.ALI_PAY, authorizeInfo);
    },
    authorizeByQQ: (authorizeInfo?: string) => {
        return MobShareSDK.authorize(SocialType.QQ, authorizeInfo);
    },
    authorizeByWeChat: (authorizeInfo?: string) => {
        return MobShareSDK.authorize(SocialType.WE_CHAT, authorizeInfo);
    },
    doUserInfo: (platform: SocialType, account?: string) => {
        return MobShareSDK.doUserInfo(platform, account);
    }


};

export default MobAuthenticateModule;
