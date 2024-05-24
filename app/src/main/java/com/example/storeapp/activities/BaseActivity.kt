package com.example.storeapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.storeapp.R
import com.example.storeapp.dialogs.ErrorDialog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

const val TAG_ERROR_DIALOG = "error"

open class BaseActivity : AppCompatActivity(), CoroutineScope {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("fatal", throwable.stackTraceToString())
    }
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO + coroutineExceptionHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    protected fun showError(errorMsg: String) {
        ErrorDialog.Builder()
            .setMessage(errorMsg)
            .build()
            .show(supportFragmentManager, TAG_ERROR_DIALOG)
    }
}