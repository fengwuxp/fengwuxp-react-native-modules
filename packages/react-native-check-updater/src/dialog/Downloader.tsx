import {AppVersionInfo} from "../AppVersionInfo";
import {BackHandler, Image, ImageSourcePropType, Linking, NativeModules, Text, View} from "react-native";
import React, {PureComponent, useState} from "react";
import RootSiblings from 'react-native-root-siblings'
import {AppCheckForUpdaterModuleSDK} from "../AppCheckForUpdaterModuleSDK";
import RNFS, {DownloadFileOptions} from "react-native-fs";
import DeviceInfo from "react-native-device-info";
import * as Progress from 'react-native-progress';
import styles from "./styles.less"
import {StyleSheetScreenAdapter} from 'fengwuxp-react-native-starter'

const AppCheckUpdater: AppCheckForUpdaterModuleSDK = NativeModules.AppCheckUpdater;

export interface DownloadDialogProps {

    appVersionInfo: AppVersionInfo;

    backgroundIcon?: ImageSourcePropType;

    /**
     * Fill color of the indicator.
     *
     * @type {string}
     * @memberof DefaultPropTypes
     * @default rgba(0, 122, 255, 1)
     */
    progressBarColor?: string;

    title?: string;
}

export const DownloadDialog = (props: DownloadDialogProps) => {

    const {backgroundIcon, progressBarColor, title, appVersionInfo} = props;

    const [downloadStatus, setDownloadStatus] = useState(false);
    const [downloadProgress, setDownloadProgress] = useState(0);

    if (downloadStatus) {

        return <View style={styles.download_mask}>
            <View style={styles.download_progress}>
                <Text style={styles.download_progress_text}>正在下载</Text>
                <View style={styles.download_progress_bar}>
                    <Progress.Bar color={progressBarColor}
                                  height={StyleSheetScreenAdapter.scalePx2dp(16)}
                                  progress={downloadProgress}
                                  width={StyleSheetScreenAdapter.scalePx2dp(410)}/>
                    <Text
                        style={[styles.download_progress_text]}>{parseFloat((downloadProgress * 100).toString()).toFixed(2)}%</Text>
                </View>
            </View>
        </View>
    }

    return <View style={styles.download_mask}>
        <View style={styles.dialog}>
            {backgroundIcon && <Image source={backgroundIcon}/>}
            <View style={styles.dialog_title}>
                <Text style={styles.dialog_title_text}>{title || "检查更新"}</Text>
            </View>
            <View style={styles.dialog_content}>
                <Text style={styles.dialog_content_text}>{appVersionInfo.note}</Text>
            </View>
            <View style={styles.dialog_buttons}>
                <Text style={[styles.dialog_button, styles.border_right]}
                      onPress={() => {
                          if (appVersionInfo.forcibly) {
                              BackHandler.exitApp();
                          } else {
                              Downloader.hide();
                          }
                      }}>暂不升级</Text>
                <Text style={[styles.dialog_button, styles.update_immediately]} onPress={() => {
                    setDownloadStatus(true);

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

                    const downloadDest = `${(RNFS.ExternalDirectoryPath)}/${DeviceInfo.getBundleId()}.apk`;
                    const options: DownloadFileOptions = {
                        fromUrl: appVersionInfo.url,
                        toFile: downloadDest,
                        background: true,
                        begin: (res) => {
                            console.log('begin', res);
                            console.log('contentLength:', res.contentLength / 1024 / 1024, 'M');
                        },
                        progress: (result) => {
                            setDownloadProgress(Math.ceil(result.bytesWritten / result.contentLength));
                        },

                    };
                    const ret = RNFS.downloadFile(options);
                    return ret.promise.then((result) => {
                        if (result && result.statusCode === 200) {
                            AppCheckUpdater.installApp(downloadDest, appVersionInfo.code).catch((e) => {
                                console.log("安装失败", e)
                            })
                        }
                        return Promise.reject();
                    });


                }}>立即升级</Text>
            </View>
        </View>
    </View>
};


export interface DownloaderHolder {

    destroy: () => void
}

export interface DownloaderProps {

    renderDownloadDialog: (props: DownloadDialogProps) => React.ReactElement
}


export default class Downloader extends PureComponent<DownloaderProps, {}> {

    private static DEFAULT_HOLDER: DownloaderHolder;

    static defaultProps: DownloaderProps = {

        renderDownloadDialog: (props: DownloadDialogProps) => {

            return <DownloadDialog {...props}/>
        }
    };

    constructor(props: DownloaderProps) {
        super(props)
    }

    public render(): React.ReactElement | string | number | {} | React.ReactNodeArray | React.ReactPortal | boolean | null | undefined {

        return null
    }

    public static show = (dialogProps: DownloadDialogProps, props: DownloaderProps = Downloader.defaultProps): void => {

        const {renderDownloadDialog} = props;
        Downloader.hide();
        Downloader.DEFAULT_HOLDER = new RootSiblings(renderDownloadDialog(dialogProps))

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

