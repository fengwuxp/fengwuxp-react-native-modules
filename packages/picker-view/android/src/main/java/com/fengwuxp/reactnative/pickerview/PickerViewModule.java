package com.fengwuxp.reactnative.pickerview;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;

import java.text.MessageFormat;
import java.util.Objects;


/**
 * picker view
 * <p>
 * https://github.com/Bigkoo/Android-PickerView
 */
public class PickerViewModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private static final String TAG = "PickerViewModule";


    private DateTimePicker dateTimePicker;

    private OptionsPicker optionsPicker = new OptionsPicker();

    private Handler mainHandler = new Handler(Looper.getMainLooper());


    private static final String REACT_MODEL_NAME = "PickerView";

    public PickerViewModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {

        return REACT_MODEL_NAME;
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
     * @param labelOptions     可空，逗号隔开 ， 默认选项：年,月,日
     * @param format           可空，默认格式：yyyy-MM-dd
     * @param dateTime         可空，默认当前时间
     * @param rangeOfStartTime
     * @param rangeOfEndTime
     * @param configs
     */
    @ReactMethod
    public void datePick(String title,
                         String labelOptions,
                         String format,
                         final String dateTime,
                         final String rangeOfStartTime,
                         final String rangeOfEndTime,
                         ReadableMap configs,
                         Promise promise) {

        Log.i("--接收到的参数->", MessageFormat.format("{0} {1} {2} {3} {4} {5} {6} ",
                title, labelOptions, format, dateTime, rangeOfEndTime, rangeOfStartTime, configs));
        Activity activity = getCurrentActivity();
        if (this.dateTimePicker == null) {
            this.dateTimePicker = new DateTimePicker();
        }
        this.dateTimePicker.pick(title, labelOptions, format, dateTime, rangeOfStartTime, rangeOfEndTime, configs, promise, activity);

    }


    /**
     * @param title        标题
     * @param labelOptions
     * @param configs
     * @param promise
     */
    @ReactMethod
    public void optionsPick(String title,
                            String labelOptions,
                            ReadableMap configs,
                            Promise promise) {
        if (this.optionsPicker == null) {
            this.optionsPicker = new OptionsPicker();
        }
        optionsPicker.pick(title, labelOptions, configs, promise, getReactApplicationContext());
    }

    /**
     * 设置选中项
     *
     * @param options
     */
    @ReactMethod
    public void setSelectedOptions(ReadableArray options) {

        if (this.optionsPicker == null) {
            return;
        }
        if (options == null) {
            return;
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Integer[] integers = new Integer[3];
                for (int i = 0; i < 3; i++) {
                    double aDouble = options.getDouble(i);
                    integers[i] = (int) aDouble;
                }
                optionsPicker.setSelectedOptions(integers);
            }
        });


    }

    /**
     * 设置级联的选项列表
     *
     * @param options1Items
     */
    @ReactMethod
    public void setPickerOptions(ReadableArray options1Items) {
        if (this.optionsPicker == null) {
            return;
        }
        int size = options1Items.size();
        Log.i(TAG, MessageFormat.format("{0}", size));
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                optionsPicker.setPickerOptions(
                        options1Items.getArray(0),
                        size > 1 ? options1Items.getArray(1) : null,
                        size > 2 ? options1Items.getArray(2) : null
                );
            }
        });


    }


    /**
     * 设置非级联的选项列表
     *
     * @param options1Items
     */
    @ReactMethod
    public void setNPickerOptions(ReadableArray options1Items) {
        if (this.optionsPicker == null) {
            return;
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                int size = options1Items.size();
                optionsPicker.setNPicker(
                        size > 0 ? options1Items.getArray(0) : null,
                        size > 1 ? options1Items.getArray(1) : null,
                        size > 2 ? options1Items.getArray(2) : null
                );
            }
        });
    }


    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {
        if (this.dateTimePicker != null) {
            this.dateTimePicker.destroy();
        }
        if (this.optionsPicker != null) {
            this.optionsPicker.destroy();
        }
    }


}
