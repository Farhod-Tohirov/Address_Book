package uz.star.testforanymobile.utils

import android.util.Log
import java.text.DecimalFormat

/**
 * Created by Farhod Tohirov on 05-Feb-21
 **/

fun Float.metrToKM(): String {
    var result = ""
    val df = DecimalFormat()
    df.maximumFractionDigits = 2
    if (this > 0F) {
        if (this < 1000F) {
            result = df.format(this)
            result += " m"
        } else {
            result = df.format(this * 0.001F)
            result += " km"
        }
    }
    return result
}