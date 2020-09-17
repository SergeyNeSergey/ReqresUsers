package ru.nikanorovsa.reqresuserc.retrofit

import ru.nikanorovsa.reqresuserc.model.ListUsers

import io.reactivex.Observable
import retrofit2.http.GET

interface UserController {
    /**
     * Метод для получения данных с сайта
     */
    @GET("users?page=2")
    fun getUserAsync(): Observable<ListUsers>
}