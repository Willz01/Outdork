package dev.samuelmcmurray.utilities

import dev.samuelmcmurray.data.model.Activity

interface MyCallback {
    fun onCallback(value: ArrayList<Activity>)
}