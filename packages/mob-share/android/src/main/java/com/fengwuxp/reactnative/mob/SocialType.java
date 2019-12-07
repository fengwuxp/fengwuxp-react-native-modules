package com.fengwuxp.reactnative.mob;

import cn.sharesdk.alipay.friends.Alipay;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 第三方社区类型
 */
public enum SocialType {

    /**
     * 微信好友
     */
    WE_CHAT(Wechat.NAME),

    /**
     * 微信朋友圈
     */

    WE_CHAT_MOMENTS(WechatMoments.NAME),

    /**
     * 微信收藏
     */

    WE_CHAT_FAVORITE(WechatFavorite.NAME),

    /**
     * QQ
     */
    QQ(cn.sharesdk.tencent.qq.QQ.NAME),

    /**
     * QQ空间
     */
    QQ_ZONE(QZone.NAME),

    /**
     * 短信
     */
    SHORE_MESSAGE(ShortMessage.NAME),

    /**
     * 邮件分享
     */
    E_MAIL(Email.NAME),

    /**
     * 微博
     */

    SINA_WEI_BO(SinaWeibo.NAME),

    /**
     * 腾讯微博
     */

    TENCENT_WEB_BO(TencentWeibo.NAME),
    /**
     * 扫码
     */
    SCAN_QR_CODE(ShortMessage.NAME),

    /**
     * 链接
     */
    LINK(""),


    /**
     * 支付宝
     */
    ALI_PAY(Alipay.NAME);

    private String name;

    SocialType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
