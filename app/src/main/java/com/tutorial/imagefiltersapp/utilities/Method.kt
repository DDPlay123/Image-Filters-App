package com.tutorial.imagefiltersapp.utilities

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.tutorial.imagefiltersapp.Request_PERMISSION

object Method {
    fun requestPermission(activity: Activity, vararg permissions: String) {
        if (!hasPermissions(activity, *permissions))
            ActivityCompat.requestPermissions(activity, permissions, Request_PERMISSION)
        else
            return
    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions)
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return false
        return true
    }
}