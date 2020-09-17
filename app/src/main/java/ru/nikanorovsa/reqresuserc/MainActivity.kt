package ru.nikanorovsa.reqresuserc


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.activity_main.*
import ru.nikanorovsa.reqresuserc.model.ListUsers
import ru.nikanorovsa.reqresuserc.model.UserModel
import ru.nikanorovsa.reqresuserc.retrofit.RetrofitFactory.userController
import ru.nikanorovsa.reqresuserc.room.AppDatabase
import ru.nikanorovsa.reqresuserc.room.UserDao

//Основной класс приложения
class MainActivity : AppCompatActivity(), RecyclerOnItemClickListener {
    //Лист поставляемый в  Recycler. Обновляется в методе initRateList и сохраняется в баззе данных
    var listRate: MutableList<UserModel> = ArrayList()

    //База данных
    private var dataBase: AppDatabase? = null

    //Интерфейс для запроса к базе данных
    private var userDao: UserDao? = null

    //Ключ для интента
    private val KEY = "key"


    // В данном методе производится проверка наличия данных в базе данных. Инициализируется база данных на измениния в
    //которой подписывается слушатель. Инициализируется класс Recycler. При первом запуске приложения
    // обязательно подключение интернета. При последующих возможна работа без интернета, Recycler будет
    //проинициализирован из базы данных предыдущим сохраненным значением списка.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar.visibility = View.GONE

        dataBase = AppDatabase.getAppDataBase(context = this)
        userDao = dataBase?.rateDao()
        val observable = userDao?.getAll()
        observable?.observeOn(AndroidSchedulers.mainThread())?.subscribe(object :
            DisposableSubscriber<MutableList<UserModel>>() {
            override fun onComplete() {


                recycler.adapter!!.notifyDataSetChanged()

            }


            override fun onError(t: Throwable?) {

                runOnUiThread {
                    val toast = Toast.makeText(applicationContext, "$t", Toast.LENGTH_LONG)
                    toast.show()
                }
            }

            override fun onNext(t: MutableList<UserModel>?) {
                if (t == null) {
                    runOnUiThread {
                        val toast = Toast.makeText(
                            applicationContext,
                            "$t" + "object in database",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                } else {
                    listRate = t
                    if (listRate.size != 0) {
                        recycler.layoutManager = GridLayoutManager(applicationContext, 2)
                        recycler.adapter = Recycler(
                            listRate,
                            applicationContext, this@MainActivity

                        )
                    } else {
                        initRateList()
                    }
                }


            }

        })


    }

    //Создаю меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Отрабатываю клик, вызываю метод initRateList()
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.update -> {
                initRateList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Метод для заполнения базы данных и списка объектов в массив поставляемый в RecyclerView.
// При вызове метода запускается прогресс бар, на объект observable полученный из UserController и
// приведённый к Flowable подписывается слушатель.
    private fun initRateList() {
        progressBar.visibility = View.VISIBLE
        val observable = userController.getUserAsync()
        observable
            .subscribeOn(Schedulers.io())
            .toFlowable(BackpressureStrategy.BUFFER).observeOn(Schedulers.io())
            .subscribe(object :
                DisposableSubscriber<ListUsers>() {
                override fun onComplete() {


                }

                override fun onNext(t: ListUsers?) {
                    if (t != null) {


                        userDao?.updateData(t.data)
                        runOnUiThread {


                            progressBar.visibility = View.GONE
                        }


                    }


                }

                override fun onError(t: Throwable?) {
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        val toast =
                            Toast.makeText(applicationContext, "$t", Toast.LENGTH_LONG)
                        toast.show()
                    }

                }

            })

    }

    // Метод интерфейса RecyclerOnItemClickListener в котором отрабатывается нажатие на карточку и запуск
// активности содержащей в себе фрагмент
    override fun onItemClick(item: UserModel, position: Int) {
        Log.d("eee", item.id.toString())

        val intent = Intent(this, ActivityForFragment::class.java)
        intent.putExtra(KEY, item.id.toString())
        startActivity(intent)
    }


}





