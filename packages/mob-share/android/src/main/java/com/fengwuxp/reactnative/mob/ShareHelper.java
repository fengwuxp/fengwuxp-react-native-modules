package com.fengwuxp.reactnative.mob;

import android.content.Context;
import android.util.Log;

import java.text.MessageFormat;
import java.util.Map;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 创建者     ky-wrg
 * 创建时间   2017/12/26
 * 描述         ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ShareHelper {

    private static ShareHelper SIMPLE_HELPER;


    private ShareHelper() {
    }

    public synchronized static ShareHelper getInstance() {
        if (SIMPLE_HELPER == null) {
            SIMPLE_HELPER = new ShareHelper();
        }
        return SIMPLE_HELPER;
    }

    public void shareToSignPlatform(Context context, String platform, Map<String, Object> params, PlatformActionListener listener) {


        try {

            Log.i(getClass().getSimpleName(), "share " + platform + "," + params);

            OnekeyShare oks = null;

            if (ShareType.QQ.name().equalsIgnoreCase(platform))
                oks = toQQ(params);
            else if (ShareType.QQ_ZONE.name().equalsIgnoreCase(platform))
                oks = toQQZone(params);
            else if (ShareType.WE_CHAT.name().equalsIgnoreCase(platform))
                oks = toWeiXin(params);
            else if (ShareType.WE_CHAT_MOMENTS.name().equalsIgnoreCase(platform))
                oks = toWeiXinMoments(params);
            else if (ShareType.WE_CHAT_FAVORITE.name().equalsIgnoreCase(platform))
                oks = toWeiXinFavorite(params);
            else if (ShareType.E_MAIL.name().equalsIgnoreCase(platform))
                oks = toEmail(params);
            else if (ShareType.SCAN_QR_CODE.name().equalsIgnoreCase(platform))
                oks = toMessage(params);
            else if (ShareType.SINA_WEI_BO.name().equalsIgnoreCase(platform))
                oks = toSinaWeibo(params);
            else if (ShareType.TENCENT_WEB_BO.name().equalsIgnoreCase(platform))
                oks = toTencentWeibo(params);
            else {
                listener.onError(null, -1, new RuntimeException(MessageFormat.format("not support platform :{0}", platform)));
                return;
            }
            oks.setCallback(listener);
            oks.show(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * qq
     *
     * @param params
     * @return
     */
    private OnekeyShare toQQ(Map<String, Object> params) {
        OnekeyShare oks = new OnekeyShare();
        if (params.containsKey("imageUrl"))
            oks.setImageUrl((String) params.get("imageUrl"));
        if (params.containsKey("titleUrl"))
            oks.setTitleUrl((String) params.get("titleUrl"));
        if (params.containsKey("musicUrl"))
            oks.setMusicUrl((String) params.get("musicUrl"));
        if (params.containsKey("text"))
            oks.setText((String) params.get("text"));
        if (params.containsKey("title"))
            oks.setTitle((String) params.get("title"));
        if (params.containsKey("imageArray"))
            oks.setImageArray((String[]) params.get("imageArray"));
        oks.setPlatform(QQ.NAME);
        return oks;
    }

    /**
     * qq空间
     *
     * @param params
     * @return
     */
    private OnekeyShare toQQZone(Map<String, Object> params) {
        OnekeyShare oks = new OnekeyShare();
        if (params.containsKey("imageUrl"))
            oks.setImageUrl((String) params.get("imageUrl"));
        if (params.containsKey("site"))
            oks.setTitleUrl((String) params.get("site"));
        if (params.containsKey("siteUrl"))
            oks.setTitleUrl((String) params.get("siteUrl"));
        if (params.containsKey("titleUrl"))
            oks.setTitleUrl((String) params.get("titleUrl"));
        if (params.containsKey("filePath")) {
            oks.setFilePath((String) params.get("filePath"));
        }
        if (params.containsKey("text"))
            oks.setText((String) params.get("text"));
        if (params.containsKey("title"))
            oks.setTitle((String) params.get("title"));

        if (params.containsKey("imageArray"))
            oks.setImageArray((String[]) params.get("imageArray"));
        oks.setPlatform(QZone.NAME);
        return oks;
    }

    /**
     * 微信
     *
     * @param params
     * @return
     */
    private OnekeyShare toWeiXin(Map<String, Object> params) {
        OnekeyShare oks = new OnekeyShare();
        if (params.containsKey("imageUrl"))
            oks.setImageUrl((String) params.get("imageUrl"));
        if (params.containsKey("imagePath"))
            oks.setImagePath((String) params.get("imagePath"));
        if (params.containsKey("musicUrl"))
            oks.setMusicUrl((String) params.get("musicUrl"));
        if (params.containsKey("filePath")) {
            oks.setFilePath((String) params.get("filePath"));
        }
        if (params.containsKey("url"))
            oks.setUrl((String) params.get("url"));
        if (params.containsKey("imageArray"))
            oks.setImageArray((String[]) params.get("imageArray"));//ios 无
        if (params.containsKey("text"))
            oks.setText((String) params.get("text"));
        if (params.containsKey("title"))
            oks.setTitle((String) params.get("title"));
        oks.setPlatform(Wechat.NAME);
        return oks;
    }

    /**
     * 微信朋友圈
     *
     * @param params
     * @return
     */
    private OnekeyShare toWeiXinMoments(Map<String, Object> params) {
        OnekeyShare oks = toWeiXin(params);
        oks.setPlatform(WechatMoments.NAME);
        return oks;
    }

    /**
     * 微信收藏
     *
     * @param params
     * @return
     */
    private OnekeyShare toWeiXinFavorite(Map<String, Object> params) {
        OnekeyShare oks = toWeiXin(params);
        oks.setPlatform(WechatFavorite.NAME);
        return oks;
    }

    /**
     * 邮件
     *
     * @param params
     * @return
     */
    public OnekeyShare toEmail(Map<String, Object> params) {
        OnekeyShare oks = new OnekeyShare();
        if (params.containsKey("imageUrl"))
            oks.setImageUrl((String) params.get("imageUrl"));
        if (params.containsKey("imagePath"))
            oks.setImagePath((String) params.get("imagePath"));
        if (params.containsKey("filePath")) {
            oks.setFilePath((String) params.get("filePath"));
        }
        if (params.containsKey("address"))
            oks.setAddress((String) params.get("address"));
        if (params.containsKey("text"))
            oks.setText((String) params.get("text"));
        if (params.containsKey("title"))
            oks.setTitle((String) params.get("title"));
        oks.setPlatform(Email.NAME);

        return oks;
    }

    /**
     * 手机信息
     *
     * @param params
     * @return
     */
    public OnekeyShare toMessage(Map<String, Object> params) {
        OnekeyShare oks = toEmail(params);
        oks.setPlatform(cn.sharesdk.system.text.ShortMessage.NAME);
        return oks;
    }

    /**
     * 新浪微博
     *
     * @param params
     * @return
     */
    public OnekeyShare toSinaWeibo(Map<String, Object> params) {
        OnekeyShare oks = new OnekeyShare();
        if (params.containsKey("imageUrl"))
            oks.setImageUrl((String) params.get("imageUrl"));
        if (params.containsKey("imagePath"))
            oks.setImagePath((String) params.get("imagePath"));
        if (params.containsKey("filePath")) {
            oks.setFilePath((String) params.get("filePath"));
        }
        if (params.containsKey("text"))
            oks.setText((String) params.get("text"));//要分享链接需要链接添加在text里分享
        oks.setPlatform(SinaWeibo.NAME);

        return oks;
    }

    /**
     * 腾讯微博
     *
     * @param params
     * @return
     */
    public OnekeyShare toTencentWeibo(Map<String, Object> params) {
        OnekeyShare oks = new OnekeyShare();
        if (params.containsKey("imageUrl"))
            oks.setImageUrl((String) params.get("imageUrl"));
        if (params.containsKey("imagePath"))
            oks.setImagePath((String) params.get("imagePath"));
        if (params.containsKey("latitude"))
            oks.setLatitude((Float) params.get("latitude"));
        if (params.containsKey("longitude"))
            oks.setLongitude((Float) params.get("longitude"));
        if (params.containsKey("imageArray"))
            oks.setImageArray((String[]) params.get("imageArray"));
        if (params.containsKey("text"))
            oks.setText((String) params.get("text"));//要分享链接需要链接添加在text里分享
        oks.setPlatform(TencentWeibo.NAME);

        return oks;
    }


}
