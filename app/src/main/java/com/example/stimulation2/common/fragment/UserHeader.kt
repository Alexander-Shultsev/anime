package com.example.stimulation2.common.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.stimulation2.common.Global
import com.example.stimulation2.common.Query
import com.example.stimulation2.data.AuthorizationUser
import com.example.stimulation2.databinding.FragmentUserHeaderBinding
import com.example.stimulation2.sign.SignIn
import okhttp3.*
import java.io.IOException

class UserHeader : Fragment() {

    private lateinit var binding: FragmentUserHeaderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserHeaderBinding.inflate(inflater, container, false)

        binding.btnLogout.setOnClickListener { logout() }
        getUserData()

        return binding.root
    }

    private fun logout() {
        Global.token = 0
        Global.type_user = ""
        startActivity(Intent(requireContext(), SignIn::class.java))
//        requireActivity().finish()
    }

    private lateinit var formBody: FormBody
    private lateinit var request: Request
    private lateinit var query: Query
    private var client = OkHttpClient()
    private lateinit var idUser: String

    private fun getUserData() {
        if (Global.id_change_user != 0) {
            idUser = Global.id_change_user.toString()
            binding.btnLogout.isVisible = false
        } else {
            idUser = Global.token.toString()
            binding.btnLogout.isVisible = true
        }

        formBody = FormBody.Builder()
            .add("id_user", idUser)
            .build()

        query = Query()
        request = query.getRequest("user_data", formBody)

        client.newCall(request).enqueue( object: Callback {
            override fun onFailure(call: Call, e: IOException) { Log.i("tag", "oh no") }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    requireActivity().runOnUiThread {
                        putUserData(query.parseJsonUserData(response.body!!.string()))
                    }
                }
            }
        })
    }

    private fun putUserData(data: Array<AuthorizationUser>) {
        binding.userName.text = data[0].first_name
        binding.typeUser.text = "Участник"
        binding.countCoin.text = data[0].money
        binding.countCrown.text = data[0].crown
    }
}