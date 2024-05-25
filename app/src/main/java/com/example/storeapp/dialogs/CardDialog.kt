package com.example.storeapp.dialogs

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.domain.entity.Product
import com.example.storeapp.databinding.DialogProductCardBinding

class CardDialog(private val product: Product) : DialogFragment() {
    private lateinit var binding: DialogProductCardBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogProductCardBinding.inflate(layoutInflater)
        binding.buttonBack.setOnClickListener {
            dismiss()
        }
        Glide.with(this).load(product.image).into(binding.productCard.productImage)
        binding.productCard.textProductTitle.text = product.title
        binding.productCard.textPrice.text = "${product.price}$"
        binding.productCard.description.text = product.description
        binding.productCard.description.movementMethod = ScrollingMovementMethod()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // make rounded dialog

        return binding.root
    }
}