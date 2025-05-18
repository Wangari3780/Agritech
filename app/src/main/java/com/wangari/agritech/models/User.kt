package com.wangari.agritech.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val accountType: AccountType = AccountType.FARMER,
    val mpesaPhone: String? = null,
    val mpesaLinked: Boolean = false,
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val createdAt: Date = Date()
) : Parcelable

enum class AccountType {
    FARMER,
    BUYER,
    SUPPLIER,
    ADMIN
}

@Parcelize
data class NotificationSettings(
    val marketPriceAlerts: Boolean = true,
    val transactionUpdates: Boolean = true,
    val weatherAlerts: Boolean = true,
    val marketTips: Boolean = true
) : Parcelable