package com.example.stimulation2.worker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.stimulation2.R
import com.example.stimulation2.common.Query
import com.example.stimulation2.data.Rating
import com.example.stimulation2.databinding.FragmentWorkerHistoryBinding
import com.example.stimulation2.databinding.FragmentWorkerRatingBinding
import okhttp3.*
import java.io.IOException

class WorkerRating : Fragment() {

    private lateinit var binding: FragmentWorkerRatingBinding
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWorkerRatingBinding.inflate(inflater, container, false)

        adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item
        )
        binding.list.adapter = adapter

        /* Заполение listview данными */
        getBonus()

        return binding.root
    }

    private lateinit var request: Request
    private lateinit var query: Query
    private var client = OkHttpClient()

    private fun getBonus() {
        query = Query()
        request = query.getRequest("rating")

        client.newCall(request).enqueue( object: Callback {
            override fun onFailure(call: Call, e: IOException) { Log.i("tag", "oh no") }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    requireActivity().runOnUiThread {
                        putRatingInList(query.parseJsonRating(response.body!!.string()))
                    }
                }
            }
        })
    }

    private fun putRatingInList(data: Array<Rating>) {
        for(i in data.indices) {
            adapter.add("\t\t${data[i].first_name} : ${data[i].crown}")
        }
    }
}