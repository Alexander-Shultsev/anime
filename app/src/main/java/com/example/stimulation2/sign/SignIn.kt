package com.example.stimulation2.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stimulation2.Manager
import com.example.stimulation2.R
import com.example.stimulation2.Worker
import com.example.stimulation2.common.Global
import com.example.stimulation2.common.Query
import com.example.stimulation2.common.ShowText
import com.example.stimulation2.data.TokenAndTypeUser
//import com.example.stimulation2.common.ChangeFragment
import com.example.stimulation2.databinding.FragmentSignInBinding
import okhttp3.*
import java.io.IOException

class SignIn : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.btnGoToSignUp.setOnClickListener { goToSignUpScreen() }
        binding.btnLogin.setOnClickListener { validation() }

        return binding.root
    }


    private lateinit var login: String
    private lateinit var password: String

    private fun validation() {
        login = binding.editTextLogin.text.toString()
        password = binding.editTextPassword.text.toString()

        if (!login.isEmpty() and !password.isEmpty())
            signIn()
        else
            ShowText(requireContext(), "Заполните все данные")
    }


    private lateinit var formBody: FormBody
    private lateinit var request: Request
    private lateinit var query: Query
    private var client = OkHttpClient()

    private fun signIn() {
        formBody = FormBody.Builder()
            .add("login", login)
            .add("password", password)
            .build()

        query = Query()
        request = query.getRequest("type_user", formBody)

        client.newCall(request).enqueue( object: Callback {
            override fun onFailure(call: Call, e: IOException) { Log.i("tag", "oh no") }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) goToUserScreen(query.parseJsonTypeUser(response.body!!.string()))
                else requireActivity().runOnUiThread { ShowText(requireContext(), "Неверный логин или пароль") }
            }
        })
    }

    private fun goToUserScreen(user: Array<TokenAndTypeUser>) {
        Global.token = user[0].id_user.toInt()

        if (user[0].type_user == "Worker") {
            Global.type_user = "Сотрудник"
            startActivity(Intent(requireContext(), Worker::class.java))
        } else {
            Global.type_user = "Менеджер"
            startActivity(Intent(requireContext(), Manager::class.java))
        }
//            requireActivity().finish()
    }

    private fun goToSignUpScreen() {
        parentFragmentManager.beginTransaction().replace(R.id.sign_container, SignUp()).commit()
    }
}