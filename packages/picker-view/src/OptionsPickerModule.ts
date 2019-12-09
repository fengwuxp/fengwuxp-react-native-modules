import {
    CascadeOptionsData,
    NCascadeOptionsData,
    PickerItem,
    PickerItemObject,
    PickerViewAndroidSDK
} from "./PickerViewSDK";
import {DeviceEventEmitter, EmitterSubscription, NativeModules} from 'react-native';

type OptionsSelectResult = {
    indexs: number[],
    values: PickerItem[]
};

export interface OptionsPickerModuleInterface {

    pick: (options: BaseOptionsPickerOptions) => Promise<OptionsSelectResult>;
}


type GetOptionsPickerColumnItemsFunction = (prevItem: PickerItem, currentColumnIndex: number) => Promise<PickerItem[]>;

type OptionsItemSelectChangeCallbackFunction = (changeColumnIndex, result: OptionsSelectResult) => void;

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
    onOptionsItemSelectChange?: OptionsItemSelectChangeCallbackFunction,


    /**
     * 获取选项列表列的数据
     * @param prevItem               上一列选中的数据
     * @param currentColumnIndex    column index  从0开始
     */
    getOptionsPickerColumnItems: GetOptionsPickerColumnItemsFunction;


    /**
     * 用于选择的数据
     */
    optionsData?: CascadeOptionsData | NCascadeOptionsData;

    /**
     * 是否级联选择
     * default false
     */
    isCascade?: boolean;

    /**
     * 使用非级联模式 模拟级联
     */
    // isMockCascade?: boolean;

    /**
     * 选择的列项
     * default 3
     */
    optionsColumns?: number;

}

const PickerView: PickerViewAndroidSDK = NativeModules.PickerView;

let PickerData: { optionsData: any, selectedValues: number[] } = {
    optionsData: null,
    selectedValues: null
};

const OptionsPickerModule: OptionsPickerModuleInterface = {

    pick: async (options: BaseOptionsPickerOptions) => {
        let {
            optionsData,
            isCascade,
            title,
            configs,
            columnLabels,
            onOptionsItemSelectChange,
            getOptionsPickerColumnItems,
            optionsColumns,
            defaultSelectedValues,
        } = options;

        const noneOptionsDate = optionsData == null;
        defaultSelectedValues = defaultSelectedValues || [];
        defaultSelectedValues = defaultSelectedValues.filter(item => item != null);
        optionsColumns = optionsColumns || 3;
        const selectedValues = [];
        const isSimplePickerItem = typeof defaultSelectedValues[0] === 'string';
        if (noneOptionsDate) {
            // 加载初始化数据
            if (isCascade) {
                PickerData.optionsData = await initCascadeOptionsData(optionsColumns, defaultSelectedValues, isSimplePickerItem, selectedValues, getOptionsPickerColumnItems);
            } else {
                PickerData.optionsData = await initOptions(optionsColumns, defaultSelectedValues, isSimplePickerItem, selectedValues, getOptionsPickerColumnItems);
            }
        } else {
            PickerData.optionsData = optionsData;
        }

        // console.log("--selectedValues-->", selectedValues);
        PickerData.selectedValues = selectedValues;
        // 设置默认选中
        PickerView.setSelectedOptions([...selectedValues]);
        PickerView.setNPickerOptions([...PickerData.optionsData] as NCascadeOptionsData);

        // 选项发生改变的回调的wrapper
        const onOptionsItemSelectChangeCallbackWrapper = (indexs: number[]) => {
            console.log("-----select change---->", indexs, PickerData.selectedValues);
            const changeColumnIndex = indexs.map((index, i) => {
                const isChange = index !== PickerData.selectedValues[i];
                if (isChange) {
                    return i;
                } else {
                    return -1;
                }
            }).find(index => index >= 0);
            console.log("--changeColumnIndex-->", changeColumnIndex)
            const selectedResult = getSelectedResult(indexs, isCascade, PickerData.optionsData);
            if (onOptionsItemSelectChange) {
                onOptionsItemSelectChange(changeColumnIndex, selectedResult);
            }
            PickerData.selectedValues = indexs;
            if (isCascade === false) {
                return
            }
            // console.log("----selectedResult--->", selectedResult);
            if (changeColumnIndex === 0) {
                onCascadeFirstColumnChangeTryLoadData(selectedResult.values[0], indexs, getOptionsPickerColumnItems)
            } else if (changeColumnIndex === 1) {
                onCascadeSecondColumnChangeTryLoadData(selectedResult.values[1], indexs, getOptionsPickerColumnItems)
            }
        };

        // 监听选择change事件
        const eventListenerHolder = registerOptionsSelectChangeHandlerListener(onOptionsItemSelectChangeCallbackWrapper);

        return new Promise((resolve, reject) => {
            PickerView.optionsPick(title, columnLabels, configs).then((indexs: number[]) => {
                resolve(getSelectedResult(indexs, isCascade, PickerData.optionsData))
            }).catch(reject).finally(() => {
                // removeOptionsSelectChangeHandlerListener(onOptionsItemSelectChangeCallbackWrapper);
                // 移除事件监听
                eventListenerHolder.remove();
                PickerData.optionsData = null;
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
                           optionsData: NCascadeOptionsData): OptionsSelectResult => {
    const [i1, i2, i3] = indexs;
    const values: PickerItem[] = [];
    const [items1, items2, items3] = optionsData as any;
    if (items1) {
        values.push(items1[i1]);
    }
    if (items2) {
        values.push(items2[i2]);
    }
    if (items3) {
        values.push(items3[i3]);
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
    if (defaultItem == null || items == null) {
        return 0;
    }
    let index;
    if (isSimplePickerItem) {
        index = items.findIndex((item) => item === defaultItem);
    } else {
        index = items.findIndex((item: PickerItemObject) => item.value == (defaultItem as PickerItemObject).value);
    }
    return index < 0 ? 0 : index
};

/**
 * 初始化 级联选择器的数据
 * @param optionsColumns
 * @param defaultSelectedValues
 * @param isSimplePickerItem
 * @param selectIndexList
 * @param getOptionsPickerColumnItems
 */
async function initCascadeOptionsData(optionsColumns: number,
                                      defaultSelectedValues: PickerItem[],
                                      isSimplePickerItem: boolean,
                                      selectIndexList: number[],
                                      getOptionsPickerColumnItems: GetOptionsPickerColumnItemsFunction): Promise<NCascadeOptionsData> {
    let cascadeOptionsData: Array<PickerItem[]> = [];
    for (let i = 0; i < optionsColumns; i++) {
        let pickerItem = null;
        if (i > 0) {
            pickerItem = defaultSelectedValues[i - 1] || cascadeOptionsData[i - 1][0];
        }
        const items = await getOptionsPickerColumnItems(pickerItem, i);
        selectIndexList.push(findSelectedIndex(items, isSimplePickerItem, defaultSelectedValues[i] || pickerItem));
        cascadeOptionsData.push(items)
    }
    return cascadeOptionsData as NCascadeOptionsData;
}

/**
 * 级联列表的第一列数据发生变化 , 尝试加载第二列的数据
 * @param prevItem
 * @param indexList
 * @param getOptionsPickerColumnItems
 */
const onCascadeFirstColumnChangeTryLoadData = (prevItem: PickerItem,
                                               indexList: number[],
                                               getOptionsPickerColumnItems: GetOptionsPickerColumnItemsFunction) => {
    const [i1] = indexList;
    getOptionsPickerColumnItems(prevItem, 1).then((items) => {
        PickerData.optionsData[1] = items;
        onCascadeSecondColumnChangeTryLoadData(items[0], [i1, 0, 0], getOptionsPickerColumnItems);
    });
};

/**
 * 级联列表的第二列数据发生变化 , 尝试加载第二列的数据
 * @param prevItem
 * @param indexList
 * @param getOptionsPickerColumnItems
 */
const onCascadeSecondColumnChangeTryLoadData = (prevItem: PickerItem,
                                                indexList: number[],
                                                getOptionsPickerColumnItems: GetOptionsPickerColumnItemsFunction) => {
    const [i1, i2] = indexList;
    getOptionsPickerColumnItems(prevItem, 2).then((items) => {
        PickerData.optionsData[2] = items;
        // 重新设置数据
        PickerView.setSelectedOptions([i1, i2, 0]);
        PickerView.setNPickerOptions([...PickerData.optionsData] as any);
    });
};

/**
 * 初始化非级联选择 的数据
 * @param optionsColumns
 * @param defaultSelectedValues
 * @param isSimplePickerItem
 * @param selectIndexList
 * @param getOptionsPickerColumnItems
 */
function initOptions(optionsColumns: number,
                     defaultSelectedValues: PickerItem[],
                     isSimplePickerItem: boolean,
                     selectIndexList: number[],
                     getOptionsPickerColumnItems: GetOptionsPickerColumnItemsFunction): Promise<NCascadeOptionsData> {

    const promises: Promise<PickerItem[]>[] = [];
    for (let i = 0; i < optionsColumns; i++) {
        promises.push(getOptionsPickerColumnItems(defaultSelectedValues[i], i));
    }
    return Promise.all(promises).then((values: Array<any>) => {
        const numbers: number[] = values.map((items, i) => {
            return findSelectedIndex(items, isSimplePickerItem, defaultSelectedValues[i]);
        });
        selectIndexList.push(...numbers);
        return values as NCascadeOptionsData;
    });
}


const OPTIONS_SELECT_CHANGE_EVENT: string = "onOptionsSelectChange";

const registerOptionsSelectChangeHandlerListener = (optionsSelectChangeHandle: (currentIndex, index: number[]) => void): EmitterSubscription => {
    return DeviceEventEmitter.addListener(OPTIONS_SELECT_CHANGE_EVENT, optionsSelectChangeHandle as any);
};

// const removeOptionsSelectChangeHandlerListener = (optionsSelectChangeHandle: (currentIndex, index: number[]) => void) => {
//     // DeviceEventEmitter.removeAllListeners(OPTIONS_SELECT_CHANGE_EVENT);
//     DeviceEventEmitter.removeListener(OPTIONS_SELECT_CHANGE_EVENT, optionsSelectChangeHandle);
// };

export default OptionsPickerModule;
