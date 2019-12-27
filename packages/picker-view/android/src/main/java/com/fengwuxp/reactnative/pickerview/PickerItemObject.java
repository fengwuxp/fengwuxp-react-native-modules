package com.fengwuxp.reactnative.pickerview;

import com.contrarywind.interfaces.IPickerViewData;
import com.facebook.react.bridge.ReadableMap;

import java.util.Objects;


/**
 * picker item
 */
public final class PickerItemObject implements IPickerViewData {

    private final String text;

    private final Object value;

    public PickerItemObject(String text, Object value) {
        this.text = text;
        this.value = value;
    }

    public PickerItemObject(ReadableMap map) {
        this.text = map.getString("text");
        this.value = map.getDynamic("value");
    }

    public String getText() {
        return text;
    }

//    public void setText(String text) {
//        this.text = text;
//    }

    public Object getValue() {
        return value;
    }

//    public void setValue(String value) {
//        this.value = value;
//    }

    @Override
    public String getPickerViewText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PickerItemObject that = (PickerItemObject) o;
        return Objects.equals(text, that.text) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PickerItemObject{");
        sb.append("text='").append(text).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
