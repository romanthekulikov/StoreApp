package com.example.storeapp.activities.product_list

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Product
import com.example.storeapp.R
import com.example.storeapp.activities.BaseActivity
import com.example.storeapp.activities.basket.BasketActivity
import com.example.storeapp.activities.product_card.ProductCardActivity
import com.example.storeapp.activities.product_card.ProductCardActivity.Companion.IN_BASKET_KEY
import com.example.storeapp.databinding.ActivityProductsBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


const val ERROR_NO_INTERNET = "No internet"
const val ERROR_UNIDENTIFIED = "Unidentified error"
const val ERROR_SERVICE_IS_SHUTDOWN = "Service is shutdown"
const val ERROR_HTTP = "Http error"

const val MESSAGE_PRODUCT_MOVE_TO_BASKET = "Product move to basket"
const val MESSAGE_ALREADY_IN_BASKET = "Product already in basket"

class ProductsActivity : BaseActivity(), ProductsAdapter.ProductClickEvents {
    private val cardResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val movedToBasket = result.data?.extras?.getBoolean(IN_BASKET_KEY) ?: true
            if (movedToBasket) {
                moveToBasket(viewModel.selectedProduct)
            }
        }
    }

    private val basketResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            launch { viewModel.getBasket() }
        }
    }
    private lateinit var binding: ActivityProductsBinding
    private lateinit var viewModel: ProductsViewModel
    private lateinit var adapter: ProductsAdapter
    private var recyclerViewState: Parcelable? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ProductsViewModelFactory())[ProductsViewModel::class.java]

        observeViewModel()
        binding.amountButton.setOnClickListener {
            if (viewModel.errorLoadDataFlag.value!!) {
                getData()
            } else {
                basketResultLauncher.launch(BasketActivity.getIntent(this))
            }
        }
        binding.imageBasket.setOnClickListener {
            basketResultLauncher.launch(BasketActivity.getIntent(this))
        }

        getData()
        initRecyclerView()
    }

    private fun observeViewModel() {
        viewModel.productList.observe(this) {
            initRecyclerView()
        }
        viewModel.currentAmount.observe(this) {
            binding.amountButton.text = String.format("Amount: %.2f$", viewModel.currentAmount.value)
        }
        viewModel.errorLoadDataFlag.observe(this) {
            if (it) {
                binding.textEmptyList.visibility = View.VISIBLE
                binding.amountButton.text = getString(R.string.try_again)
            } else {
                binding.textEmptyList.visibility = View.GONE
                binding.amountButton.text = String.format("Amount: %.2f$", viewModel.currentAmount.value)
            }
        }
    }

    private fun getData() {
        launch {
            binding.layoutProgressBar.visibility = View.VISIBLE
            try {
                viewModel.getData()
            } catch (exception: UnknownHostException) {
                showError(ERROR_NO_INTERNET)
                viewModel.errorLoadDetect()
            } catch (exception: HttpException) {
                showError(ERROR_HTTP)
                viewModel.errorLoadDetect()
            } catch (exception: SocketTimeoutException) {
                showError(ERROR_SERVICE_IS_SHUTDOWN)
                viewModel.errorLoadDetect()
            } catch (exception: IOException) {
                showError(ERROR_UNIDENTIFIED)
                viewModel.errorLoadDetect()
            }
            binding.layoutProgressBar.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerProductList.layoutManager = linearLayoutManager
        adapter = ProductsAdapter(viewModel.productList.value!!, this)
        binding.recyclerProductList.adapter = adapter
        binding.recyclerProductList.layoutManager?.onRestoreInstanceState(recyclerViewState)
        binding.recyclerProductList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val position = linearLayoutManager.findFirstVisibleItemPosition()
                if (position > 0) {
                    viewModel.setScrollPosition(position)
                }
            }
        })
        binding.recyclerProductList.scrollToPosition(viewModel.scrollPositionList)
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerViewState = binding.recyclerProductList.layoutManager?.onSaveInstanceState()
    }

    override fun onClick(product: Product) {
        val intent = ProductCardActivity.createIntent(
            fromWhomContext = this,
            image = product.image,
            title = product.title,
            price = product.price,
            description = product.description,
            inBasket = product in viewModel.basket
        )
        viewModel.setSelectedProduct(product)
        cardResultLauncher.launch(intent)
    }

    override fun moveToBasket(product: Product) {
        val filteredBasket = viewModel.basket.filter { it.title == product.title }
        if (filteredBasket.isEmpty()) {
            launch { viewModel.moveToBasket(product) }
            Toast.makeText(this, MESSAGE_PRODUCT_MOVE_TO_BASKET, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, MESSAGE_ALREADY_IN_BASKET, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun getIntent(fromWhomContext: Context): Intent {
            return Intent(fromWhomContext, ProductsActivity::class.java)
        }
    }
}