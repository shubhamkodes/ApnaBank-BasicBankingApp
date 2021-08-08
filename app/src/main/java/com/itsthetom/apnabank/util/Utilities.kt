package com.itsthetom.apnabank.util

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.itsthetom.apnabank.R
import java.lang.Exception
import java.util.*

import java.text.DateFormat
import java.text.SimpleDateFormat


object Utilities {
    private val calendar=Calendar.getInstance()
    private val monthName= arrayOf(R.string.january,R.string.february,R.string.march,
                                   R.string.april,R.string.may,R.string.june,R.string.july,
                                   R.string.august,R.string.september,R.string.october,R.string.november,R.string.december)
    fun getPrettyTime(context:Context,timeInMilliSeconds:Long):String
    {
        calendar.timeInMillis=timeInMilliSeconds
        var time= calendar.get(Calendar.DATE).toString()
        time=time+" "+context.getString(monthName[calendar.get(Calendar.MONTH)])+", "+ calendar.get(Calendar.YEAR)

        val formatter: DateFormat = SimpleDateFormat("hh:mm a", Locale.US)
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"))
        time=time+"   "+formatter.format(Date(timeInMilliSeconds))
        return time
    }

    fun hideKeyboard(activity: Activity){
        try {
            WindowInsetsControllerCompat(activity.window, activity.window.decorView).hide(
                WindowInsetsCompat.Type.ime())
        }catch (e: Exception){
            Log.d(e.toString(),e.message.toString())
        }
    }


}