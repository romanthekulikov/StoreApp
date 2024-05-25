package com.example.storeapp.activities.authorization

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.data.shared_preferences.UserPreferences
import com.example.storeapp.activities.BaseActivity
import com.example.storeapp.activities.product_list.ProductsActivity
import com.example.storeapp.databinding.ActivityAuthorizationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

const val TEXT_LOG_IN = "Or Log In"
const val TEXT_SIGN_IN = "Or Sign In"
const val ERROR_AUTHORIZATION_FAILURE = "Authorization failure, check the password and email are correct"
class AuthorizationActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthorizationBinding
    private lateinit var viewModel: AuthorizationViewModel
    private lateinit var auth: FirebaseAuth

    private val basketResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_CANCELED) {
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, AuthorizationViewModelFactory())[AuthorizationViewModel::class.java]
        auth = Firebase.auth

        binding.textSwitchMode.setOnClickListener {
            viewModel.switchAuthorizationMode()
        }

        binding.buttonAuthorization.setOnClickListener {
            viewModel.checkData()
            if (!viewModel.errorInputDataDetected.value!!) {
                if (viewModel.isSignIn.value!!) {
                    signIn()
                } else {
                    logIn()
                }
            } else {
                showError(ERROR_AUTHORIZATION_FAILURE)
            }
        }

        viewModel.isSignIn.observe(this) { isSignIn ->
            if (isSignIn) {
                binding.editTextPasswordConfirm.visibility = View.VISIBLE
                binding.textSwitchMode.text = TEXT_LOG_IN
            } else {
                binding.editTextPasswordConfirm.visibility = View.GONE
                binding.textSwitchMode.text = TEXT_SIGN_IN
            }
        }
        initEditTextFields()
    }

    private fun signIn() {
        auth.createUserWithEmailAndPassword(viewModel.email.value!!, viewModel.password.value!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                goToProductsActivity()
            } else {
                showError(ERROR_AUTHORIZATION_FAILURE)
            }
        }
    }

    private fun logIn() {
        auth.signInWithEmailAndPassword(viewModel.email.value!!, viewModel.password.value!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                goToProductsActivity()
            } else {
                showError(ERROR_AUTHORIZATION_FAILURE)
            }
        }
    }

    private fun goToProductsActivity() {
        UserPreferences(this).saveLogin(true)
        basketResultLauncher.launch(ProductsActivity.getIntent(this))
    }

    private fun initEditTextFields() {
        binding.editTextEmailLayout.editText?.setText(viewModel.email.value)
        binding.editTextPasswordLayout.editText?.setText(viewModel.password.value)
        binding.editTextPasswordConfirmLayout.editText?.setText(viewModel.confirmPassword.value)
        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /** nothing **/ }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /** nothing **/ }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setEmail(binding.editTextEmail.text.toString())
            }

        })
        binding.editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /** nothing **/ }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /** nothing **/ }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setPassword(binding.editTextPassword.text.toString())
            }

        })
        binding.editTextPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /** nothing **/ }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /** nothing **/ }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setPasswordConfirm(binding.editTextPasswordConfirm.text.toString())
            }
        })
    }

    companion object {
        fun getIntent(fromWhomContext: Context): Intent {
            return Intent(fromWhomContext, AuthorizationActivity::class.java)
        }
    }
}