package com.example.android.politicalpreparedness.utils

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@BindingAdapter("dateText")
fun TextView.bindElectionDateText(date: Date?) {
    text = if (date == null) {
        ""
    } else {
        val format = SimpleDateFormat("EEEE, MMM. dd, yyyy • HH:mm z", Locale.US)
        format.format(date)
    }
}

@BindingAdapter("followText")
fun Button.bindFollowText(isFollow: Boolean) {
    text = if (isFollow) {
        context.getString(R.string.unfollow)
    } else {
        context.getString(R.string.follow)
    }
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, value: Boolean) {
    view.visibility = if (value) View.VISIBLE else View.GONE
}



