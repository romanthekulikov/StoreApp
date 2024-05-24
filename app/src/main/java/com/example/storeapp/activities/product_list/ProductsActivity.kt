package com.example.storeapp.activities.product_list

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.database.AppDatabase
import com.example.data.database.entities.ProductEntity
import com.example.domain.entity.Product
import com.example.storeapp.R
import com.example.storeapp.activities.BaseActivity
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
    private lateinit var binding: ActivityProductsBinding
    private lateinit var viewModel: ProductsViewModel
    private lateinit var adapter: ProductsAdapter
    private var recyclerViewState: Parcelable? = null
    private val basket = mutableListOf<Product>()
    private val db = AppDatabase.db

    init {
        launch { basket.addAll(db.basketDao().getOrder()) }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ProductsViewModelFactory().create(ProductsViewModel::class.java)

        observeViewModel()
        binding.amountButton.setOnClickListener {
            if (viewModel.errorLoadDataFlag.value!!) {
                getData()
            } else {
                // TODO: Goto basket activity
            }
        }

        getData()
        initRecyclerView()
    }

    private fun observeViewModel() {
        viewModel.productList.observe(this) {
            initRecyclerView()
        }
        viewModel.currentAmountText.observe(this) {
            binding.amountButton.text = String.format("Amount: %.2f$", viewModel.currentAmountText.value)
        }
        viewModel.errorLoadDataFlag.observe(this) {
            if (it) {
                binding.textEmptyList.visibility = View.VISIBLE
                binding.amountButton.text = getString(R.string.try_again)
            } else {
                binding.textEmptyList.visibility = View.GONE
                binding.amountButton.text = String.format("Amount: %.2f$", viewModel.currentAmountText.value)
            }
        }
    }

    private fun getData() {
        launch {
            //binding.progressBar.visibility = View.VISIBLE
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
            //binding.progressBar.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerProductList.layoutManager = linearLayoutManager
        adapter = ProductsAdapter(viewModel.productList.value!!, this)
        binding.recyclerProductList.adapter = adapter
        binding.recyclerProductList.layoutManager?.onRestoreInstanceState(recyclerViewState)
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
            inBasket = product in basket
        )
        viewModel.selectedProduct = product
        cardResultLauncher.launch(intent)
    }

    override fun moveToBasket(product: Product) {
        val filteredBasket = basket.filter { it.title == product.title }
        if (filteredBasket.isEmpty()) {

            //TODO: Basket to VM
            basket.add(product)
            launch { db.basketDao().addProduct(ProductEntity(product, 0)) }
            launch { viewModel.moveToBasket(product) }
            Toast.makeText(this, MESSAGE_PRODUCT_MOVE_TO_BASKET, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, MESSAGE_ALREADY_IN_BASKET, Toast.LENGTH_SHORT).show()
        }
    }
}