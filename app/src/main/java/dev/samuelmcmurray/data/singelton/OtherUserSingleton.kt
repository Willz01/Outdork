package dev.samuelmcmurray.data.singelton

import dev.samuelmcmurray.data.model.OtherUser

class OtherUserSingleton private constructor(){

    var otherUser: OtherUser? = null

    companion object {
        val getInstance = OtherUserSingleton()
    }

}