package dev.samuelmcmurray.data.singelton

import dev.samuelmcmurray.data.model.CurrentUser

class CurrentUserSingleton private constructor(){

    var currentUser: CurrentUser?= null
    var firstTimeRegister = false
    var loggedIn = false
    companion object {
        val getInstance = CurrentUserSingleton()
    }

}