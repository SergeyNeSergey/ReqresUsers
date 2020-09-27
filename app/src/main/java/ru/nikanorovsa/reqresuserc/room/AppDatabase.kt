package ru.nikanorovsa.reqresuserc.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.nikanorovsa.reqresuserc.model.UserModel

// Абстрактный класс с объектом-компаньёном для создания базы данных Room. Объект компаньён используется
// в качестве синглтона.
@Database(entities = [UserModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "user_model"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}
