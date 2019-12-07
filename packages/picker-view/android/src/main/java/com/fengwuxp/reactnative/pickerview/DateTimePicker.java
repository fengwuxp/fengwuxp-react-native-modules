package com.fengwuxp.reactnative.pickerview;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateTimePicker {

    private static final String TAG = "DateTimePicker";


    private static final String[] DEFAULT_FORMATS = {
            "yyyy", "MM", "dd", "HH", "mm", "ss"

    };

    private static final String[] DEFAULT_PREFIXS = {
            "", "-", "-", " ", ":", ":"
    };

    private static DateTimePicker DATE_TIME_PICKER;

    private DateTimePicker() {
    }

    public synchronized static DateTimePicker getInstance() {
        if (DATE_TIME_PICKER == null) {
            DATE_TIME_PICKER = new DateTimePicker();
        }
        return DATE_TIME_PICKER;
    }

    /**
     * 注意selectOptions格式按年，月，日，时，分，秒顺序
     * 如果希望不出现选择，则填写 N，eg 选择年月则填写：年,月  或是 年,月,N,N,N,N
     *
     * @param title               可空， 默认：请选择
     * @param selectOptions       可空，逗号隔开 ， 默认选项：年,月,日
     * @param format              可空，默认格式：yyyy-MM-dd
     * @param dateTime，可以空，默认当前时间
     */
    public void pick(String title,
                     String selectOptions,
                     String format,
                     final String dateTime,
                     final String rangeOfStartTime,
                     final String rangeOfEndTime,
                     ReadableMap configs,
                     Promise promise,
                     Context context) {

        if (!hasTxt(selectOptions)) {
            //默认可以选择年月日
            selectOptions = "年,月,日";
        }

        String[] options = selectOptions.split(",");

        boolean[] typeOptions = new boolean[]{true, false, false, false, false, false};

        String[] labels = new String[]{null, null, null, null, null, null};

        boolean hasFormat = hasTxt(format);

        if (!hasFormat) {
            format = "";
        }

        StringBuilder formatBuilder = new StringBuilder(format);
        for (int i = 0; i < options.length && i < 6; i++) {

            String option = options[i];

            typeOptions[i] = !("N".equalsIgnoreCase(option));

            labels[i] = option;

            if (!hasFormat) {
                formatBuilder.append(DEFAULT_PREFIXS[i]).append(DEFAULT_FORMATS[i]);
            }

            Log.i(TAG, (i + 1) + "." + option + ",format:" + formatBuilder);

        }
        format = formatBuilder.toString();

        if (!hasTxt(format)) {
            format = "yyyy-MM-dd";
        }
        if (!hasTxt(title)) {
            title = "请选择";
        }

        Log.i(TAG, "format:" + format + ",selectOptions:" + selectOptions);

        final SimpleDateFormat df = new SimpleDateFormat(format, Locale.CHINA);

        TimePickerBuilder builder = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {


                String data = df.format(date);
                Log.i(TAG, "on result date:" + date + ",format:" + df.toPattern() + ",value:" + data);
                promise.resolve(data);

            }
        });
        builder.addOnCancelClickListener(v -> {
            promise.reject(PickerResultStatus.CANCEL.name(), "cancel select");
        });

        // 设置默认主题
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        builder.setDividerColor(typedValue.data);
        builder.setSubmitColor(typedValue.data);

        // TODO 设置配置
//        Iterator<Map.Entry<String, Object>> entryIterator = configs.getEntryIterator();
//        builder.setBgColor(configs.getInt("bgColor"));

        Calendar startTime = null, endTime = null;

        if (hasTxt(rangeOfStartTime)) {
            try {
                Date time = df.parse(rangeOfStartTime);
                startTime = Calendar.getInstance();
                startTime.setTime(time);
            } catch (ParseException e) {
                Log.w(TAG, e);
            }
        }

        if (hasTxt(rangeOfEndTime)) {
            try {
                Date time = df.parse(rangeOfEndTime);
                endTime = Calendar.getInstance();
                endTime.setTime(time);
            } catch (ParseException e) {
                Log.w(TAG, e);
            }
        }

        builder.setRangDate(startTime, endTime);

        builder.setLabel(labels[0], labels[1], labels[2], labels[3], labels[4], labels[5]);

        builder.setType(typeOptions);

        builder.setTitleText(title);

        TimePickerView pickerView = builder.build();

        if (hasTxt(dateTime)) {
            try {
                Date parseTime = df.parse(dateTime);
                Calendar instance = Calendar.getInstance();
                instance.setTime(parseTime);
                pickerView.setDate(instance);
            } catch (ParseException e) {
                Log.w(TAG, e);
            }
        }

        pickerView.show();

    }


     static boolean hasTxt(String txt) {
        return txt != null && txt.trim().length() > 0;
    }
}
