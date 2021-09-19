package com.prototype.exam.ui.main.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prototype.exam.data.model.User
import com.prototype.exam.databinding.ItemLayoutBinding
import com.prototype.exam.ui.main.adapter.MainAdapter.DataViewHolder


class MainAdapter(private val users: ArrayList<User>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<DataViewHolder>() {
    inner class DataViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(user: User) {
            itemView.apply {
                binding.textTemperature.text = user.name
                binding.textLocation.text = user.company.companyName
                binding.textWeather.text = user.address.city
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size

    internal fun getItem(position: Int): User {
        return users[position]
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(users[position])
    }

    fun addUsers(users: List<User>) {
        this.users.apply {
            clear()
            addAll(users)
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}