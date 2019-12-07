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

interface PickerItemObject {

    text: string;
    value?: string | number
}


export type PickerItem = PickerItemObject | string;

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
        optionsItemSelectChangeCallback?: (selectedValues: number[]) => void,
        configs?: {
            [key: string]: any
        },
    ) => Promise<number[]>;

    setSelectedOptions: (selectedValues: number[]) => void;

    setPickerOptions: (item1: Array<PickerItem>,
                       item2?: Array<PickerItem[]>,
                       item3?: Array<Array<PickerItem[]>>) => void;
}
