package com.peterstev.lagosdevs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.peterstev.domain.model.User
import com.peterstev.lagosdevs.databinding.FragmentMainItemBinding
import com.peterstev.lagosdevs.databinding.LoadMoreProgressBinding

class UserAdapter(
    private val items: MutableList<User>,
    private val listener: OnItemClickListener,
    recyclerView: RecyclerView,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var totalItems = 0
    private var lastVisibleItemsPosition = -1
    private var itemThreshold = 5
    private var isLoading: Boolean = false

    companion object {
        private const val ITEM_VIEW = 0
        private const val LOADING_VIEW = 1
    }

    init {
        val manager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItems = manager.itemCount
                lastVisibleItemsPosition = manager.findLastVisibleItemPosition()
                if (!isLoading && totalItems <= (lastVisibleItemsPosition + itemThreshold)) {
                    listener.loadMore()
                    isLoading = true
                }
            }
        })
    }

    fun stopLoading() {
        isLoading = false
    }

    fun getList(): MutableList<User> = items

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW;
//        return when (position) {
//            (items.size - 1) -> LOADING_VIEW
//            else -> ITEM_VIEW
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW -> {
                val binding = FragmentMainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UserViewHolder(binding)
            }
            else -> {
                val binding = LoadMoreProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserViewHolder -> holder.bind(items[position], listener)
            is LoadingViewHolder -> holder.bind()
        }
    }

    fun updateList(list: MutableList<User>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    class UserViewHolder(private val view: FragmentMainItemBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(user: User, listener: OnItemClickListener) {
            Glide.with(view.itemImaqe).load(user.avatarUrl).into(view.itemImaqe)
            view.itemText.text = user.login
            itemView.setOnClickListener { listener.onItemClick(user) }
        }
    }

    class LoadingViewHolder(private val view: LoadMoreProgressBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind() {
            view.loadMore.visibility = View.VISIBLE
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: User)
        fun loadMore()
    }
}
