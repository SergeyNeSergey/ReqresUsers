package ru.nikanorovsa.reqresuserc.model

import com.google.gson.annotations.SerializedName
import ru.nikanorovsa.reqresuserc.model.Ad
import ru.nikanorovsa.reqresuserc.model.UserModel


//Класс POJO для получения данных из JSON через Retrofit
data class ListUsers(

    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("total")
    val totalVal: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("data")
    val data: ArrayList<UserModel>,
    @SerializedName("ad")
    val ad: Ad
)