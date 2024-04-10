package pl.tapo24.twa.utils

import java.util.*

interface DataFormatterConverter {



    fun formatToHumanCalendarDate(year: Int, _month: Int, dayOfMonth: Int): String {
        val month = _month + 1
        var monthString = ""
        var dayString = ""
        monthString = if (month <=9) {
            "0$month"
        }else {
            month.toString()
        }

        dayString = if (dayOfMonth <=9) {
            "0$dayOfMonth"
        }else {
            dayOfMonth.toString()
        }
        return "$year-$monthString-$dayString"

    }


    fun convertHumanCalendarDateToTimeStamp(humanDate: String) : Int {
        val date = humanDate.split("-")
        val year = date[0].toInt()
        val month = date[1].toInt()-1
        val day = date[2].toInt()
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, 0,0, 0)
        return (calendar.timeInMillis/1000).toInt()

    }

    fun convertTimeStampToHumanCalendarDate(timeStamp: Int): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeStamp.toLong()*1000
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return formatToHumanCalendarDate(year, month, day)

    }
}