package com.fengwuxp.reactnative.mob.share;

import android.content.Context;
import android.util.Log;

import com.fengwuxp.reactnative.mob.SocialType;

import java.text.MessageFormat;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


public final class ShareHelper {

    private static ShareHelper SIMPLE_HELPER;

    private static int SHARE_ERROR = -1;

    private static int NO_SUPPORT_PLATFORM = -2;

    private static int APP_NOT_INSTALL = -2;

    private ShareHelper() {
    }

    public synchronized static ShareHelper getInstance() {
        if (SIMPLE_HELPER == null) {
            SIMPLE_HELPER = new ShareHelper();
        }
        return SIMPLE_HELPER;
    }

    public void shareToSignPlatform(Context context, String platformName, Map<String, Object> params, PlatformActionListener listener) {

        SocialType shareType = SocialType.valueOf(platformName);
        try {
            Log.i(getClass().getSimpleName(), MessageFormat.format("share {0},{1}", platformName, params));
            OnekeyShare oks = new OnekeyShare();
            setOnekeyShareParams(params, oks);
            oks.setPlatform(shareType.getName());
            Platform platform = ShareSDK.getPlatform(shareType.getName());
            if (platform == null) {
                return;
            }
            if (platform.isClientValid()) {
                listener.onError(platform, APP_NOT_INSTALL, new RuntimeException("app not install"));
                return;
            }
            oks.setCallback(listener);
            oks.show(context);
        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(null, SHARE_ERROR, e);
        }
    }

//    private Platform getSharePlatform(SocialType shareType, OnekeyShare oks) {
//        Platform platform = ShareSDK.getPlatform(shareType.getName());
//        oks.setPlatform(shareType.getName());
//
//        switch (shareType) {
//            case QQ:
//                oks.setPlatform(QQ.NAME);
//                platform = ShareSDK.getPlatform(QQ.NAME);
//                break;
//            case QQ_ZONE:
//                oks.setPlatform(QZone.NAME);
//                platform = ShareSDK.getPlatform(QZone.NAME);
//                break;
//            case WE_CHAT:
//                oks.setPlatform(Wechat.NAME);
//                platform = ShareSDK.getPlatform(Wechat.NAME);
//                break;
//            case WE_CHAT_MOMENTS:
//                oks.setPlatform(WechatMoments.NAME);
//                platform = ShareSDK.getPlatform(WechatMoments.NAME);
//                break;
//            case WE_CHAT_FAVORITE:
//                oks.setPlatform(WechatFavorite.NAME);
//                platform = ShareSDK.getPlatform(WechatFavorite.NAME);
//                break;
//            case E_MAIL:
//                oks.setPlatform(Email.NAME);
//                platform = ShareSDK.getPlatform(Email.NAME);
//                break;
//            case SCAN_QR_CODE:
//                oks.setPlatform(ShortMessage.NAME);
//                platform = ShareSDK.getPlatform(ShortMessage.NAME);
//                break;
//            case SINA_WEI_BO:
//                oks.setPlatform(SinaWeibo.NAME);
//                platform = ShareSDK.getPlatform(SinaWeibo.NAME);
//                break;
//            case TENCENT_WEB_BO:
//                oks.setPlatform(TencentWeibo.NAME);
//                platform = ShareSDK.getPlatform(TencentWeibo.NAME);
//                break;
//            default:
//                listener.onError(platform, NO_SUPPORT_PLATFORM, new RuntimeException(MessageFormat.format("not support platform :{0}", platformName)));
//        }
//        return platform;
//    }

    private void setOnekeyShareParams(Map<String, Object> params, OnekeyShare oks) {
        if (params.containsKey("imageUrl")) {
            oks.setImageUrl((String) params.get("imageUrl"));
        }
        if (params.containsKey("site")) {
            oks.setTitleUrl((String) params.get("site"));
        }
        if (params.containsKey("siteUrl")) {
            oks.setTitleUrl((String) params.get("siteUrl"));
        }
        if (params.containsKey("musicUrl")) {
            oks.setMusicUrl((String) params.get("musicUrl"));
        }
        if (params.containsKey("address")) {
            oks.setAddress((String) params.get("address"));
        }
        if (params.containsKey("titleUrl")) {
            oks.setTitleUrl((String) params.get("titleUrl"));
        }
        if (params.containsKey("filePath")) {
            oks.setFilePath((String) params.get("filePath"));
        }
        if (params.containsKey("text")) {
            oks.setText((String) params.get("text"));
        }
        if (params.containsKey("title")) {
            oks.setTitle((String) params.get("title"));
        }

        if (params.containsKey("imageArray")) {
            oks.setImageArray((String[]) params.get("imageArray"));
        }
    }

    /**
     //     * qq
     //     *
     //     * @param params
     //     * @param listener
     //     * @return
     //     */
//    private OnekeyShare toQQ(Map<String, Object> params, PlatformActionListener listener) {
//        Platform qqPlatform = ShareSDK.getPlatform(QQ.NAME);
//
//        if (!qqPlatform.isClientValid()) {
//
//        }
//
//        OnekeyShare oks = new OnekeyShare();
//        if (params.containsKey("imageUrl"))
//            oks.setImageUrl((String) params.get("imageUrl"));
//        if (params.containsKey("titleUrl"))
//            oks.setTitleUrl((String) params.get("titleUrl"));
//        if (params.containsKey("musicUrl"))
//            oks.setMusicUrl((String) params.get("musicUrl"));
//        if (params.containsKey("text"))
//            oks.setText((String) params.get("text"));
//        if (params.containsKey("title"))
//            oks.setTitle((String) params.get("title"));
//        if (params.containsKey("imageArray"))
//            oks.setImageArray((String[]) params.get("imageArray"));
//        oks.setPlatform(QQ.NAME);
//        return oks;
//    }
//
//    /**
//     * qq空间
//     *
//     * @param params
//     * @return
//     */
//    private OnekeyShare toQQZone(Map<String, Object> params) {
//        OnekeyShare oks = new OnekeyShare();
//        if (params.containsKey("imageUrl"))
//            oks.setImageUrl((String) params.get("imageUrl"));
//        if (params.containsKey("site"))
//            oks.setTitleUrl((String) params.get("site"));
//        if (params.containsKey("siteUrl"))
//            oks.setTitleUrl((String) params.get("siteUrl"));
//        if (params.containsKey("titleUrl"))
//            oks.setTitleUrl((String) params.get("titleUrl"));
//        if (params.containsKey("filePath")) {
//            oks.setFilePath((String) params.get("filePath"));
//        }
//        if (params.containsKey("text"))
//            oks.setText((String) params.get("text"));
//        if (params.containsKey("title"))
//            oks.setTitle((String) params.get("title"));
//
//        if (params.containsKey("imageArray"))
//            oks.setImageArray((String[]) params.get("imageArray"));
//        oks.setPlatform(QZone.NAME);
//        return oks;
//    }
//
//    /**
//     * 微信
//     *
//     * @param params
//     * @return
//     */
//    private OnekeyShare toWeiXin(Map<String, Object> params) {
//        OnekeyShare oks = new OnekeyShare();
//        if (params.containsKey("imageUrl"))
//            oks.setImageUrl((String) params.get("imageUrl"));
//        if (params.containsKey("imagePath"))
//            oks.setImagePath((String) params.get("imagePath"));
//        if (params.containsKey("musicUrl"))
//            oks.setMusicUrl((String) params.get("musicUrl"));
//        if (params.containsKey("filePath")) {
//            oks.setFilePath((String) params.get("filePath"));
//        }
//        if (params.containsKey("url"))
//            oks.setUrl((String) params.get("url"));
//        if (params.containsKey("imageArray"))
//            oks.setImageArray((String[]) params.get("imageArray"));//ios 无
//        if (params.containsKey("text"))
//            oks.setText((String) params.get("text"));
//        if (params.containsKey("title"))
//            oks.setTitle((String) params.get("title"));
//        oks.setPlatform(Wechat.NAME);
//        return oks;
//    }
//
//    /**
//     * 微信朋友圈
//     *
//     * @param params
//     * @return
//     */
//    private OnekeyShare toWeiXinMoments(Map<String, Object> params) {
//        OnekeyShare oks = toWeiXin(params);
//        oks.setPlatform(WechatMoments.NAME);
//        return oks;
//    }
//
//    /**
//     * 微信收藏
//     *
//     * @param params
//     * @return
//     */
//    private OnekeyShare toWeiXinFavorite(Map<String, Object> params) {
//        OnekeyShare oks = toWeiXin(params);
//        oks.setPlatform(WechatFavorite.NAME);
//        return oks;
//    }
//
//    /**
//     * 邮件
//     *
//     * @param params
//     * @return
//     */
//    public OnekeyShare toEmail(Map<String, Object> params) {
//        OnekeyShare oks = new OnekeyShare();
//        if (params.containsKey("imageUrl"))
//            oks.setImageUrl((String) params.get("imageUrl"));
//        if (params.containsKey("imagePath"))
//            oks.setImagePath((String) params.get("imagePath"));
//        if (params.containsKey("filePath")) {
//            oks.setFilePath((String) params.get("filePath"));
//        }
//        if (params.containsKey("address"))
//            oks.setAddress((String) params.get("address"));
//        if (params.containsKey("text"))
//            oks.setText((String) params.get("text"));
//        if (params.containsKey("title"))
//            oks.setTitle((String) params.get("title"));
//        oks.setPlatform(Email.NAME);
//
//        return oks;
//    }
//
//    /**
//     * 手机信息
//     *
//     * @param params
//     * @return
//     */
//    public OnekeyShare toMessage(Map<String, Object> params) {
//        OnekeyShare oks = toEmail(params);
//        oks.setPlatform(cn.sharesdk.system.text.ShortMessage.NAME);
//        return oks;
//    }
//
//    /**
//     * 新浪微博
//     *
//     * @param params
//     * @return
//     */
//    public OnekeyShare toSinaWeibo(Map<String, Object> params) {
//        OnekeyShare oks = new OnekeyShare();
//        if (params.containsKey("imageUrl"))
//            oks.setImageUrl((String) params.get("imageUrl"));
//        if (params.containsKey("imagePath"))
//            oks.setImagePath((String) params.get("imagePath"));
//        if (params.containsKey("filePath")) {
//            oks.setFilePath((String) params.get("filePath"));
//        }
//        if (params.containsKey("text"))
//            oks.setText((String) params.get("text"));//要分享链接需要链接添加在text里分享
//        oks.setPlatform(SinaWeibo.NAME);
//
//        return oks;
//    }
//
//    /**
//     * 腾讯微博
//     *
//     * @param params
//     * @return
//     */
//    public OnekeyShare toTencentWeibo(Map<String, Object> params) {
//        OnekeyShare oks = new OnekeyShare();
//        if (params.containsKey("imageUrl"))
//            oks.setImageUrl((String) params.get("imageUrl"));
//        if (params.containsKey("imagePath"))
//            oks.setImagePath((String) params.get("imagePath"));
//        if (params.containsKey("latitude"))
//            oks.setLatitude((Float) params.get("latitude"));
//        if (params.containsKey("longitude"))
//            oks.setLongitude((Float) params.get("longitude"));
//        if (params.containsKey("imageArray"))
//            oks.setImageArray((String[]) params.get("imageArray"));
//        if (params.containsKey("text"))
//            oks.setText((String) params.get("text"));//要分享链接需要链接添加在text里分享
//        oks.setPlatform(TencentWeibo.NAME);
//
//        return oks;
//    }


}