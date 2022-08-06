package com.example.mastersrgamerz;

import androidx.fragment.app.DialogFragment;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.fragment.app.DialogFragment;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class MyTimePicker extends DialogFragment implements  TimePickerDialog.OnTimeSetListener {
    public  interface TimepickerListener{
        void OnTimeSet(TimePicker timePicker,int hour,int min);
    }
    TimepickerListener timepickerListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            timepickerListener=(TimepickerListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar= Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR);
        int min=calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),this,hour,min,DateFormat.is24HourFormat(getContext()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
timepickerListener.OnTimeSet(view,hourOfDay,minute);
    }
}
