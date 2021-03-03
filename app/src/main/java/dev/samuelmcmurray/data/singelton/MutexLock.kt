package dev.samuelmcmurray.data.singelton

class MutexLock private constructor(){

    var locked = true
    var flag = true

    companion object {
        val getInstance = MutexLock()
    }
}