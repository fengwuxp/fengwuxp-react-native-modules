import {SocialType} from "./SocialType";

export interface MobSDKInterface {

    /**
     * 授权
     * @param platform
     */
    authorize: (platform: SocialType) => Promise<any>;

    /**
     * 分享
     * @param platform     分享的平台类型
     * @param shareParams  分享的参数
     */
    share: (platform: SocialType, shareParams: ShareParams) => Promise<void>;
}


export interface ShareParams {

    //标题
    title: string;

    //文本内容
    text?: string;

    //url
    url?: string;

    //图片
    imageUrl?: string;
    imageArray?: string[];

    titleUrl?: string;

    filePath?: string;

    musicUrl?: string;


    siteUrl?: string;

}
