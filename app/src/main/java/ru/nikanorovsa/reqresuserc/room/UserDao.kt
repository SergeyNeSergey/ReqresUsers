package ru.nikanorovsa.reqresuserc.room

import androidx.room.*
import io.reactivex.Flowable
import ru.nikanorovsa.reqresuserc.model.UserModel

// Интерфейс для обращения к базе данных Room
@Dao
interface UserDao {

    @Query("SELECT * FROM user_model")
    fun getAll(): Flowable<MutableList<UserModel>>

    @Query("SELECT * FROM user_model WHERE id LIKE :id")
    fun findById(id: String): Flowable<UserModel>

    @Insert
    fun insertAll(rateModelList: MutableList<UserModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insert(model: UserModel)


    @Query("DELETE FROM user_model")
    fun deleteAllTable()

    @Query("DELETE FROM user_model WHERE id LIKE :id")
    fun deleteUser(id: String)


    @Transaction
    fun updateData(rate: MutableList<UserModel>) {
        deleteAllTable()
        insertAll(rate)

    }
}

