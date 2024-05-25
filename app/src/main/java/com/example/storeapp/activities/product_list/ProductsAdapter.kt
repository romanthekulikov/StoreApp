package com.example.storeapp.activities.product_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.entity.Product
import com.example.storeapp.R
import com.example.storeapp.databinding.ItemProductBinding
import com.example.storeapp.databinding.UnitProductItemBinding
import kotlin.math.round

class ProductsAdapter(
    private var list: List<Product>,
    private val clickEvents: ProductClickEvents
): RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {
    private var leftList = mutableListOf<Product>()
    private var rightList = mutableListOf<Product>()
    init {
        splitList()
    }

    private fun splitList() {
        list.forEachIndexed { index, product ->
            if (index % 2 == 0) {
                leftList.add(product)
            } else {
                rightList.add(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context))
        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams

        return ProductsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return round(list.size.toDouble() / 2).toInt()
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        if (leftList.size > rightList.size && position == rightList.size) {
            holder.bind(leftList[position])
        } else {
            holder.bind(leftList[position], rightList[position])
        }
    }

    inner class ProductsViewHolder(private val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(itemLeft: Product, itemRight: Product? = null) {
            bindSide(itemLeft, binding.leftProduct)
            if (itemRight == null) {
                binding.rightProduct.rootItemLayout.visibility = View.GONE
            } else {
                binding.rightProduct.rootItemLayout.visibility = View.VISIBLE
                bindSide(itemRight, binding.rightProduct)
            }
        }

        @SuppressLint("SetTextI18n")
        private fun bindSide(item: Product, sideBinding: UnitProductItemBinding) {
            Glide.with(this.itemView).load(item.image).placeholder(R.drawable.image_stub).into(sideBinding.imageProduct)
            sideBinding.textPriceProduct.text = "${item.price}$"
            sideBinding.textTitleProduct.text = item.title
            sideBinding.rootItemLayout.setOnClickListener {
                clickEvents.onClick(item)
            }
            sideBinding.imageBasket.setOnClickListener {
                clickEvents.moveToBasket(item)
            }
        }
    }

    interface ProductClickEvents {
        fun onClick(product: Product)
        fun moveToBasket(product: Product)
    }
}