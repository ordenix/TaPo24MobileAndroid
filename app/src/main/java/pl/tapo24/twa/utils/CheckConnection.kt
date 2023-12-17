package pl.tapo24.twa.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import pl.tapo24.twa.data.NetworkTypes

class CheckConnection {
    fun getConnectionType(context: Context): NetworkTypes {
        var result = NetworkTypes.None // Returns connection type. 0: none; 1: mobile data; 2: wifi; 3: vpn
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = NetworkTypes.WiFi
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = NetworkTypes.Mobile
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)){
                        result = NetworkTypes.Vpn
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = NetworkTypes.WiFi
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = NetworkTypes.Mobile
                    } else if(type == ConnectivityManager.TYPE_VPN) {
                        result = NetworkTypes.Vpn
                    }
                }
            }
        }
        return result
    }
}