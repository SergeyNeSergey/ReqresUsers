package ru.nikanorovsa.reqresuserc


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.fragment_layout.*
import ru.nikanorovsa.reqresuserc.model.UserModel
import ru.nikanorovsa.reqresuserc.room.AppDatabase
import ru.nikanorovsa.reqresuserc.room.UserDao

//  Фрагмент отображающий полную информацию об отдельном юзере с возможностью её изменения.
class UserFragment : Fragment() {
    //Объект компаньон инициализирующий фрагмент и сохраняющий в своём поле id юзера(UserModel) для вызова
    //его во фрагменте для отрисовки.
    companion object {
        lateinit var id1: String
        fun newInstance(id: String): UserFragment {
            val args = Bundle()
            id1 = id
            args.putString("key", id)
            Log.d("eee", id1 + " in")

            val fragment = UserFragment()
            fragment.arguments = args
            return fragment
        }
    }

    //База данных
    private var dataBase: AppDatabase? = null

    //Интерфейс для запроса к базе данных
    private var userDao: UserDao? = null

    //Обращаюсь к базе данных. Передаю id пользователя(UserModel), получаю объект UserModel и с помощью
    //данных в нём отрисовываю View
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_layout, container, false)
        dataBase = AppDatabase.getAppDataBase(activity!!.applicationContext)
        userDao = dataBase?.rateDao()
        val observable = userDao!!.findById(id1)
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(object :
            DisposableSubscriber<UserModel>() {
            override fun onComplete() {

            }

            override fun onNext(t: UserModel?) {
                userName11.text = "User id=" + id1
                userName.setText(t?.first_name)
                userSecondName.setText(t?.last_name)
                email.setText(t?.email)
                wayToFotoFile.setText(t?.avatar)
                context?.let {
                    Glide
                        .with(it)
                        .load(t?.avatar)
                        .into(image)
                }


            }

            override fun onError(t: Throwable?) {


            }
        })





        return v
    }

    // обрабатываю нажатия на клавиши, сохраняю изменения либо удаляю UserModel из базы данных
    override fun onResume() {
        super.onResume()
        delete.setOnClickListener {
            Completable.fromAction { userDao?.deleteUser(id1) }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    val intent = Intent(this.activity, MainActivity::class.java)
                    startActivity(intent)
                }

        }
        save.setOnClickListener {
            Completable.fromAction {
                val model = UserModel(
                    id1.toInt(),
                    email.text.toString(),
                    userName.text.toString(),
                    userSecondName.text.toString(),
                    wayToFotoFile.text.toString()
                )
                userDao?.insert(model)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    val intent = Intent(this.activity, MainActivity::class.java)
                    startActivity(intent)
                }

        }

    }


}

