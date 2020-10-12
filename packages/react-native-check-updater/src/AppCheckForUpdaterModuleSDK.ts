

export interface AppCheckForUpdaterModuleSDK {

    /**
     * 安装 app
     * @param apkFilePath
     * @param newestVersionCode 最新的版本号
     */
    installApp: (apkFilePath: string, newestVersionCode: number) => Promise<void>;
}
