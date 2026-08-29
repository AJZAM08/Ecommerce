package com.ecommerce.utils

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Long): String {
    val localeID = Locale("id", "ID")
    val formatter = NumberFormat.getCurrencyInstance(localeID).apply {
        maximumFractionDigits = 0
    }
    return formatter.format(amount).replace("Rp", "Rp ")
}