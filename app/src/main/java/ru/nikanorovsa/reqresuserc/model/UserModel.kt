package ru.nikanorovsa.reqresuserc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


//Модель данных для вывода всех юзеров

@Entity(tableName = "user_model")
data class UserModel(

    @PrimaryKey @SerializedName ("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("avatar") val avatar: String
)