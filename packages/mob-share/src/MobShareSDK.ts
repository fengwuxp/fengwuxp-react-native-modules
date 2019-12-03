import {SharePlatformType} from "./SharePlatformType";

export interface MobShareInterface {

    /**
     *
     * @param platform     分享的平台类型
     * @param shareParams  分享的参数
     */
    shareSignPlatform: (platform: SharePlatformType, shareParams: ShareParams) => Promise<void>;
}


export interface ShareParams {

    //标题
    title: string;

    //文本内容
    text?: string;

    //url
    url?: string;

    //图片
    image?: string;

}
