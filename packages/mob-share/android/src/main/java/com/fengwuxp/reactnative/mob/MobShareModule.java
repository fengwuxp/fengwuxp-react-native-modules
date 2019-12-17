package com.fengwuxp.reactnative.mob;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.fengwuxp.sharesdk.PlatformAuthorizeUserInfoManager;
import com.fengwuxp.sharesdk.PlatformShareManager;
import com.fengwuxp.sharesdk.SocialType;
import com.mob.MobSDK;

import java.util.HashMap;

import androidx.annotation.RequiresApi;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;


/**
 * mob share module
 * <p>
 * https://www.mob.com/mobService/sharesdk
 */
public class MobShareModule extends ReactContextBaseJavaModule {


    private static final String NAME = "MobShareSDK";

    private Context mContext;

    private PlatformShareManager platformShareManager = new PlatformShareManager();
    private PlatformAuthorizeUserInfoManager platformAuthorizeUserInfoManager = new PlatformAuthorizeUserInfoManager();


    public MobShareModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.mContext = reactContext.getApplicationContext();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void initialize() {
        super.initialize();
        //初始化Mob
        MobSDK.init(mContext);
    }


    /**
     * 授权
     *
     * @param platform {@code SocialType}
     * @param promise
     */
    @ReactMethod
    public void authorize(String platform, Promise promise) {
        if (TextUtils.isEmpty(platform)) {
            promise.reject(ShareSDKResultStatus.FAILURE.name(), "未指定授权平台");
            return;
        }
        SocialType shareType = SocialType.valueOf(platform);
        platformAuthorizeUserInfoManager.doAuthorize(shareType, getCurrentActivity(), new ShareMobPlatformActionListener(promise, false));
    }

    /**
     * 分享单个平台
     *
     * @param platform {@code SocialType}
     * @param params   分享参数
     * @param promise
     */
    @ReactMethod
    public void share(String platform, ReadableMap params, Promise promise) {
        if (TextUtils.isEmpty(platform)) {
            promise.reject(ShareSDKResultStatus.FAILURE.name(), "未指定分享平台");
            return;
        } else if (params == null || !params.getEntryIterator().hasNext()) {
            promise.reject(ShareSDKResultStatus.FAILURE.name(), "未指定分享内容");
            return;
        }
        SocialType shareType = SocialType.valueOf(platform);
        String title = params.getString("title");
        String text = params.getString("text");
        String imageUrl = params.getString("imageUrl");
        String url = params.getString("url");
        platformShareManager.shareWebPage(shareType, title, text, imageUrl, url, new ShareMobPlatformActionListener(promise, true));

        ;
    }

    private Platform.ShareParams getShareParams(ReadableMap params) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setText(params.getString("text"));
        shareParams.setTitle(params.getString("title"));
        shareParams.setImageUrl(params.getString("imageUrl"));
        shareParams.setTitleUrl(params.getString("titleUrl"));
        shareParams.setMusicUrl(params.getString("musicUrl"));
        shareParams.setAddress(params.getString("address"));
        shareParams.setFilePath(params.getString("filePath"));
        ReadableArray imageArray = params.getArray("imageArray");
        if (imageArray != null) {
            shareParams.setImageArray(imageArray.toArrayList().toArray(new String[0]));
        }
        return shareParams;

    }


    public static class ShareMobPlatformActionListener implements PlatformActionListener {

        private final Promise promise;

        private final boolean isShare;

        public ShareMobPlatformActionListener(Promise promise, boolean isShare) {
            this.promise = promise;
            this.isShare = isShare;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (this.isShare) {
                promise.resolve(ShareSDKResultStatus.SUCCESS.name());
            } else {
                // 授权
                WritableMap writableMap = new WritableNativeMap();
                hashMap.forEach((key, value) -> {
                    if (value != null) {
                        writableMap.putString(key, value.toString());
                    }
                });
                promise.resolve(writableMap);
            }
        }

        @Override
        public void onError(Platform platform, int code, Throwable throwable) {
            WritableMap writableMap = new WritableNativeMap();
            writableMap.putString("message", throwable.getMessage());
            writableMap.putInt("code", code);
            if (platform != null) {
                writableMap.putString("platform", platform.getName());
            }
            writableMap.putString("status", ShareSDKResultStatus.FAILURE.name());
            promise.reject(ShareSDKResultStatus.FAILURE.name(), writableMap);
        }

        @Override
        public void onCancel(Platform platform, int i) {
            WritableMap writableMap = new WritableNativeMap();
            writableMap.putString("status", ShareSDKResultStatus.CANCEL.name());
            promise.reject(ShareSDKResultStatus.CANCEL.name(), writableMap);
        }
    }

}
