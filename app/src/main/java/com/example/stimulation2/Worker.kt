package com.example.stimulation2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.stimulation2.common.fragment.UserHeader
import com.example.stimulation2.databinding.ActivityMainBinding
import com.example.stimulation2.worker.WorkerBonus
import com.example.stimulation2.worker.WorkerHistory
import com.example.stimulation2.worker.WorkerRating

class Worker : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        supportFragmentManager.beginTransaction().replace(R.id.worker_header_container, UserHeader()).commit()
        supportFragmentManager.beginTransaction().replace(R.id.worker_container, WorkerBonus()).commit()

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setMenuSelectItem()
    }

    private fun setMenuSelectItem () {
        binding.workerMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_bonus -> {
                    changeFragment(WorkerBonus())
                    true
                }
                R.id.menu_item_rating -> {
                    changeFragment(WorkerRating())
                    true
                }
//                R.id.menu_item_history -> {
//                    changeFragment(WorkerHistory())
//                    true
//                }
                else -> false
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.worker_container, fragment).commit()
    }
}