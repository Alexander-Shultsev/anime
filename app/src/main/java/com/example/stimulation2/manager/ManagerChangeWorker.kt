package com.example.stimulation2.manager

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stimulation2.R
import com.example.stimulation2.common.Global
import com.example.stimulation2.common.Query
import com.example.stimulation2.common.ShowText
import com.example.stimulation2.common.fragment.UserHeader
import com.example.stimulation2.common.fragment.UserHeaderWithoutNumber
import com.example.stimulation2.data.CrownAndMoney
import com.example.stimulation2.databinding.FragmentManagerChangeWorkerBinding
import okhttp3.*
import java.io.IOException

class ManagerChangeWorker : Fragment() {

    private lateinit var binding: FragmentManagerChangeWorkerBinding
    private lateinit var dialog: AlertDialog.Builder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentManagerChangeWorkerBinding.inflate(inflater, container, false)

        binding.btnChangeWorkerNumbers.setOnClickListener { changeWorker() }
        binding.btnGoBack.setOnClickListener { goBack() }
        binding.btnMakeManagerFromWorker.setOnClickListener { createDialog() }

        dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Подтверждение")

        dialog.setPositiveButton("Да"){ dialog, i ->
            makeManagerFromWorker()
        }
        dialog.setNegativeButton("Нет"){ dialog, i -> }

        return binding.root
    }

    private fun createDialog() {
        dialog.setMessage("Вы хотите сделать этого пользователя менеджером?")
        dialog.show()
    }

    private lateinit var formBody: FormBody
    private lateinit var request: Request
    private lateinit var query: Query
    private var client = OkHttpClient()

    private fun makeManagerFromWorker() {
        crown = binding.editTextCrown.text.toString()
        money = binding.editTextMoney.text.toString()

        formBody = FormBody.Builder()
            .add("UID", Global.id_change_user.toString())
            .build()

        query = Query()
        request = query.getRequest("uptomanager", formBody)

        client.newCall(request).enqueue( object: Callback {
            override fun onFailure(call: Call, e: IOException) { Log.i("tag", "oh no") }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    requireActivity().runOnUiThread {
                        ShowText(requireContext(), "Теперь этот пользователь - менеджер")
                    }
                    parentFragmentManager.beginTransaction().replace(R.id.manager_container, ManagerWorkerList()).commit()
                    parentFragmentManager.beginTransaction().replace(R.id.manager_header_container, UserHeaderWithoutNumber()).commit()
                }
                else requireActivity().runOnUiThread { ShowText(requireContext(), "Не удалось сделать пользователя менеджером") }
            }
        })
    }

    private lateinit var crown: String
    private lateinit var money: String

    private fun changeWorker() {
        crown = binding.editTextCrown.text.toString()
        money = binding.editTextMoney.text.toString()

        if (!(crown.isEmpty() && money.isEmpty())) {
            if (crown.isEmpty()) crown = "0"
            if (money.isEmpty()) money = "0"

            formBody = FormBody.Builder()
                .add("UID", Global.id_change_user.toString())
                .add("crown", crown)
                .add("money", money)
                .build()

            query = Query()
            request = query.getRequest("ucmch", formBody)

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.i("tag", "oh no")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code == 200) {
                        requireActivity().runOnUiThread {
                            ShowText(requireContext(), "Данные обновены")
                            //
                            //                        changeCrownAndMoney(query.parseJsonCrownAndMoney(response.body!!.string()))
                        }
                        changeCrownAndMoney()
                    }
                    else
                        requireActivity().runOnUiThread {
                            ShowText(requireContext(), "Не удалось изменить данные")
                        }
                }
            })
        } else ShowText(requireContext(), "Заполните данные")
    }

    private fun changeCrownAndMoney() {
        parentFragmentManager.beginTransaction().replace(R.id.manager_header_container, UserHeader()).commit()
    }

    private fun goBack() {
        Global.id_change_user = 0
        parentFragmentManager.beginTransaction().replace(R.id.manager_header_container, UserHeaderWithoutNumber()).commit()
        parentFragmentManager.beginTransaction().replace(R.id.manager_container, ManagerWorkerList()).commit()
    }

}