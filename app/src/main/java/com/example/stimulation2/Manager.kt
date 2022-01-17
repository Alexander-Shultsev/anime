package com.example.stimulation2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stimulation2.common.fragment.UserHeader
import com.example.stimulation2.common.fragment.UserHeaderWithoutNumber
import com.example.stimulation2.databinding.ActivityManagerBinding
import com.example.stimulation2.manager.ManagerWorkerList

class Manager : AppCompatActivity() {

    private lateinit var binding: ActivityManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerBinding.inflate(layoutInflater)

        supportFragmentManager.beginTransaction().replace(R.id.manager_header_container, UserHeaderWithoutNumber()).commit()
        supportFragmentManager.beginTransaction().replace(R.id.manager_container, ManagerWorkerList()).commit()

        setContentView(binding.root)
    }
}