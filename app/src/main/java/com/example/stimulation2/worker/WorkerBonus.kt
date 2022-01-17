package com.example.stimulation2.worker

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import com.example.stimulation2.R
import com.example.stimulation2.common.Global
import com.example.stimulation2.common.Query
import com.example.stimulation2.common.ShowText
import com.example.stimulation2.common.fragment.UserHeader
import com.example.stimulation2.data.Bonus
import com.example.stimulation2.data.ListUser
import com.example.stimulation2.data.Rating
import com.example.stimulation2.databinding.FragmentWorkerBonusBinding
import com.example.stimulation2.manager.ManagerWorkerList
import okhttp3.*
import java.io.IOException
import java.time.Instant

class WorkerBonus : Fragment() {

    private lateinit var binding: FragmentWorkerBonusBinding
    private lateinit var bonusList: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dialog : AlertDialog.Builder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWorkerBonusBinding.inflate(inflater, container, false)

        adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item
        )
        binding.list.adapter = adapter

        /* Заполение listview данными */
        getBonuses()

        /* Создание диалогового окна */
        dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Покупка")
        dialog.setNegativeButton("Нет"){ dialogInterface, i -> }

        return binding.root
    }

    private lateinit var request: Request
    private lateinit var query: Query
    private var client = OkHttpClient()
    private lateinit var formBody: FormBody

    private fun getBonuses() {
        query = Query()
        request = query.getRequest("bonuses")

        client.newCall(request).enqueue( object: Callback {
            override fun onFailure(call: Call, e: IOException) { Log.i("tag", "oh no") }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    requireActivity().runOnUiThread {
                        putUsersInList(query.parseJsonBonus(response.body!!.string()))
                    }
                }
            }
        })
    }

    private fun putUsersInList(data: Array<Bonus>) {
        for(i in data.indices) {
            adapter.add("\t\t${data[i].name_bonus} / ${data[i].price}")
        }

        binding.list.setOnItemClickListener { adapterView, view, i, l ->
            showMessage(data, l.toInt())
        }
    }

    private fun showMessage(data: Array<Bonus>, numberListItem: Int) {
        dialog.setMessage("Вы ходите приобрести: \"" + data[numberListItem].name_bonus + "\" за " + data[numberListItem].price)
        dialog.setPositiveButton("Да"){ dialogInterface, i ->
            buyBonus(data[numberListItem].id_bonus)
        }
        dialog.show()
    }


    private fun buyBonus(id_bonus: String) {
        Log.i("tag", "buyBonus: ${id_bonus}")
        formBody = FormBody.Builder()
            .add("UID", Global.token.toString())
            .add("BID", id_bonus)
            .build()

        query = Query()
        request = query.getRequest("trade", formBody)

        client.newCall(request).enqueue( object: Callback {
            override fun onFailure(call: Call, e: IOException) { Log.i("tag", "oh no") }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    requireActivity().runOnUiThread {
                        ShowText(requireContext(), "Поздравляем с покупкой")
                    }
                    parentFragmentManager.beginTransaction().replace(R.id.worker_header_container, UserHeader()).commit()
                }
                else requireActivity().runOnUiThread { ShowText(requireContext(), "Не удалось совершить покупку") }
            }
        })
    }
}