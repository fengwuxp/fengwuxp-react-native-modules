package com.fengwuxp.reactnative.mob;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.fengwuxp.reactnative.mob.authenticate.AuthenticateHelper;
import com.fengwuxp.reactnative.mob.share.ShareHelper;
import com.mob.MobSDK;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;


/**
 * mob share module
 * <p>
 * https://www.mob.com/mobService/sharesdk
 */
public class MobShareModule extends ReactContextBaseJavaModule {

    private Context mContext;


    public MobShareModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.mContext = reactContext.getApplicationContext();
    }

    @Override
    public String getName() {
        return "MobShareSDK";
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

        AuthenticateHelper.authorize(mContext, platform, new ShareMobPlatformActionListener(promise, false));
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
//        this.checkCalled(promise);
        if (TextUtils.isEmpty(platform)) {
            promise.reject(ShareSDKResultStatus.FAILURE.name(), "未指定分享平台");
            return;
        } else if (params == null || !params.getEntryIterator().hasNext()) {
            promise.reject(ShareSDKResultStatus.FAILURE.name(), "未指定分享内容");
            return;
        }

        // 复制参数
        HashMap<String, Object> map = new HashMap<>();
        Iterator<Map.Entry<String, Object>> entryIterator = Objects.requireNonNull(params).getEntryIterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, Object> next = entryIterator.next();
            Object value = next.getValue();
            map.put(next.getKey(), value);
        }

        ShareHelper.getInstance().shareToSignPlatform(getCurrentActivity(), platform, map, new ShareMobPlatformActionListener(promise, true));
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
