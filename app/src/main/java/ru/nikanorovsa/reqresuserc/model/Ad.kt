package ru.nikanorovsa.reqresuserc.model

import com.google.gson.annotations.SerializedName
// сторонние данные
data class Ad(
    @SerializedName("company")
    val company: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("text")
    val text: String)