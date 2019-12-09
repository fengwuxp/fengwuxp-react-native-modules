type DateTimeFormatType =
    "yyyy"
    | "MM"
    | "dd"
    | "HH"
    | "mm"
    | "ss"
    | "yyyy-MM"
    | "yyyy-MM-dd"
    | "HH:mm"
    | "yyyy-MM-dd HH:mm"
    | "yyyy-MM-dd HH:mm:ss"

export interface PickerItemObject {

    text: string;
    value?: string | number
}


export type PickerItem = PickerItemObject | string;

export type CascadeOptionsData = [
    Array<PickerItem>,
    Array<PickerItem[]>,
    Array<Array<PickerItem[]>>,
];

export type NCascadeOptionsData = [
    Array<PickerItem>,
    Array<PickerItem>,
    Array<PickerItem>,
];

export interface PickerViewAndroidSDK {

    /**
     * 日期选择器
     * @param title
     * @param selectOptions
     * @param format
     * @param dateTime
     * @param rangeOfStartTime
     * @param rangeOfEndTime
     * @param configs
     */
    datePick: (
        title?: string,
        selectOptions?: string,
        format?: DateTimeFormatType,
        dateTime?: string,
        rangeOfStartTime?: string,
        rangeOfEndTime?: string,
        configs?: {
            [key: string]: any
        },
    ) => Promise<string>;

    /**
     * 多列选项选择器
     * @param title
     * @param selectOptions
     * @param configs
     * @param optionsItemSelectChangeCallback
     */
    optionsPick: (
        title?: string,
        selectOptions?: string,
        configs?: {
            [key: string]: any
        },
        optionsItemSelectChangeCallback?: (selectedValues: number[]) => void
    ) => Promise<number[]>;

    /**
     * 设置默认的选中索引
     * @param selectedValues
     */
    setSelectedOptions: (selectedValues: number[]) => void;

    /**
     * 设置级联的选择数据
     * @param options
     */
    setPickerOptions: (options: CascadeOptionsData) => void;

    /**
     * 设置非级联数据
     * @param options
     */
    setNPickerOptions: (options: NCascadeOptionsData) => void;
}
