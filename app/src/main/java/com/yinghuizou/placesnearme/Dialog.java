package com.yinghuizou.placesnearme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by yinghuizou on 8/19/17.
 */

public class Dialog extends DialogFragment {
    CharSequence[] item = {"hi","no"};

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
        //build.setTitle("Select the type of fun place").setSingleChoiceItems(item,-1,)


        return super.onCreateDialog(savedInstanceState);
    }





}
