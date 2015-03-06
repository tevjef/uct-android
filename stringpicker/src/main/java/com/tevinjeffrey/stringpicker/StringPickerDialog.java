package com.tevinjeffrey.stringpicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.tevinjeffrey.stringpicker.R;

public class StringPickerDialog extends DialogFragment {

    private OnClickListener mListener;

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        if (!(activity instanceof OnClickListener)) {
            throw new RuntimeException("callback is must implements StringPickerDialog.OnClickListener!");
        }
        mListener = (OnClickListener) activity;
        mActivity = activity;
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.string_picker_dialog, null, false);

        final StringPicker stringPicker = (StringPicker) view.findViewById(R.id.string_picker);
        final Bundle params = getArguments();
        if (params == null) {
            throw new RuntimeException("params is null!");
        }

        final String[] values = params.getStringArray(getValue(R.string.string_picker_dialog_values));
        stringPicker.setValues(values);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(getValue(R.string.string_picker_dialog_title));
        builder.setPositiveButton(getValue(R.string.string_picker_dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onClick(stringPicker.getCurrentValue());
            }
        });
        builder.setNegativeButton(getValue(R.string.string_picker_dialog_cancel), null);
        builder.setView(view);
        return builder.create();
    }

    private String getValue(final int resId) {
        return mActivity.getString(resId);
    }

    public interface OnClickListener {
        void onClick(final String value);
    }

}