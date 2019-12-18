import {AppVersionInfo} from "./AppVersionInfo";
import {PlatformType} from "./PlatformType";
import DeviceInfo from 'react-native-device-info';
import {LocalVersionInfo} from "./LocalVersionInfo";
import {Platform} from "react-native";
import Downloader from "./dialog/Downloader";


interface CheckAppVersionReq {

    appCode: string;

    platformType?: PlatformType;

    currVersionCode?: number;
}

interface AppService {

    checkAppVersion: (req: CheckAppVersionReq) => Promise<AppVersionInfo>;
}


/**
 * 检查更新android 版本
 */
export default class AppVersionChecker {


    private appService: AppService;


    constructor(appService: AppService) {
        this.appService = appService;
    }

    /**
     * 检测更新(仅支持安卓端)
     */
    checkApkUpdate = (): Promise<void> => {
        if (Platform.OS !== "android") {
            // "not support checker update"
            return Promise.reject();
        }

        return new Promise((resolve, reject) => {
            this.isNewestVersion().then((data) => {
                Downloader.show({
                    appVersionInfo: data
                });
                resolve();
            })

        });

    };

    /**
     * 是否最新版本
     */
    isNewestVersion = (): Promise<AppVersionInfo> => {

        return this.getLocalVersion().then(({versionCode}) => {
            return this.getAppVersionByServer({
                appCode: null,
                currVersionCode: versionCode
            }).catch(({message}) => {

                return Promise.reject(message);
            });
        })
    };

    /**
     * 获取本地版本
     */
    getLocalVersion = (): Promise<LocalVersionInfo> => {

        const readableVersion = DeviceInfo.getReadableVersion();
        const versionCode = readableVersion.split(".")[2];
        return Promise.resolve({
            versionCode: parseInt(versionCode),
            versionName: readableVersion,
            packageName: DeviceInfo.getBundleId()
        })
    };

    /**
     * 从服务端获取版本号
     * @param req
     */
    private getAppVersionByServer = (req: CheckAppVersionReq): Promise<AppVersionInfo> => {

        return this.appService.checkAppVersion(req);
    }
}
