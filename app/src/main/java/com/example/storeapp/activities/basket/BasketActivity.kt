package com.example.storeapp.activities.basket

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.ProductEntity
import com.example.storeapp.activities.BaseActivity
import com.example.storeapp.databinding.ActivityBasketBinding
import com.example.storeapp.dialogs.CardDialog
import com.example.storeapp.dialogs.WarningDialog
import kotlinx.coroutines.launch

const val TAG_PRODUCT_CARD = "product_card"
const val MESSAGE_WARNING_REMOVE_PRODUCT = "You want to remove a product from your basket?"

class BasketActivity : BaseActivity(), BasketAdapter.BasketClickEvents {
    private lateinit var binding: ActivityBasketBinding
    private lateinit var viewModel: BasketViewModel
    private lateinit var adapter: BasketAdapter

    private val onBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, BasketViewModelFactory())[BasketViewModel::class.java]

        launch {
            viewModel.getData()
            initRecyclerView()
        }

        viewModel.currentAmount.observe(this) {
            binding.textAmount.text = String.format("%.2f$", it)
        }

        binding.buttonOrder.setOnClickListener {
            launch{ viewModel.makeOrder() }
            binding.recyclerBasket.visibility = View.GONE
            binding.textOrderStatus.visibility = View.VISIBLE
        }

        onBackPressedDispatcher.addCallback(onBackPressed)
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this@BasketActivity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        adapter = BasketAdapter(viewModel.list.toMutableList(), this@BasketActivity)
        binding.recyclerBasket.layoutManager = linearLayoutManager
        binding.recyclerBasket.adapter = adapter
        binding.recyclerBasket.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val position = linearLayoutManager.findFirstVisibleItemPosition()
                if (position >= 0) {
                    viewModel.setScrollPosition(position)
                }
            }
        })
        binding.recyclerBasket.scrollToPosition(viewModel.scrollPositionList)
    }

    override fun increaseCount(product: ProductEntity) {
        launch { viewModel.increaseCount(product) }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun decreaseCount(product: ProductEntity, position: Int) {
        if (product.count == 0) {
            product.count++
            launch { viewModel.deleteProduct(product) }
            adapter.removeItem(position)
            adapter.notifyDataSetChanged()
        } else {
            launch { viewModel.decreaseCount(product) }
        }
    }

    override fun onImageClick(product: ProductEntity) {
        CardDialog(product).show(supportFragmentManager, TAG_PRODUCT_CARD)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun deleteProduct(product: ProductEntity, position: Int) {
        WarningDialog.Builder
            .setMessage(MESSAGE_WARNING_REMOVE_PRODUCT)
            .setAction {
                adapter.removeItem(position)
                adapter.notifyDataSetChanged()
                launch { viewModel.deleteProduct(product) }
            }
            .build().show(supportFragmentManager, TAG_PRODUCT_CARD)
    }

    companion object {
        fun getIntent(fromWhomContext: Context): Intent {
            return Intent(fromWhomContext, BasketActivity::class.java)
        }
    }
}