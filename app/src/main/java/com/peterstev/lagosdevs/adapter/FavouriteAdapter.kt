package com.peterstev.lagosdevs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.peterstev.domain.model.User
import com.peterstev.lagosdevs.R
import com.peterstev.lagosdevs.databinding.FragmentFavItemBinding

class FavouriteAdapter(
    private val items: MutableList<User>,
    private val listener: OnItemClickListener,
) : RecyclerView.Adapter<FavouriteAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = FragmentFavItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    fun updateList(list: MutableList<User>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    class UserViewHolder(private val view: FragmentFavItemBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(user: User, listener: OnItemClickListener) {
            val context = view.root.context
            with(view) {
                Glide.with(itemImaqe).load(user.avatarUrl).into(itemImaqe)
                itemText.text = user.login
                btFav.setOnClickListener {
                    val icon = btFav.drawable
                    when (icon) {
                        ContextCompat.getDrawable(context, R.drawable.ic_star_half_24) ->
//                            btFav.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_full_24))
                            btFav.setBackgroundResource(R.drawable.ic_star_full_24)
                        ContextCompat.getDrawable(context, R.drawable.ic_star_full_24) ->
//                            btFav.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_half_24))
                            btFav.setBackgroundResource(R.drawable.ic_star_half_24)
                    }
                    listener.onRemoveFavourite(user)
                }
            }
            itemView.setOnClickListener { listener.onItemClick(user) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: User)
        fun onRemoveFavourite(user: User)
    }
}
