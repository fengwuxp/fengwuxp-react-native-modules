package com.fengwuxp.reactnative.mob;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.mob.MobSDK;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

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
        return "MobShare";
    }

    @Override
    public void initialize() {
        super.initialize();
        //初始化Mob
        MobSDK.init(mContext);
    }


    /**
     * 分享单个平台
     *
     * @param platform
     * @param params
     * @param promise
     */
    @ReactMethod
    public void shareSignPlatform(String platform, ReadableMap params, Promise promise) {
        this.checkCalled(promise);
        if (TextUtils.isEmpty(platform)) {
            promise.reject(ShareResultStatus.FAILURE.name(), "未指定分享平台");
        } else if (params == null || !params.getEntryIterator().hasNext()) {
            promise.reject(ShareResultStatus.FAILURE.name(), "未指定分享内容");
        }

        // 复制参数
        HashMap<String, Object> map = new HashMap<>();
        Iterator<Map.Entry<String, Object>> entryIterator = Objects.requireNonNull(params).getEntryIterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, Object> next = entryIterator.next();
            map.put(next.getKey(), next.getValue());
        }

        ShareHelper.getInstance().shareToSignPlatform(mContext, platform, map, new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                promise.resolve(ShareResultStatus.SUCCESS.name());
            }

            @Override
            public void onError(Platform platform, int code, Throwable throwable) {
                promise.reject(ShareResultStatus.FAILURE.name(), MessageFormat.format("分享失败,error code: {0}", code));
            }

            @Override
            public void onCancel(Platform platform, int i) {
                promise.reject(ShareResultStatus.CANCEL.name(), "取消分享");
            }
        });
    }

    private void checkCalled(Promise promise) {
        if (promise == null) {
            throw new RuntimeException("Must be called using a promise");
        }
    }

}
