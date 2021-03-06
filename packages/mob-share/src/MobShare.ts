import {NativeModules, Linking, Platform} from 'react-native';
import {MobSDKInterface, ShareParams} from "./MobShareSDK";
import {SocialType} from "./SocialType";
import {SOCIAL_TYPE_MAP_APP_SCHEME} from "./AppURLSchemeConstant";
import {MobShareSdkIosConstantValue} from "./MobShareSdkIosConstantValue";

const IS_IOS = Platform.OS === 'ios';
let initShareSdk = false;

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
        if (IS_IOS && !initShareSdk) {
            initShareSdk = true;
            // ios 环境初始化一下sdk
            MobShareSDK.setup();
        }

        return Linking.canOpenURL(SOCIAL_TYPE_MAP_APP_SCHEME[sharePlatformType]).then((isInstall) => {
            if (!isInstall) {
                return Promise.reject({message: "应用未安装"});
            }
            if (IS_IOS) {
                sharePlatformType = MobShareSdkIosConstantValue[sharePlatformType].toString();
            }

            return MobShareSDK.share(sharePlatformType, shareParams);
        })


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
