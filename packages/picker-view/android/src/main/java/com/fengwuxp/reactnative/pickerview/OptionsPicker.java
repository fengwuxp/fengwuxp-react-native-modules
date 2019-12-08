package com.fengwuxp.reactnative.pickerview;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fengwuxp.reactnative.pickerview.DateTimePicker.DEFAULT_TITLE;
import static com.fengwuxp.reactnative.pickerview.DateTimePicker.hasTxt;


public class OptionsPicker {

    private static final String TAG = "OptionsPicker";

    private static final String OPTIONS_SELECT_CHANGE_EVENT = "onOptionsSelectChange";

    private OptionsPickerView pickerView;


    private List<Object> wv_option1;
    private List<Object> wv_option2;
    private List<Object> wv_option3;

    private List<Object> mOptions1Items;
    private List<List<Object>> mOptions2Items;
    private List<List<List<Object>>> mOptions3Items;

    private Integer[] selectedOptions;

    OptionsPicker() {
    }


    public void setSelectedOptions(Integer[] options) {
        this.selectedOptions = options;
        if (pickerView != null) {
            if (options.length == 1) {
                pickerView.setSelectOptions(options[0]);
            }
            if (options.length == 2) {
                pickerView.setSelectOptions(options[0], options[1]);
            }
            if (options.length == 3) {
                pickerView.setSelectOptions(options[0], options[1], options[2]);
            }
        }
    }

    public void setPickerOptions(ReadableArray options1Items,
                                 ReadableArray options2Items,
                                 ReadableArray options3Items) {

        List<List<Object>> _options2Items = null;
        if (options2Items != null) {
            _options2Items = new ArrayList<>(options2Items.size());
            for (int i = 0; i < options2Items.size(); i++) {
                _options2Items.add(this.transformPickerArray(Objects.requireNonNull(options2Items.getArray(i))));
            }
        }

        List<List<List<Object>>> _options3Items = null;

        if (options3Items != null) {
            _options3Items = new ArrayList<>(options3Items.size());

            for (int i = 0; i < options3Items.size(); i++) {
                ReadableArray readableArray = options3Items.getArray(i);
                assert readableArray != null;
                List<List<Object>> lists = new ArrayList<>(readableArray.size());
                for (int j = 0; j < readableArray.size(); j++) {
                    lists.add(this.transformPickerArray(Objects.requireNonNull(readableArray.getArray(j))));
                }
                _options3Items.add(lists);
            }
        }


        this.setPickerOptions(this.transformPickerArray(options1Items), _options2Items, _options3Items);

    }


    // 不联动情况下调用
    public void setNPicker(ReadableArray options1Items,
                           ReadableArray options2Items,
                           ReadableArray options3Items) {
        if (options1Items != null) {
            this.wv_option1 = options1Items.toArrayList();
        }
        if (options2Items != null) {
            this.wv_option2 = options2Items.toArrayList();
        }
        if (options3Items != null) {
            this.wv_option3 = options3Items.toArrayList();
        }

        if (pickerView != null) {
            pickerView.setNPicker(this.wv_option1, this.wv_option2, this.wv_option3);
        }

    }

    /**
     * @param title                   可为空
     * @param labelOptions            可为空
     * @param configs                 可为空
     * @param promise
     * @param reactApplicationContext
     */
    public void pick(String title,
                     String labelOptions,
                     ReadableMap configs,
                     Promise promise,
                     ReactApplicationContext reactApplicationContext) {

        if (!hasTxt(title)) {
            title = DEFAULT_TITLE;
        }
        if (!hasTxt(labelOptions)) {
            title = ",,,";
        }

        Activity activity = reactApplicationContext.getCurrentActivity();
        OptionsPickerBuilder builder = new OptionsPickerBuilder(activity, (i1, i2, i3, view) -> {

            promise.resolve(new int[]{i1, i2, i3});
            pickerView.dismiss();
            pickerView = null;
        });
        String[] options = labelOptions.split(",");
        String[] labels = new String[]{null, null, null};

        for (int i = 0; i < options.length && i < 3; i++) {
            labels[i] = options[i];
        }

        builder.setLabels(labels[0], labels[1], labels[2]);
        builder.setOptionsSelectChangeListener((i1, i2, i3) -> {
            Log.i(TAG, MessageFormat.format("{0}  {1}  {2}", i1, i2, i3));
            WritableNativeArray indexs = new WritableNativeArray();
            indexs.pushInt(i1);
            indexs.pushInt(i2);
            indexs.pushInt(i3);
            sendEvent(reactApplicationContext, OPTIONS_SELECT_CHANGE_EVENT, indexs);
        });

        builder.addOnCancelClickListener(v -> {
            promise.reject(PickerResultStatus.CANCEL.name(), "cancel select");
        });


        builder.setTitleText(title);

        // 设置默认主题
//        TypedValue typedValue = new TypedValue();
//        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
//        builder.setDividerColor(typedValue.data);
//        builder.setSubmitColor(typedValue.data);

        pickerView = builder.build();
        if (this.wv_option1 != null) {
            pickerView.setNPicker(this.wv_option1, this.wv_option2, this.wv_option3);
        }
        if (this.mOptions1Items != null) {
            pickerView.setPicker(this.mOptions1Items, this.mOptions2Items, this.mOptions3Items);
        }
        if (this.selectedOptions != null) {
            this.setSelectedOptions(this.selectedOptions);
        }
        activity.runOnUiThread(() -> pickerView.show());

    }

    void destroy() {
        if (this.pickerView != null) {
            pickerView.dismiss();
            this.pickerView = null;
        }
        this.mOptions1Items = null;
        this.mOptions2Items = null;
        this.mOptions3Items = null;
        this.wv_option1 = null;
        this.wv_option2 = null;
        this.wv_option3 = null;
        this.selectedOptions = null;
    }

    protected void setPickerOptions(List<Object> options1Items,
                                    List<List<Object>> options2Items,
                                    List<List<List<Object>>> options3Items) {
        this.mOptions1Items = options1Items;
        this.mOptions2Items = options2Items;
        this.mOptions3Items = options3Items;
        if (pickerView != null) {

            // [   "福建"  ]
            // [  ["福州市","厦门"] ,]
            // [  [["鼓楼区","晋安区"],[思明区","集美区"]], ]
            pickerView.setPicker(options1Items, options2Items, options3Items);
        }


    }


    private List<Object> transformPickerArray(ReadableArray readableArray) {

        List<Object> list = new ArrayList<>();
        int size = readableArray.size();
        boolean isMap = ReadableType.Map.equals(readableArray.getDynamic(0).getType());

        for (int i = 0; i < size; i++) {
            if (isMap) {
                ReadableMap map = readableArray.getMap(i);
                assert map != null;
                list.add(new PickerItemObject(map));
            } else {
                list.add(readableArray.getString(i));
            }

        }
        return list;
    }


    private void sendEvent(ReactContext reactContext, String eventName, Object params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}
