package com.fengwuxp.reactnative.mob;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.mob.MobSDK;

import java.util.Map;

import static com.fengwuxp.reactnative.mob.ShareHelper.ERROR;

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
    public void shareSignPlatform(String platform, Map<String, Object> params, Promise promise) {
        if (TextUtils.isEmpty(platform)) {
            promise.reject(ERROR, "未指定分享平台");
        } else if (params == null || params.isEmpty()) {
            promise.reject(ERROR, "未指定分享内容");
        } else {
            ShareHelper.getInstance().shareToSignPlatform(mContext, platform, params, promise);
        }
    }


}
