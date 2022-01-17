package com.example.stimulation2.common

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext

class ShowTextLong(context: Context, str: String) {

    init {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show()
    }
}