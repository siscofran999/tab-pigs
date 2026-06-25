package com.sisco.tabpigs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sisco.tabpigs.databinding.ItemPlayBinding

class PlayAdapter(): ListAdapter<PlayModel, PlayAdapter.PlayViewHolder>(DIFF_UTIL) {

    private var mListener: ItemClickListener? = null

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<PlayModel>() {
            override fun areItemsTheSame(oldItem: PlayModel, newItem: PlayModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PlayModel, newItem: PlayModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemPlayBinding.inflate(layoutInflater, parent, false)
        return PlayViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlayViewHolder(private val binding: ItemPlayBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlayModel?) {
            if (item?.isShowPig == true) {
                binding.imgPig.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(200)
                    .start()
            }else {
                binding.imgPig.translationY = 150f
                binding.imgPig.alpha = 0f
            }
            binding.root.setOnSingleClickListener {
                if (item != null) {
                    mListener?.onItemClick(item)
                }
            }
        }
    }

    fun itemClickListener(listener: ItemClickListener) {
        mListener = listener
    }

    interface ItemClickListener {
        fun onItemClick(item: PlayModel)
    }
}