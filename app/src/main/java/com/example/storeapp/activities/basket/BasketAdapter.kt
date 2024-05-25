package com.example.storeapp.activities.basket

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.ProductEntity
import com.example.storeapp.R
import com.example.storeapp.databinding.ItemBasketBinding

class BasketAdapter(
    private val list: MutableList<ProductEntity>,
    private val clickEvents: BasketClickEvents
) : RecyclerView.Adapter<BasketAdapter.BasketItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketItemViewHolder {
        val binding = ItemBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams

        return BasketItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BasketItemViewHolder, position: Int) {
        holder.bind(position)
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
    }

    inner class BasketItemViewHolder(private val binding: ItemBasketBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val item = list[position]
            Glide.with(itemView.rootView).load(item.image).placeholder(R.drawable.image_stub).into(binding.imageProduct)
            binding.textTitle.text = item.title
            binding.textCount.text = item.count.toString()
            binding.textProductAmount.text = "${item.price * item.count}$"
            binding.buttonPlus.setOnClickListener {
                item.count++
                notifyItemChanged(position)
                clickEvents.increaseCount(item)
            }
            binding.buttonMinus.setOnClickListener {
                if (item.count > 0) {
                    item.count--
                    notifyItemChanged(position)
                    clickEvents.decreaseCount(item, position)
                }
            }
            binding.imageProduct.setOnClickListener {
                clickEvents.onImageClick(item)
            }
            binding.rootLayout.setOnLongClickListener {
                clickEvents.deleteProduct(item, position)
                true
            }
        }
    }

    interface BasketClickEvents {
        fun increaseCount(product: ProductEntity)
        fun decreaseCount(product: ProductEntity, position: Int)
        fun onImageClick(product: ProductEntity)
        fun deleteProduct(product: ProductEntity, position: Int)
    }
}