import {SocialType} from "./SocialType";

export const QQ = "mqq://";
export const 微信 = "weixin://";
export const 腾讯微博 = "tencentweibo://";
export const 新浪微博 = "weibo://";
// export const 淘宝 = "taobao://";
export const 支付宝 = "支付宝://";
// export const 美团 = "imeituan://";
// export const 知乎 = "zhihu://";
// export const 优酷 = "youku://";


export const SOCIAL_TYPE_MAP_APP_SCHEME = {
    [SocialType.QQ]: QQ,
    [SocialType.QQ_ZONE]: QQ,
    [SocialType.TENCENT_WEB_BO]: 腾讯微博,
    [SocialType.WE_CHAT_MOMENTS]: 微信,
    [SocialType.WE_CHAT]: 微信,
    [SocialType.WE_CHAT_FAVORITE]: 微信,
    [SocialType.SINA_WEI_BO]: 新浪微博,
    [SocialType.ALI_PAY]: 支付宝,
};
