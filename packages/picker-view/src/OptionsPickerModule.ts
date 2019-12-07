import {PickerItem, PickerViewAndroidSDK} from "./PickerViewSDK";
import {NativeModules} from 'react-native';

export interface OptionsPickerModuleInterface {

    pick: (options: BaseOptionsPickerOptions) => Promise<number[]>;
}


export interface BaseOptionsPickerOptions {

    /**
     * 每一列的标题   用 ','分割
     * 默认选项：",,,"
     */
    columnLabels?: string;

    /**
     * 标题
     */
    title?: string;

    /**
     * 其他配置，例如样式等
     */
    configs?: {
        [key: string]: string
    }

    /**
     * 默认选择的值
     */
    defaultSelectedValues?: number[];

    /**
     * 切换item项滚动停止时，实时回调监听
     * @param currentIndex    当前变化的 index
     * @param selectedValues  所有选择中的值
     */
    optionsItemSelectChangeCallback?: (currentIndex, selectedValues: number[]) => void,

    /**
     * 用于级联选择的数据
     */
    cascadeOptionsData?: [
        Array<PickerItem>,
        Array<PickerItem[]>,
        Array<Array<PickerItem[]>>,
    ];

    /**
     * 非级联选择的数据
     */
    optionsData?: [
        Array<PickerItem>,
        Array<PickerItem>,
        Array<PickerItem>,
    ]
}

const PickerView: PickerViewAndroidSDK = NativeModules.PickerView;

const OptionsPickerModule: OptionsPickerModuleInterface = {

    pick: (options: BaseOptionsPickerOptions) => {
        const {cascadeOptionsData, title, configs, columnLabels, optionsItemSelectChangeCallback, defaultSelectedValues} = options;
        const [items1, items2, items3] = cascadeOptionsData;
        const selectedValues = defaultSelectedValues || [0, 0, 0];
        const promise = PickerView.optionsPick(title, columnLabels, (values) => {
            const changeIndex = values.map((index, i) => {
                const isChange = index === selectedValues[i];
                if (isChange) {
                    return index;
                } else {
                    return -1;
                }
            }).find(index => index >= 0);
            if (optionsItemSelectChangeCallback) {
                optionsItemSelectChangeCallback(changeIndex, values);
            }

        }, configs);

        PickerView.setSelectedOptions([...selectedValues]);
        PickerView.setPickerOptions(items1, items2, items3);

        return promise;
    }

};

export default OptionsPickerModule;
