import {PickerViewAndroidSDK} from "./PickerViewSDK";
import {NativeModules} from 'react-native';
import DateFormatUtils ,{DateFormatType}from "fengwuxp-common-utils/lib/date/DateFormatUtils";

/**
 * 时间选择器模块
 */
export interface TimerPickerModuleInterface {

    /**
     * 选择时间
     * @param options
     * @return string  字符串格式的时间
     */
    readonly pick: (options: DateTimerPickerOptions) => Promise<string>;

    /**
     * 选择日期 年月日
     * @param options
     * @return string yyyy-MM-dd
     */
    readonly pickDate: (options: BaseDateTimerPickerOptions) => Promise<string>;


    /**
     * 选择日期 年月日时分
     * @param options
     * @return string yyyy-MM-dd hh:mm
     */
    readonly pickDateTime: (options: BaseDateTimerPickerOptions) => Promise<string>;


    /**
     * 选择时分
     * @param value
     * @return string hh:mm
     */
    readonly pickTime: (options: BaseDateTimerPickerOptions) => Promise<string>;
}

export interface BaseDateTimerPickerOptions {

    /**
     * 每一列的标题  用 ','分割
     * 注意columnTitles格式按年，月，日，时，分，秒顺序
     * 如果希望不出现选择，则填写 N，eg 选择年月则填写：年,月  或是 年,月,N,N,N,N
     * 默认选项：年,月,日
     */
    columnLabels?: string;


    /**
     * 默认当前时间 格式为 yyyy-MM-dd hh:mm:ss
     */
    value?: string | Date;

    /**
     * 时间范围选择开始
     * 默认 1970-01-01 00:00:00
     */
    rangeBegin?: string | Date;

    /**
     * 时间范围选择结束
     * 默认：null
     */
    rangeEnd?: string | Date;

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
}

export interface DateTimerPickerOptions extends BaseDateTimerPickerOptions {


    /**
     * 强制格式化类型，如果改字段有值，返回的结果会按照format的格式返回
     * 默认格式：yyyy-MM-dd
     */
    format?: DateFormatType;

}

const transformDate = (date: string | Date, format: DateFormatType) => {
    if (date == null) {
        return null;
    }

    if (typeof date === "string") {
        return date;
    }
    if (format == null) {
        return null;
    }
    return DateFormatUtils.formatterDate(date, format);
};
const EMPTY_CHAR = "N";

const PickerView: PickerViewAndroidSDK = NativeModules.PickerView;

/**
 * 时间选择器
 */
const TimerPickerModule: TimerPickerModuleInterface = {

    pick: (options: DateTimerPickerOptions) => {
        const format = options.format == null ? null : options.format;
        const value = transformDate(options.value, format);
        const rangeBegin = transformDate(options.rangeBegin, format);
        const rangeEnd = transformDate(options.rangeEnd, format);
        const args = [
            options.title,
            options.columnLabels,
            format.replace("hh", 'HH'),
            value,
            rangeBegin,
            rangeEnd,
            options.configs || {} as any
        ];
        return PickerView.datePick(...args);
    },

    pickDate: (options: BaseDateTimerPickerOptions) => {
        return TimerPickerModule.pick({
            // columnLabels: `年,月,日`,
            ...options,
            format: "yyyy-MM-dd"
        });
    },

    pickDateTime: (options: BaseDateTimerPickerOptions) => {
        return TimerPickerModule.pick({
            columnLabels: `年,月,日,时,分`,
            ...options,
            format: "yyyy-MM-dd hh:mm"
        });
    },

    pickTime: (options: BaseDateTimerPickerOptions) => {
        return TimerPickerModule.pick({
            columnLabels: `${EMPTY_CHAR},${EMPTY_CHAR},${EMPTY_CHAR},时,分`,
            ...options,
            format: "hh:mm"
        });
    }

};
export default TimerPickerModule;

