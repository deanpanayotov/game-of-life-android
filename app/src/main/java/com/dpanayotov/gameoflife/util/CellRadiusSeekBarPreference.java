package com.dpanayotov.gameoflife.util;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

/* The following code was written by Matthew Wiggins
 * and is released under the APACHE 2.0 license
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Improvements :
 * - save the value on positive button click, not on seekbar change
 * - handle @string/... values in xml file
 */

public class CellRadiusSeekBarPreference extends DialogPreference implements SeekBar
        .OnSeekBarChangeListener, OnClickListener {

    private static final int DEFAULT_MIN = 6;
    private static final int DEFAULT_MAX = 10;
    private static final int DEFAULT_DEFAULT = DEFAULT_MAX;

    private static final String androidns = "http://schemas.android.com/apk/res/android";

    private SeekBar mSeekBar;
    private TextView mValueText;
    private Context mContext;

    private String mDialogMessage, mSuffix;
    private int mDefault, mMin, mMax, mValue = 0;

    List<Integer> cellRadiuses;

    public CellRadiusSeekBarPreference(Context context, AttributeSet attrs) {

        super(context, attrs);
        mContext = context;

        // Get string value for dialogMessage :
        int mDialogMessageId = attrs.getAttributeResourceValue(androidns, "dialogMessage", 0);
        if (mDialogMessageId == 0)
            mDialogMessage = attrs.getAttributeValue(androidns, "dialogMessage");
        else mDialogMessage = mContext.getString(mDialogMessageId);

        // Get string value for suffix (text attribute in xml file) :
        int mSuffixId = attrs.getAttributeResourceValue(androidns, "text", 0);
        if (mSuffixId == 0) mSuffix = attrs.getAttributeValue(androidns, "text");
        else mSuffix = mContext.getString(mSuffixId);

        // Get min, max and default seekbar values :
        cellRadiuses = Screen.getAvailableCellRadiuses(context);
        mMin = 0;
        mMax = cellRadiuses.size() - 1;
        mDefault = (cellRadiuses.size() - 1) / 2;
    }


    // ------------------------------------------------------------------------------------------
    // DialogPreference methods :
    @Override
    protected View onCreateDialogView() {

        LinearLayout.LayoutParams params;
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6, 6, 6, 6);

        TextView mSplashText = new TextView(mContext);
        mSplashText.setPadding(30, 10, 30, 10);
        if (mDialogMessage != null)
            mSplashText.setText(mDialogMessage);
        layout.addView(mSplashText);

        mValueText = new TextView(mContext);
        mValueText.setGravity(Gravity.CENTER_HORIZONTAL);
        mValueText.setTextSize(32);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(mValueText, params);

        mSeekBar = new SeekBar(mContext);
        mSeekBar.setOnSeekBarChangeListener(this);
        layout.addView(mSeekBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (shouldPersist())
            mValue = getPersistedInt(mDefault);

        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);

        return layout;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);
    }

    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue) {
        super.onSetInitialValue(restore, defaultValue);
        if (restore)
            mValue = shouldPersist() ? getPersistedInt(mDefault) : 0;
        else
            mValue = (Integer) defaultValue;
    }
    // ------------------------------------------------------------------------------------------


    // ------------------------------------------------------------------------------------------
    // OnSeekBarChangeListener methods :
    @Override
    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
        String t = String.valueOf(cellRadiuses.get(value));
        mValueText.setText(mSuffix == null ? t : t.concat(" " + mSuffix));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seek) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seek) {
    }

    // ------------------------------------------------------------------------------------------
    // Set the positive button listener and onClick action : 
    @Override
    public void showDialog(Bundle state) {

        super.showDialog(state);

        Button positiveButton = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (shouldPersist()) {
            mValue = cellRadiuses.get(mSeekBar.getProgress());
            persistInt(mValue);
            callChangeListener(mValue);
        }

        getDialog().dismiss();
    }
    // ------------------------------------------------------------------------------------------
}