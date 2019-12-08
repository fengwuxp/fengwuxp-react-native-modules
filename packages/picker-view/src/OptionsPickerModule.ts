import {
    CascadeOptionsData,
    NCascadeOptionsData,
    PickerItem,
    PickerItemObject,
    PickerViewAndroidSDK
} from "./PickerViewSDK";
import {DeviceEventEmitter, NativeModules} from 'react-native';

type OptionsSelectResult = {
    indexs: number[],
    values: PickerItem[]
};

export interface OptionsPickerModuleInterface {

    pick: (options: BaseOptionsPickerOptions) => Promise<OptionsSelectResult>;
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
    defaultSelectedValues?: PickerItem[];

    /**
     * 切换item项滚动停止时，实时回调监听
     * @param changeColumnIndex    当前变化的 column index  从0开始
     * @param result               选择结果
     */
    onOptionsItemSelectChangeCallback?: (changeColumnIndex, result: OptionsSelectResult) => void,


    /**
     * 当列表滚动时 加载一列的数据
     * @param changeColumnItem      滚动的列选中的数据
     * @param changeColumnIndex    滚动的列索引  从0开始
     */
    onOptionsChangeGetPickerItems: (changeColumnItem: PickerItem, changeColumnIndex: number) => Promise<PickerItem[]>;


    /**
     * 用于选择的数据
     */
    optionsData?: CascadeOptionsData | NCascadeOptionsData;

    /**
     * 是否级联选择
     * default true
     */
    isCascade?: boolean;

    /**
     * 选择的列项
     * default 3
     */
    optionsColumns?: number;

}

const PickerView: PickerViewAndroidSDK = NativeModules.PickerView;


const OptionsPickerModule: OptionsPickerModuleInterface = {

    pick: async (options: BaseOptionsPickerOptions) => {
        let {
            optionsData,
            isCascade,
            title,
            configs,
            columnLabels,
            onOptionsItemSelectChangeCallback,
            onOptionsChangeGetPickerItems,
            optionsColumns,
            defaultSelectedValues
        } = options;

        isCascade = isCascade || true;
        const noneOptionsDate = optionsData == null;
        defaultSelectedValues = defaultSelectedValues || [];
        defaultSelectedValues = defaultSelectedValues.filter(item => item != null);
        optionsColumns = optionsColumns || 3;
        const selectedValues = [];
        const isSimplePickerItem = typeof defaultSelectedValues[0] === 'string';
        if (noneOptionsDate) {
            // 加载初始化数据
            if (isCascade) {
                optionsData = await initCascadeOptionsData(optionsColumns, defaultSelectedValues, isSimplePickerItem, selectedValues, onOptionsChangeGetPickerItems);
            } else {
                optionsData = await initOptions(optionsColumns, defaultSelectedValues, isSimplePickerItem, selectedValues, onOptionsChangeGetPickerItems);
            }
        }

        console.log("--selectedValues-->", selectedValues);
        // 设置默认选中
        PickerView.setSelectedOptions([...selectedValues]);

        if (isCascade) {
            PickerView.setPickerOptions(optionsData as CascadeOptionsData);
        } else {
            PickerView.setNPicker(optionsData as NCascadeOptionsData);

        }

        // 选项发生改变的回调的wrapper
        const onOptionsItemSelectChangeCallbackWrapper = (indexs: number[]) => {
            console.log("-----select change---->", indexs);
            const changeColumnIndex = indexs.map((index, i) => {
                const isChange = index === selectedValues[i];
                if (isChange) {
                    return index;
                } else {
                    return -1;
                }
            }).find(index => index >= 0);
            const selectedResult = getSelectedResult(indexs, isCascade, optionsData);
            if (onOptionsItemSelectChangeCallback) {

                onOptionsItemSelectChangeCallback(changeColumnIndex, selectedResult);
            }
            if (isCascade === false) {
                return
            }
            if (changeColumnIndex === 0) {
                onCascadeFirstColumnChangeTryLoadData(selectedResult.values[indexs[0]], optionsData as any, indexs, onOptionsChangeGetPickerItems)
            } else {
                onCascadeSecondColumnChangeTryLoadData(selectedResult.values[indexs[1]], optionsData as any, indexs, onOptionsChangeGetPickerItems)
            }
        };
        registerOptionsSelectChangeHandlerListener(onOptionsItemSelectChangeCallbackWrapper);

        return new Promise((resolve, reject) => {
            PickerView.optionsPick(title, columnLabels, configs).then((indexs: number[]) => {
                resolve(getSelectedResult(indexs, isCascade, optionsData))
            }).catch(reject).finally(() => {
                removeOptionsSelectChangeHandlerListener(onOptionsItemSelectChangeCallbackWrapper);
            });
        })
    }
};

/**
 * 获取选中结果数据
 * @param indexs       选中的索引
 * @param isCascade
 * @param optionsData
 */
const getSelectedResult = (indexs: number[],
                           isCascade: boolean,
                           optionsData: CascadeOptionsData | NCascadeOptionsData): OptionsSelectResult => {
    const [i1, i2, i3] = indexs;
    const values: PickerItem[] = [];
    const [items1, items2, items3] = optionsData as any;
    if (isCascade) {
        values.push(items1[i1]);
        if (items2) {
            const items2Element = items2[i1] || [];
            values.push(items2Element[i2]);
        }
        if (items3) {
            let items3Element = items3[i1] || [];
            let items3Element2 = items3Element[i2] || [];
            values.push(items3Element2[i3])
        }
    } else {
        if (items1) {
            values.push(items1[i1]);
        }
        if (items2) {
            values.push(items2[i2]);
        }
        if (items3) {
            values.push(items3[i3]);
        }
    }
    return {
        indexs,
        values
    }
};

/**
 * 查找默认选中的索引
 * @param items                某一列的选择数据
 * @param isSimplePickerItem   是否为字符串选择项
 * @param defaultItem          默认选中的值
 */
const findSelectedIndex = (items: PickerItem[], isSimplePickerItem: boolean, defaultItem: PickerItem) => {
    if (isSimplePickerItem) {
        return items.findIndex((item) => item === defaultItem);
    } else {
        return items.findIndex((item: PickerItemObject) => item.value == (defaultItem as PickerItemObject).value);
    }
};

/**
 * 初始化 级联选择器的数据
 * @param optionsColumns
 * @param defaultSelectedValues
 * @param isSimplePickerItem
 * @param selectIndexList
 * @param onOptionsChangeGetPickerItems
 */
async function initCascadeOptionsData(optionsColumns: number,
                                      defaultSelectedValues: PickerItem[],
                                      isSimplePickerItem: boolean,
                                      selectIndexList: number[],
                                      onOptionsChangeGetPickerItems: (prevItem: PickerItem, cellIndex: number) => Promise<PickerItem[]>) {
    const cascadeOptionsData: Array<PickerItem[]> = [];
    for (let i = 0; i < optionsColumns; i++) {
        let pickerItem = defaultSelectedValues[i];
        if (i > 0) {
            pickerItem = cascadeOptionsData[i - 1][0] || pickerItem;
        }
        const items = await onOptionsChangeGetPickerItems(pickerItem, i);
        selectIndexList.push(findSelectedIndex(items, isSimplePickerItem, pickerItem) || 0);
        cascadeOptionsData.push(items)
    }
    return cascadeOptionsData.map((items, index) => {
        if (index === 0) {
            return items;
        }
        if (index === 1) {
            return [
                cascadeOptionsData[1]
            ]
        }
        return [
            [
                cascadeOptionsData[2]
            ]
        ]
    }).reduce((prev: Array<any>, item) => {
        prev.push(item);
        return prev;
    }, []) as CascadeOptionsData;
}

/**
 * 级联列表的第一列数据发生变化 , 尝试加载第二列的数据
 * @param prevItem
 * @param optionsData
 * @param indexList
 * @param onOptionsChangeGetPickerItems
 */
const onCascadeFirstColumnChangeTryLoadData = (prevItem: PickerItem,
                                               optionsData: CascadeOptionsData,
                                               indexList: number[],
                                               onOptionsChangeGetPickerItems: (prevItem: PickerItem, cellIndex: number) => Promise<PickerItem[]>) => {

    const [i1] = indexList;
    const items2 = optionsData[1][i1];
    if (items2 != null) {
        return
    }
    onOptionsChangeGetPickerItems(prevItem, 0).then((items) => {
        optionsData[1][i1] = items;
        // 重新设置数据
        PickerView.setSelectedOptions([i1, 0, 0]);
        PickerView.setPickerOptions(optionsData);
    });
};

/**
 * 级联列表的第二列数据发生变化 , 尝试加载第二列的数据
 * @param prevItem
 * @param optionsData
 * @param indexList
 * @param onOptionsChangeGetPickerItems
 */
const onCascadeSecondColumnChangeTryLoadData = (prevItem: PickerItem,
                                                optionsData: CascadeOptionsData,
                                                indexList: number[],
                                                onOptionsChangeGetPickerItems: (prevItem: PickerItem, cellIndex: number) => Promise<PickerItem[]>) => {
    const [i1, i2] = indexList;
    const items3 = optionsData[2][i1] || [];
    const items3_2 = items3[i2];
    if (items3_2 != null) {
        return
    }
    onOptionsChangeGetPickerItems(prevItem, 1).then((items) => {
        items3[i2] = items;
        optionsData[2][i1] = items3;
        // 重新设置数据
        PickerView.setSelectedOptions([i1, i2, 0]);
        PickerView.setPickerOptions(optionsData);
    });
};

/**
 * 初始化非级联选择 的数据
 * @param optionsColumns
 * @param defaultSelectedValues
 * @param isSimplePickerItem
 * @param selectIndexList
 * @param onOptionsChangeGetPickerItems
 */
function initOptions(optionsColumns: number,
                     defaultSelectedValues: PickerItem[],
                     isSimplePickerItem: boolean,
                     selectIndexList: number[],
                     onOptionsChangeGetPickerItems: (prevItem: PickerItem, cellIndex: number) => Promise<PickerItem[]>): Promise<NCascadeOptionsData> {
    const promises: Promise<PickerItem[]>[] = [];
    for (let i = 0; i < optionsColumns; i++) {
        promises.push(onOptionsChangeGetPickerItems(defaultSelectedValues[i], i));
    }
    return Promise.all(promises).then((values: Array<any>) => {
        const numbers: number[] = values.map((items, i) => {
            return findSelectedIndex(items, isSimplePickerItem, defaultSelectedValues[i]) || 0;
        });
        selectIndexList.push(...numbers);
        return values as NCascadeOptionsData;
    });
}


const OPTIONS_SELECT_CHANGE_EVENT: string = "onOptionsSelectChange";

const registerOptionsSelectChangeHandlerListener = (optionsSelectChangeHandle: (currentIndex, index: number[]) => void) => {
    DeviceEventEmitter.addListener(OPTIONS_SELECT_CHANGE_EVENT, optionsSelectChangeHandle);
};

const removeOptionsSelectChangeHandlerListener = (optionsSelectChangeHandle: (currentIndex, index: number[]) => void) => {
    // DeviceEventEmitter.removeAllListeners(OPTIONS_SELECT_CHANGE_EVENT);
    DeviceEventEmitter.removeListener(OPTIONS_SELECT_CHANGE_EVENT, optionsSelectChangeHandle);
};

export default OptionsPickerModule;
