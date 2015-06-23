package com.dotit.gireve.ihm.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import com.dotit.gireve.R;

public class NumberPickerPreference extends DialogPreference {
    public static final int DEFAULT_VALUE = 100;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 1000;
    NumberPicker picker;
    Integer initialValue;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.fragment_number_picker_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.picker = (NumberPicker)view.findViewById(R.id.picker);
        // TODO this should be an XML parameter:
        picker.setMinValue(MIN_VALUE);
        picker.setMaxValue(MAX_VALUE);
        if ( this.initialValue != null ) picker.setValue(initialValue);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        if ( which == DialogInterface.BUTTON_POSITIVE ) {
            this.initialValue = picker.getValue();
            persistInt( initialValue );
            callChangeListener( initialValue );
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
                                     Object defaultValue) {
        int def = ( defaultValue instanceof Number ) ? (Integer)defaultValue
                : ( defaultValue != null ) ? Integer.parseInt(defaultValue.toString()) : DEFAULT_VALUE;
        if ( restorePersistedValue ) {
            this.initialValue = getPersistedInt(def);
        }
        else this.initialValue = (Integer)defaultValue;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, DEFAULT_VALUE);
    }
}