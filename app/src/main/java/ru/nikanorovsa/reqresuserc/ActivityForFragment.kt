package ru.nikanorovsa.reqresuserc

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Активность для запуска фрагмента
class ActivityForFragment : AppCompatActivity() {
    val KEY = "key"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_fragment)
        val idFromIntent = intent.getStringExtra(KEY)
        if (idFromIntent != null) {
            Log.d("eee", idFromIntent)
            val userFragment = UserFragment.newInstance(idFromIntent)
            val manager = supportFragmentManager
            val args = Bundle()
            args.putSerializable(KEY, idFromIntent)
            userFragment.arguments = args
            manager.beginTransaction().add(R.id.fragment_conainer, userFragment).commit()
        } else {
            Toast.makeText(applicationContext, "empty data id", Toast.LENGTH_LONG)
                .show()
        }

    }


}

