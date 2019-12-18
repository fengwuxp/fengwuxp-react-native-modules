package com.fengwuxp.reactnative.version.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.fengwuxp.reactnative.version.VersionFileProvider;

import java.io.File;


public final class AppHelper {

    private static final String TAG = "AppHelper";

    private AppHelper() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 安装 App
     *
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(apkPath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = VersionFileProvider.getUriForFile(context, context.getPackageName() + ".versionProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri,
                "application/vnd.android.package-archive");
        context.startActivity(intent);

    }

    /**
     * @param context
     * @param downloadPath
     * @param newestVersionCode 开发者认为的最新的版本号
     * @return
     */
    public static boolean checkAPKIsExists(Context context, String downloadPath, Integer newestVersionCode) {
        File file = new File(downloadPath);
        boolean result = false;
        if (file.exists()) {
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(downloadPath,
                        PackageManager.GET_ACTIVITIES);
                //判断安装包存在并且包名一样并且版本号不一样
//                int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
//                Log.i(TAG, "本地安装包版本号：" + info.versionCode + "\n 当前app版本号：" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
//                if (context.getPackageName().equalsIgnoreCase(info.packageName) && versionCode != info.versionCode) {
//                    //判断开发者传入的最新版本号是否大于缓存包的版本号，大于那么相当于没有缓存
//                    result = newestVersionCode == null || info.versionCode >= newestVersionCode;
//                }
                result = true;
            } catch (Exception e) {
                result = false;
            }
        } else {
            Log.e(TAG, "安装文件不存在,下载目录: " + downloadPath);
        }
        return result;

    }

}
