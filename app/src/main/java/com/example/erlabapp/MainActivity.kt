package com.example.erlabapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.erlabapp.viewmodel.DeviceVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: DeviceVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getResultFromJNIForRAuth.observe(this, {
            if (it.data.isNullOrEmpty()) {
                Log.i("TAG","")
            } else {
                Log.i("", it.data)
            }
        })
    }
}