package com.example.stimulation2.worker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import com.example.stimulation2.R
import com.example.stimulation2.common.Query
import com.example.stimulation2.data.Rating
import com.example.stimulation2.databinding.FragmentWorkerHistoryBinding
import okhttp3.*
import java.io.IOException

class WorkerHistory : Fragment() {

    private lateinit var binding: FragmentWorkerHistoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWorkerHistoryBinding.inflate(inflater, container, false)

        return binding.root
    }
}