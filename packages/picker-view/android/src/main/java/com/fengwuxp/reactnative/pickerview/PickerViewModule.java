package com.fengwuxp.reactnative.pickerview;

import android.content.Context;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

/**
 * picker view
 * <p>
 * https://github.com/Bigkoo/Android-PickerView
 */
public class PickerViewModule extends ReactContextBaseJavaModule {

    private Context mContext;


    public PickerViewModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.mContext = reactContext.getApplicationContext();
    }

    @Override
    public String getName() {
        return "PickerView";
    }

    @Override
    public void initialize() {
        super.initialize();

    }

    /**
     * 注意selectOptions格式按年，月，日，时，分，秒顺序
     * 如果希望不出现选择，则填写 N，eg 选择年月则填写：年,月  或是 年,月,N,N,N,N
     *
     * @param title            可空， 默认：请选择
     * @param selectOptions    可空，逗号隔开 ， 默认选项：年,月,日
     * @param format           可空，默认格式：yyyy-MM-dd
     * @param dateTime         可空，默认当前时间
     * @param rangeOfStartTime
     * @param rangeOfEndTime
     * @param configs
     */
    @ReactMethod
    public void datePick(String title,
                         String selectOptions,
                         String format,
                         final String dateTime,
                         final String rangeOfStartTime,
                         final String rangeOfEndTime,
                         ReadableMap configs,
                         Promise promise) {


        DateTimePicker.getInstance().pick(title, selectOptions, format, dateTime, rangeOfStartTime, rangeOfEndTime, configs, promise, mContext);

    }


    /**
     * @param title
     * @param selectOptions
     * @param optionsSelectChangeCallback
     * @param promise
     */
    @ReactMethod
    public void optionsPick(String title,
                            String selectOptions,
                            ReadableMap configs,
                            Callback optionsSelectChangeCallback,
                            Promise promise) {
        OptionsPicker.getInstance().pick(title, selectOptions, configs, optionsSelectChangeCallback, promise, mContext);
    }

    @ReactMethod
    public void setSelectedOptions(ReadableArray options) {

        Integer[] ts = options.toArrayList().toArray(new Integer[0]);
        OptionsPicker.getInstance().setSelectedOptions(ts);
    }

    @ReactMethod
    public void setPickerOptions(ReadableArray optionsItems) {
        this.setPickerOptions(optionsItems, null, null);
    }

    @ReactMethod
    public void setPickerOptions(ReadableArray options1Items, ReadableArray options2Items) {
        this.setPickerOptions(options1Items, options2Items, null);
    }

    @ReactMethod
    public void setPickerOptions(ReadableArray options1Items,
                                 ReadableArray options2Items,
                                 ReadableArray options3Items) {
        OptionsPicker.getInstance().setPickerOptions(
                options1Items.toArrayList(),
                options2Items == null ? null : options2Items.toArrayList(),
                options3Items == null ? null : options3Items.toArrayList()

        );
    }

    @ReactMethod
    public void setNPickerOptions(ReadableArray options1Items,
                                  ReadableArray options2Items,
                                  ReadableArray options3Items) {
        OptionsPicker.getInstance().setNPicker(
                options1Items.toArrayList(),
                options2Items == null ? null : options2Items.toArrayList(),
                options3Items == null ? null : options3Items.toArrayList()

        );
    }

}
