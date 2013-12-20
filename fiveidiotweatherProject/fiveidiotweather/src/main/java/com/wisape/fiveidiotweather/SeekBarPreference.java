package com.wisape.fiveidiotweather;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by wisape on 13-12-20.
 */
public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {
    private final static int mMax = 100;
    private LayoutInflater inflater;
    private Context con;
    private SeekBar seekBar;
    private TextView transText;
    private int mValue;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        con = context;
        inflater = LayoutInflater.from(context);
        setPositiveButtonText("设置");
        setNegativeButtonText("取消");
    }

    @Override
    protected View onCreateDialogView() {
        View view =  inflater.inflate(R.layout.seek_bar, null);
        seekBar = (SeekBar) view.findViewById(R.id.transseekbar);
        transText = (TextView) view.findViewById(R.id.trans);
        seekBar.setMax(mMax);
        transText.setText(String.valueOf(mValue) + "%");
        seekBar.setOnSeekBarChangeListener(this);
        return(view);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        seekBar.setProgress(mValue);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            mValue = seekBar.getProgress();
            String value = String.valueOf(mValue);
            if (callChangeListener(value)) {
                Log.d("5sha", "add value " + value);

                persistString(value);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String value = null;

        if (restoreValue ) {
            if (defaultValue == null) {
                value = getPersistedString("100");
            }
            else {
                value = getPersistedString(defaultValue.toString());
            }
        }
        else {
            value=defaultValue.toString();
        }

        mValue = Integer.parseInt(value);
        Log.d("5sha", "get value " + mValue);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        transText.setText(String.valueOf(seekBar.getProgress()) + "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}