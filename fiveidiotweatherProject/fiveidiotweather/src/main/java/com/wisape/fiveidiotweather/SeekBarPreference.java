package com.wisape.fiveidiotweather;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsSeekBar;
import android.widget.SeekBar;

/**
 * Created by wisape on 13-12-20.
 */
public class SeekBarPreference extends DialogPreference{
    private int mValue;
    private Context con;
    private SeekBar seekBar;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        con = context;
        mValue = 100;
    }

    @Override
    protected View onCreateDialogView() {
        seekBar = new SeekBar(con);
        seekBar.setMax(100);
        return(seekBar);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        seekBar.setProgress(mValue);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        String value = String.valueOf(seekBar.getProgress());
            if (callChangeListener(value)) {
                persistString(value);
            }
    }


    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String value;

        if (restoreValue) {
            if (defaultValue==null) {
                value=getPersistedString("100");
            }
            else {
                value=getPersistedString(defaultValue.toString());
            }
        }
        else {
            value=defaultValue.toString();
        }

        mValue = Integer.parseInt(value);
    }

}