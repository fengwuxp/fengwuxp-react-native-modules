package com.fengwuxp.reactnative.version;


import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.fengwuxp.reactnative.version.helper.AppHelper;

import java.io.File;

import androidx.annotation.NonNull;

/**
 * 检查更新
 */
public class AppCheckForUpdaterModule extends ReactContextBaseJavaModule {


    private static final String NAME = "AppCheckUpdater";


    public AppCheckForUpdaterModule(ReactApplicationContext reactContext) {
        super(reactContext);

    }


    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * 安装app
     *
     * @param apkPath           apk 文件所在的路径
     * @param newestVersionCode 最新的版本号
     * @param promise
     */
    @ReactMethod
    public void installApp(String apkPath, int newestVersionCode, Promise promise) {


        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        if (AppHelper.checkAPKIsExists(reactApplicationContext, apkPath, newestVersionCode)) {

            promise.resolve(null);
            AppHelper.installApk(reactApplicationContext, apkPath);
        } else {
            promise.reject("-1", "文件不存在或版本号有误");
        }

    }


}
