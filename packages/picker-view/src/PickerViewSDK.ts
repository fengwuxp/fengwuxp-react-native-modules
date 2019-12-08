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

    optionsPick: (
        title?: string,
        selectOptions?: string,
        configs?: {
            [key: string]: any
        },
        optionsItemSelectChangeCallback?: (selectedValues: number[]) => void
    ) => Promise<number[]>;

    setSelectedOptions: (selectedValues: number[]) => void;

    /**
     * 设置级联的选择数据
     * @param options
     */
    setPickerOptions: (options: CascadeOptionsData) => void;

    setNPicker: (options: NCascadeOptionsData) => void;
}
