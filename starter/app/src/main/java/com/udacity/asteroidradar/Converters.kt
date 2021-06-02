package com.udacity.asteroidradar

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    @TypeConverter
    fun StringToDate(value: String?): Date? {

        return value?.let { dateFormat.parse(it) }
    }

    @TypeConverter
    fun dateToString(date: Date?): String? {

        return date?.let { dateFormat.format(it) }
    }
}


object ConverterUtil {

    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    @JvmStatic
    fun StringToDate(value: String): Date {

        return dateFormat.parse(value)
    }

    @JvmStatic
    fun dateToString(date: Date): String {

        return dateFormat.format(date)
    }

}
