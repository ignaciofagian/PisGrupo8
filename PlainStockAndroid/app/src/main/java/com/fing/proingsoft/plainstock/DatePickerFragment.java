package com.fing.proingsoft.plainstock;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.fing.proingsoft.plainstock.otros.Configuracion;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private int year, monthOfYear, dayOfMonth;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current Date as the default values for the picker
        final Calendar c = Configuracion.getInstance().getFechaSistema();
        year = c.get(Calendar.YEAR);
        monthOfYear = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);


        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),this,year,monthOfYear,dayOfMonth);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.getDatePicker().setMinDate(0);
        return dialog;


        //return new DatePickerDialog(getActivity(),this,year,monthOfYear,dayOfMonth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_picker,null);
    }

    public void onDateSet(DatePicker view, int year,int monthOfYear,int dayOfMonth) {
    }
}
