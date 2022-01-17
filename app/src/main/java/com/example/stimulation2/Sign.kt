package com.example.stimulation2

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.stimulation2.common.ShowText
import com.example.stimulation2.common.ShowTextLong
import com.example.stimulation2.databinding.ActivitySignBinding
import com.example.stimulation2.sign.SignIn

class Sign : AppCompatActivity() {

    private lateinit var binding: ActivitySignBinding
    private lateinit var o: ActivitySignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.sign_container, SignIn()).commit()

        if (!isNetwork()) ShowTextLong(applicationContext, "Отсутствует интернет подключение")
    }

    private fun isNetwork(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) return true
        return false
    }
}