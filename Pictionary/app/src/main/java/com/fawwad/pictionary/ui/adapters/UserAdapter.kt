package com.fawwad.pictionary.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fawwad.pictionary.R
import com.fawwad.pictionary.repository.models.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var users: List<User> = listOf();

    fun updateUsersList(users: List<User>){
        this.users = users
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            View.inflate(
                parent.context,
                R.layout.row_user, null
            )
        )
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        if (position>=0 && position<users.size){
            users.get(position).let {
                holder.setUser(it)
            }
        }
    }


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var nameView: TextView? = null
        private var pointsView: TextView? = null
        private var turnView: TextView? = null
        private var hostView: TextView? = null

        init {
            nameView = itemView.findViewById(R.id.tv_name)
            pointsView = itemView.findViewById(R.id.tv_points)
            turnView = itemView.findViewById(R.id.tv_is_turn)
            hostView = itemView.findViewById(R.id.tv_is_host)
        }

        fun setUser(user: User) {
            nameView?.setText(user.userName)
            pointsView?.setText(user.score.toString())
            turnView?.visibility = if (user.turn)View.VISIBLE else View.GONE
            hostView?.visibility = if (user.host)View.VISIBLE else View.GONE
        }


    }

}