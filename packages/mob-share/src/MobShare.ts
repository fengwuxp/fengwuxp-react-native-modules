import {NativeModules} from 'react-native';
import {MobSDKInterface, ShareParams} from "./MobShareSDK";
import {SocialType} from "./SocialType";


export interface MobShareModuleInterface {


    /**
     *
     * @param platform     分享的平台类型
     * @param shareParams  分享的参数
     */
    share: (platform: SocialType, shareParams: ShareParams) => Promise<void>;

    shareByWeChat: (shareParams: ShareParams) => Promise<void>;

    shareByWeChatMoments: (shareParams: ShareParams) => Promise<void>;

    shareByWeCHatFavorite: (shareParams: ShareParams) => Promise<void>;

    shareByQQ: (shareParams: ShareParams) => Promise<void>;

    shareByQqZone: (shareParams: ShareParams) => Promise<void>;

    shareByTencentWeiBo: (shareParams: ShareParams) => Promise<void>;

    shareBySinaWeiBo: (shareParams: ShareParams) => Promise<void>;

    shareByShortMessage: (shareParams: ShareParams) => Promise<void>;

    shareByAliPay: (shareParams: ShareParams) => Promise<void>;
}

const MobShareSDK: MobSDKInterface = NativeModules.MobShareSDK;

const MobShareModule: MobShareModuleInterface = {
    share: (sharePlatformType: SocialType, shareParams: ShareParams) => {
        return MobShareSDK.share(sharePlatformType, shareParams);
    },

    shareByQQ: (shareParams: ShareParams) => {
        return MobShareSDK.share(SocialType.QQ, shareParams);
    },

    shareByQqZone: (shareParams: ShareParams) => {
        return MobShareSDK.share(SocialType.QQ_ZONE, shareParams);
    },

    shareByShortMessage: (shareParams: ShareParams) => {
        return MobShareSDK.share(SocialType.SHORT_MESSAGE, shareParams);
    },

    shareBySinaWeiBo: (shareParams: ShareParams) => {
        return MobShareSDK.share(SocialType.SINA_WEI_BO, shareParams);
    },

    shareByTencentWeiBo: (shareParams: ShareParams) => {
        return MobShareSDK.share(SocialType.TENCENT_WEB_BO, shareParams);
    },

    shareByWeCHatFavorite: (shareParams: ShareParams) => {
        return MobShareSDK.share(SocialType.WE_CHAT_FAVORITE, shareParams);
    },

    shareByWeChat: (shareParams: ShareParams) => {
        return MobShareSDK.share(SocialType.WE_CHAT, shareParams);
    },

    shareByWeChatMoments: (shareParams: ShareParams) => {
        return MobShareSDK.share(SocialType.WE_CHAT_MOMENTS, shareParams);
    },

    shareByAliPay: (shareParams: ShareParams) => {
        return MobShareSDK.share(SocialType.ALI_PAY, shareParams);
    },
};

export default MobShareModule;
