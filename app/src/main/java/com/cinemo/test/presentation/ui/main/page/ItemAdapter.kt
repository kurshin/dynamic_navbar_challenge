package com.cinemo.test.presentation.ui.main.page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinemo.test.databinding.ItemListBinding
import com.cinemo.test.databinding.ItemListVerticalBinding
import com.cinemo.test.domain.GRID_TYPE
import com.cinemo.test.domain.Item

class ItemAdapter(private val displayStyle: String) : ListAdapter<Item, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val VIEW_TYPE_VERTICAL = 1
        private const val VIEW_TYPE_HORIZONTAL = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (displayStyle == GRID_TYPE) VIEW_TYPE_VERTICAL else VIEW_TYPE_HORIZONTAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_VERTICAL -> {
                val binding = ItemListVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemVerticalViewHolder(binding)
            }
            VIEW_TYPE_HORIZONTAL -> {
                val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ItemViewHolder -> holder.bind(item)
            is ItemVerticalViewHolder -> holder.bind(item)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    class ItemViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.title.text = item.title
            binding.subtitle.text = item.subtitle
            Glide.with(itemView.context)
                .load(item.thumbnail)
                .into(binding.thumbnail)
        }
    }

    class ItemVerticalViewHolder(private val binding: ItemListVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.title.text = item.title
            binding.subtitle.text = item.subtitle
            Glide.with(itemView.context)
                .load(item.thumbnail)
                .into(binding.thumbnail)
        }
    }
}


