package com.fengwuxp.reactnative.mob.authenticate;

import android.content.Context;

import com.fengwuxp.reactnative.mob.SocialType;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * 第三方鉴权helper
 */
public final class AuthenticateHelper {


    /**
     * 鉴权
     * @param context
     * @param platformName
     * @param listener
     */
    public static void authorize(Context context, String platformName, PlatformActionListener listener) {
        SocialType socialType = SocialType.valueOf(platformName);
        Platform platform = ShareSDK.getPlatform(socialType.getName());
        if (!platform.isClientValid()) {
            listener.onError(platform, -1, new RuntimeException("app not install"));
            return;
        }
        platform.setPlatformActionListener(listener);
        platform.showUser(null);

    }

}
