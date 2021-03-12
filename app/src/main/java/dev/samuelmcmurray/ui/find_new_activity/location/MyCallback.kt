package dev.samuelmcmurray.ui.find_new_activity.location

import dev.samuelmcmurray.data.model.Activity

interface MyCallback {
    fun onCallback(value: ArrayList<Activity>)
}