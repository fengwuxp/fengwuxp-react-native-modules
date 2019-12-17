export type DownloadAndInstallApp = {
    title: string;
    content: string,
    downloadUrl: string
};

export interface AppCheckForUpdaterModuleSDK {

    /**
     * 下载并安装 app
     * @param options
     */
    downloadAndInstallApp: (options: DownloadAndInstallApp) => void;
}
