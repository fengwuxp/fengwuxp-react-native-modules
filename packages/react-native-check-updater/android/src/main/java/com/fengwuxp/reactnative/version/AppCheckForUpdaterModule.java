package com.fengwuxp.reactnative.version;


import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadFailedListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

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

    @ReactMethod
    public void downloadAndInstallApp(ReadableMap map) {
        UIData versionBundle = UIData.create();
        versionBundle.setTitle(map.getString("title"));
        versionBundle.setDownloadUrl(map.getString("downloadUrl"));
        versionBundle.setContent(map.getString("content"));
        DownloadBuilder builder = AllenVersionChecker
                .getInstance()
                .downloadOnly(versionBundle);

        builder.download(getReactApplicationContext());
//        builder.setCustomDownloadFailedListener(new CustomDownloadFailedListener() {
//            @Override
//            public Dialog getCustomDownloadFailed(Context context, UIData versionBundle) {
//                promise.reject("", "download failed");
//
//                return
//            }
//        });
    }

//    public static String DESCRIPTION;
//
//    DownloadManager downManager;
//    Activity myActivity;
//
//
//    @ReactMethod
//    public void downloading(String url, String description) {
//        AppCheckForUpdaterModule.DESCRIPTION = description;
//
//        myActivity = getCurrentActivity();
//        downManager = (DownloadManager) myActivity.getSystemService(Context.DOWNLOAD_SERVICE);
//
//        Uri uri = Uri.parse(url);
//        DownloadManager.Request request = new Request(uri);
//
//        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
//
//        //设置通知栏标题
//        request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
//        request.setMimeType("application/vnd.android.package-archive");
//        request.setTitle("下载");
//
//        if (description == null || "".equals(description)) {
//            description = "目标apk正在下载";
//        }
//
//        request.setDescription(description);
//        request.setAllowedOverRoaming(false);
//
//        // 设置文件存放目录
//        request.setDestinationInExternalFilesDir(myActivity, Environment.DIRECTORY_DOWNLOADS, description);
//
//        long downloadId = downManager.enqueue(request);
//        SharedPreferences sPreferences = myActivity.getSharedPreferences("ggfw_download", 0);
//        sPreferences.edit().putLong("ggfw_download_apk", downloadId).apply();
//    }

}
