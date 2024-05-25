package com.example.storeapp.activities.product_card

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.example.domain.entity.Product
import com.example.domain.ProductEntity
import com.example.storeapp.R
import com.example.storeapp.activities.BaseActivity
import com.example.storeapp.activities.product_list.MESSAGE_ALREADY_IN_BASKET
import com.example.storeapp.activities.product_list.MESSAGE_PRODUCT_MOVE_TO_BASKET
import com.example.storeapp.databinding.ActivityProductCardBinding
import kotlinx.coroutines.launch

class ProductCardActivity : BaseActivity() {
    private lateinit var binding: ActivityProductCardBinding
    private lateinit var viewModel: ProductCardViewModel

    private val onBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent()
            intent.putExtra(IN_BASKET_KEY, viewModel.inBasket)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inBasket = intent.extras?.getBoolean(IN_BASKET_KEY) ?: true
        viewModel = ProductCardViewModelFactory(getProductFromExtras(), inBasket).create(ProductCardViewModel::class.java)

        binding.buttonMove.setOnClickListener {
            if (viewModel.inBasket) {
                Toast.makeText(this, MESSAGE_ALREADY_IN_BASKET, Toast.LENGTH_SHORT).show()
            } else {
                moveToBasket()
                Toast.makeText(this, MESSAGE_PRODUCT_MOVE_TO_BASKET, Toast.LENGTH_SHORT).show()
            }
        }
        binding.buttonBack.setOnClickListener {
            onBackPressed.handleOnBackPressed()
        }
        setProductView()
        onBackPressedDispatcher.addCallback(onBackPressed)
    }

    private fun moveToBasket() {
        launch { viewModel.moveToBasket() }
        viewModel.inBasket = true
    }

    private fun getProductFromExtras(): Product {
        val extras = intent.extras
        return ProductEntity(
            image = extras?.getString(IMAGE_KEY) ?: "",
            title = extras?.getString(TITLE_KEY) ?: "",
            price = extras?.getDouble(PRICE_KEY) ?: 0.0,
            description = extras?.getString(DESCRIPTION_KEY) ?: "",
            count = 0
        )
    }

    private fun setProductView() {
        binding.productCard.description.text = viewModel.product.description
        binding.productCard.description.movementMethod = ScrollingMovementMethod()
        Glide.with(this)
            .load(viewModel.product.image)
            .placeholder(R.drawable.image_stub)
            .into(binding.productCard.productImage)
        binding.productCard.textProductTitle.setTypeface(null, Typeface.BOLD)
        binding.productCard.textProductTitle.text = viewModel.product.title
        binding.productCard.textPrice.text = String.format("${viewModel.product.price}$")
    }

    companion object {
        private const val IMAGE_KEY = "image_key"
        private const val TITLE_KEY = "title_key"
        private const val PRICE_KEY = "price_key"
        private const val DESCRIPTION_KEY = "description_key"
        const val IN_BASKET_KEY = "in_basket_key"
        fun createIntent(
            fromWhomContext: Context,
            image: String,
            title: String,
            price: Double,
            description: String,
            inBasket: Boolean
        ): Intent {
            val intent = Intent(fromWhomContext, ProductCardActivity::class.java)
            intent.putExtra(IMAGE_KEY, image)
            intent.putExtra(TITLE_KEY, title)
            intent.putExtra(PRICE_KEY, price)
            intent.putExtra(DESCRIPTION_KEY, description)
            intent.putExtra(IN_BASKET_KEY, inBasket)

            return intent
        }
    }
}