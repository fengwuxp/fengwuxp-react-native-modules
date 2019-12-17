import {AppVersionInfo} from "../AppVersionInfo";
import {View, Text, Image, ImageSourcePropType, Button, Linking, NativeModules, BackHandler} from "react-native";
import React, {PureComponent, useState} from "react";
import RootSiblings from 'react-native-root-siblings'
import {AppCheckForUpdaterModuleSDK} from "../AppCheckForUpdaterModuleSDK";


const AppCheckUpdater: AppCheckForUpdaterModuleSDK = NativeModules.AppCheckUpdater;

export interface DownloadDialogProps {

    appVersionInfo: AppVersionInfo;

    backgroundIcon: ImageSourcePropType;

    title?: string;
}

export const DownloadDialog = (props: DownloadDialogProps) => {

    const {backgroundIcon, title, appVersionInfo} = props;

    const [downloadStatus, seDownloadStatus] = useState(false);

    if (downloadStatus) {

        return
    }

    return <View>
        <Image source={backgroundIcon}/>
        <Text>{title || "检查更新"}</Text>
        <View>
            <Button title={"暂不升级"} onPress={() => {
                if (appVersionInfo.forcibly) {
                    BackHandler.exitApp();
                } else {
                    Downloader.hide();
                }

            }}/>
            <Button title={"立即升级"} onPress={() => {
                seDownloadStatus(true);

                if (!appVersionInfo.apk) {

                    const updatePageUrl = appVersionInfo.updatePageUrl;
                    Linking.canOpenURL(updatePageUrl).then(supported => {
                        if (!supported) {
                            console.warn('Can\'t handle url: ' + updatePageUrl);
                        } else {
                            return Linking.openURL(updatePageUrl);
                        }
                    }).catch(err => console.error('An error occurred', err));
                    return;
                }

                AppCheckUpdater.downloadAndInstallApp({
                    title: "检查更新",
                    content: appVersionInfo.note,
                    downloadUrl: appVersionInfo.url
                })
            }}/>
        </View>
    </View>
};


export interface DownloaderHolder {

    destroy: () => void
}

export interface DownloaderProps {

    renderDownloadDialog: () => React.ReactElement
}


export default class Downloader extends PureComponent<DownloaderProps, {}> {

    private static DEFAULT_HOLDER: DownloaderHolder;

    static defaultProps: DownloaderProps = {

        renderDownloadDialog: (props?: DownloadDialogProps) => {

            return <DownloadDialog {...props}/>
        }
    };

    constructor(props: DownloaderProps) {
        super(props)
    }

    public render(): React.ReactElement | string | number | {} | React.ReactNodeArray | React.ReactPortal | boolean | null | undefined {

        return null
    }

    public static show = (props: DownloaderProps = Downloader.defaultProps): void => {

        const {renderDownloadDialog} = props;
        Downloader.hide();
        Downloader.DEFAULT_HOLDER = new RootSiblings(renderDownloadDialog())

    };

    public static hide = () => {

        const defaultHolder = Downloader.DEFAULT_HOLDER;
        if (defaultHolder && typeof defaultHolder.destroy === 'function') {
            defaultHolder.destroy()
        } else {
            console.warn('default holder is  null or no found destroy method')
        }
    }


}

