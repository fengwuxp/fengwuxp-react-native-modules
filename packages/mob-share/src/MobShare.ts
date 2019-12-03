import {NativeModules} from 'react-native';
import {MobShareInterface, ShareParams} from "./MobShareSDK";
import {SharePlatformType} from "./SharePlatformType";


export interface MobShareModule extends MobShareInterface {

    shareByWeChat: (shareParams: ShareParams) => Promise<void>;

    shareByWeChatMoments: (shareParams: ShareParams) => Promise<void>;

    shareByWeCHatFavorite: (shareParams: ShareParams) => Promise<void>;

    shareByQQ: (shareParams: ShareParams) => Promise<void>;

    shareByQqZone: (shareParams: ShareParams) => Promise<void>;

    shareByTencentWeiBo: (shareParams: ShareParams) => Promise<void>;

    shareBySinaWeiBo: (shareParams: ShareParams) => Promise<void>;

    shareByShortMessage: (shareParams: ShareParams) => Promise<void>;
}

const MobShare: MobShareInterface = NativeModules.MobShare;

const mobShareModule: MobShareModule = {
    shareSignPlatform: (sharePlatformType: SharePlatformType, shareParams: ShareParams) => {
        return MobShare.shareSignPlatform(sharePlatformType, shareParams);
    },

    shareByQQ: (shareParams: ShareParams) => {
        return MobShare.shareSignPlatform(SharePlatformType.QQ, shareParams);
    },

    shareByQqZone: (shareParams: ShareParams) => {
        return MobShare.shareSignPlatform(SharePlatformType.QQ_ZONE, shareParams);
    },

    shareByShortMessage: (shareParams: ShareParams) => {
        return MobShare.shareSignPlatform(SharePlatformType.SHORT_MESSAGE, shareParams);
    },

    shareBySinaWeiBo: (shareParams: ShareParams) => {
        return MobShare.shareSignPlatform(SharePlatformType.SINA_WEI_BO, shareParams);
    },

    shareByTencentWeiBo: (shareParams: ShareParams) => {
        return MobShare.shareSignPlatform(SharePlatformType.TENCENT_WEB_BO, shareParams);
    },

    shareByWeCHatFavorite: (shareParams: ShareParams) => {
        return MobShare.shareSignPlatform(SharePlatformType.WE_CHAT_FAVORITE, shareParams);
    },

    shareByWeChat: (shareParams: ShareParams) => {
        return MobShare.shareSignPlatform(SharePlatformType.WE_CHAT, shareParams);
    },

    shareByWeChatMoments: (shareParams: ShareParams) => {
        return MobShare.shareSignPlatform(SharePlatformType.WE_CHAT_MOMENTS, shareParams);
    },

};

export default mobShareModule;
