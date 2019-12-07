package com.fengwuxp.reactnative.pickerview;

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
import com.facebook.react.bridge.ReadableMap;

import java.text.MessageFormat;
import java.util.List;

import static com.fengwuxp.reactnative.pickerview.DateTimePicker.hasTxt;


public class OptionsPicker {

    private static final String TAG = "OptionsPicker";

    private OptionsPickerView<Object> pickerView;

    private static OptionsPicker OPTIONS_TIME_PICKER;

    private OptionsPicker() {
    }

    public synchronized static OptionsPicker getInstance() {
        if (OPTIONS_TIME_PICKER == null) {
            OPTIONS_TIME_PICKER = new OptionsPicker();
        }
        return OPTIONS_TIME_PICKER;
    }

    public void setSelectedOptions(Integer[] options) {

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

    public void setPickerOptions(List<Object> optionsItems) {
        this.setPickerOptions(optionsItems, null, null);
    }

    public void setPickerOptions(List<Object> options1Items, List<List<Object>> options2Items) {
        this.setPickerOptions(options1Items, options2Items, null);
    }

    public void setPickerOptions(List<Object> options1Items,
                                 Object options2Items,
                                 Object options3Items) {

        if (pickerView != null) {

            // ["北京"]
            // ["北京",["北京市"]]
            // ["北京",["北京市",["东城区"]]]

            // ["北京"]
            // [  ["北京市"] ]
            // [  [["东城区"],["西城区"]] ]
            pickerView.setPicker(options1Items, (List<List<Object>>) options2Items, (List<List<List<Object>>>) options3Items);
        }


    }

    // 不联动情况下调用
    public void setNPicker(List<Object> options1Items,
                           List<Object> options2Items,
                           List<Object> options3Items) {
        if (pickerView != null) {
            pickerView.setNPicker(options1Items, options2Items, options3Items);
        }

    }

    /**
     * @param title         可为空
     * @param selectOptions 可为空
     * @param configs       可为空
     * @param promise
     * @param context
     */
    public void pick(String title,
                     String selectOptions,
                     ReadableMap configs,
                     Callback optionsSelectChangeCallback,
                     Promise promise,
                     Context context) {

        if (!hasTxt(title)) {
            title = "请选择";
        }
        if (!hasTxt(selectOptions)) {
            title = ",,,";
        }

        OptionsPickerBuilder builder = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int i1, int i2, int i3, View view) {

                promise.resolve(new int[]{i1, i2, i3});
                pickerView.dismiss();

                pickerView = null;
            }
        });
        String[] options = selectOptions.split(",");
        String[] labels = new String[]{null, null, null};

        for (int i = 0; i < options.length && i < 3; i++) {
            labels[i] = options[i];
        }


        builder.setLabels(labels[0], labels[1], labels[2]);
        builder.setOptionsSelectChangeListener((i1, i2, i3) -> {

            Log.i(TAG, MessageFormat.format("{0}  {1}  {2}", i1, i2, i3));
            if (optionsSelectChangeCallback != null) {
                optionsSelectChangeCallback.invoke(i1, i2, i3);
            }
        });

        builder.addOnCancelClickListener(v -> {
            promise.reject(PickerResultStatus.CANCEL.name(), "cancel select");
        });


        builder.setTitleText(title);

        // 设置默认主题
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        builder.setDividerColor(typedValue.data);
        builder.setSubmitColor(typedValue.data);

        pickerView = builder.build();
        pickerView.show();

    }


}
