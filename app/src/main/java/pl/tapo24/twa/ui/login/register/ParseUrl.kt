package pl.tapo24.twa.ui.login.register

import pl.tapo24.twa.data.register.AccountTokenData

class ParseUrl {

    fun parse(url: String): AccountTokenData? {
        val array = url.split("/")
        if (array.size == 7){
            return AccountTokenData(array[4].toInt(), array[5])
        }
        return null
    }
}