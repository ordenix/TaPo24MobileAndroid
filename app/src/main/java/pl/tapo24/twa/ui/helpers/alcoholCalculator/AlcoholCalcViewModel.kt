package pl.tapo24.twa.ui.helpers.alcoholCalculator

import androidx.lifecycle.ViewModel

class AlcoholCalcViewModel : ViewModel() {



    fun calculateFromPromileToMgL(promile: Double):String {
        if (promile == 0.0) return "0.0"
        val calc = (promile / 2.1)*100
        val round = Math.round(calc)
        val result:Double = round / 100.0
        return (result).toString()
    }

    fun calculateFromMgLToPromile(mgl: Double):String {
        return (mgl * 2.1).toString()
    }

}