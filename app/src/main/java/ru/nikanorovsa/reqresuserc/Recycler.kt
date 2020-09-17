package ru.nikanorovsa.reqresuserc

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.for_view_holder.view.*
import ru.nikanorovsa.reqresuserc.model.UserModel

// Стандартный класс для создания RecyclerView, единственная его особенность в том, что он дополнительно
//принимает var edit: Double  для обработки данных из editTextNumber макета основной активности и вывода
// их в RecyclerView
class Recycler(
    val usersList: MutableList<UserModel>,
    val context: Context,
    var clickListener: RecyclerOnItemClickListener

) :
    RecyclerView.Adapter<Recycler.ViewHolder>() {


    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.for_view_holder,
                parent,
                false
            )
        )
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val userName = view.userName
        val userSecondName = view.userSecondName
        val image = view.image
        val right = view.rightm
        val left = view.leftm
        val top = view.topm
        val down = view.down

        fun init(item: UserModel, action: RecyclerOnItemClickListener) {

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }


    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.down.setBackgroundColor(Color.BLACK)
        holder.top.setBackgroundColor(Color.BLACK)
        holder.right.setBackgroundColor(Color.BLACK)
        holder.left.setBackgroundColor(Color.BLACK)
        val user: UserModel = usersList[position]
        holder.userName.text = user.first_name
        holder.userSecondName.text = user.last_name
        Glide
            .with(context)
            .load(user.avatar)
            .into(holder.image)
        holder.init(usersList.get(position), clickListener)


    }


}

// Интерфейс для отработки кликов на карточки
interface RecyclerOnItemClickListener {
    fun onItemClick(item: UserModel, position: Int)
}

