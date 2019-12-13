package com.fengwuxp.reactnative.mob.share;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.fengwuxp.reactnative.mob.SocialType;

import java.text.MessageFormat;
import java.util.Map;

import cn.sharesdk.alipay.friends.Alipay;
import cn.sharesdk.framework.InnerShareParams;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


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

    public void shareToSignPlatform(Activity activity, String platformName, Map<String, Object> params, PlatformActionListener listener) {

        SocialType shareType = SocialType.valueOf(platformName);
        try {
            Log.i(getClass().getSimpleName(), MessageFormat.format("share {0},{1}", platformName, params));

            Platform platform = ShareSDK.getPlatform(shareType.getName());
            if (platform == null) {
                return;
            }
            if (!platform.isClientValid()) {
                listener.onError(platform, APP_NOT_INSTALL, new RuntimeException("app not install"));
                return;
            }
            Platform.ShareParams innerShareParams = this.shareInnerFactory(shareType);
            if (innerShareParams == null) {
                listener.onError(platform, NO_SUPPORT_PLATFORM, new RuntimeException("not support platform"));
                return;
            }
            this.setShareParams(params, innerShareParams);
            innerShareParams.setActivity(activity);
            platform.setPlatformActionListener(listener);
            platform.share(innerShareParams);
        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(null, SHARE_ERROR, e);
        }
    }


    private void setShareParams(Map<String, Object> params, InnerShareParams innerShareParams) {
        if (params.containsKey("imageUrl")) {
            innerShareParams.setImageUrl((String) params.get("imageUrl"));
        }
        if (params.containsKey("site")) {
            innerShareParams.setTitleUrl((String) params.get("site"));
        }
        if (params.containsKey("siteUrl")) {
            innerShareParams.setTitleUrl((String) params.get("siteUrl"));
        }
        if (params.containsKey("musicUrl")) {
            innerShareParams.setMusicUrl((String) params.get("musicUrl"));
        }
        if (params.containsKey("address")) {
            innerShareParams.setAddress((String) params.get("address"));
        }
        if (params.containsKey("titleUrl")) {
            innerShareParams.setTitleUrl((String) params.get("titleUrl"));
        }
        if (params.containsKey("filePath")) {
            innerShareParams.setFilePath((String) params.get("filePath"));
        }
        if (params.containsKey("text")) {
            innerShareParams.setText((String) params.get("text"));
        }
        if (params.containsKey("title")) {
            innerShareParams.setTitle((String) params.get("title"));
        }

        if (params.containsKey("imageArray")) {
            innerShareParams.setImageArray((String[]) params.get("imageArray"));
        }
    }


    private Platform.ShareParams shareInnerFactory(SocialType shareType) {

        switch (shareType) {
            case QQ:
                return new QQ.ShareParams();
            case QQ_ZONE:
                return new QZone.ShareParams();
            case WE_CHAT:
                return new Wechat.ShareParams();
            case WE_CHAT_MOMENTS:
                return new WechatMoments.ShareParams();
            case WE_CHAT_FAVORITE:
                return new WechatFavorite.ShareParams();
            case E_MAIL:
                return new Email.ShareParams();
            case SCAN_QR_CODE:
            case SHORE_MESSAGE:
                return new ShortMessage.ShareParams();
            case SINA_WEI_BO:
                return new SinaWeibo.ShareParams();
            case TENCENT_WEB_BO:
                return new TencentWeibo.ShareParams();
            case ALI_PAY:
                return new Alipay.ShareParams();
            default:
                return null;
        }

    }

}
