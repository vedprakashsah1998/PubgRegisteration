package com.client.vpman.pubgregisteration;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c=Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),TimePickerFragment.this,hour,minute, DateFormat.is24HourFormat(getActivity()));

    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
       Home.textInputEditText3.setText("Hour:"+hourOfDay+"Minute:"+minute);
    }
}
