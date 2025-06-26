package com.rentals.homigo.model

import android.util.Base64
import java.text.SimpleDateFormat
import java.util.*

fun generateTimestamp(): String {
    val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    return sdf.format(Date())
}

fun generatePassword(shortcode: String, passkey: String, timestamp: String): String {
    val strToEncode = shortcode + passkey + timestamp
    return Base64.encodeToString(strToEncode.toByteArray(), Base64.NO_WRAP)
}