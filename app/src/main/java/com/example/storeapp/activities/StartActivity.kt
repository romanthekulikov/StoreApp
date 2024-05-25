package com.example.storeapp.activities

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.data.shared_preferences.UserPreferences
import com.example.storeapp.activities.authorization.AuthorizationActivity
import com.example.storeapp.activities.product_list.ProductsActivity
import com.example.storeapp.databinding.ActivitySplashBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_CANCELED) {
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({
            if (UserPreferences(this).loggedIn()) {
                resultLauncher.launch(ProductsActivity.getIntent(this))
            } else {
                resultLauncher.launch(AuthorizationActivity.getIntent(this))
            }
        }, 3000)
    }
}