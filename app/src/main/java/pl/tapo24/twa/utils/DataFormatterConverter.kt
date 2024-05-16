package pl.tapo24.twa.utils

import java.util.*

interface DataFormatterConverter {



    /**
     * Formats the given year, month, and dayOfMonth into a human-readable calendar date.
     *
     * @param year The year component of the date.
     * @param _month The month component of the date. (0 - 11)
     * @param dayOfMonth The day of the month component of the date. (1 - 31)
     * @return A string representation of the calendar date in the format "yyyy-MM-dd".
     */
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


    /**
     * Converts a human-readable calendar date string in the format "yyyy-MM-dd" to a timestamp.
     *
     * @param humanDate The human-readable calendar date string to convert.
     * @return The timestamp corresponding to the provided humanDate.
     */
    fun convertHumanCalendarDateToTimeStamp(humanDate: String) : Int {
        val date = humanDate.split("-")
        val year = date[0].toInt()
        val month = date[1].toInt()-1
        val day = date[2].toInt()
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, 0,0, 0)
        return (calendar.timeInMillis/1000).toInt()

    }

    /**
     * Converts a timestamp to a human-readable calendar date string.
     *
     * @param timeStamp The timestamp to convert.
     * @return The human-readable calendar date string in the format "yyyy-MM-dd".
     */
    fun convertTimeStampToHumanCalendarDate(timeStamp: Int): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeStamp.toLong()*1000
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return formatToHumanCalendarDate(year, month, day)

    }
}