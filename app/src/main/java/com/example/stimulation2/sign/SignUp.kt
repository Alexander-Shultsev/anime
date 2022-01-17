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
import com.example.stimulation2.common.Query
import com.example.stimulation2.common.ShowText
import com.example.stimulation2.databinding.FragmentSignUpBinding
import okhttp3.*
import java.io.IOException

class SignUp : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.btnGoToSignIn.setOnClickListener { goToSignInScreen() }
        binding.btnCreateAccount.setOnClickListener { validation() }

        return binding.root
    }

    private lateinit var firstName: String
    private lateinit var login: String
    private lateinit var password: String
    private lateinit var repeatPassword: String

    private fun validation() {
        firstName = binding.editTextFirstName.text.toString()
        login = binding.editTextLogin.text.toString()
        password = binding.editTextPassword.text.toString()
        repeatPassword = binding.editTextRepeatPassword.text.toString()

        if (firstName.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty())
            if (password == repeatPassword)
                createAccount(firstName, login, password)
            else ShowText(requireContext(), "Парои не совпадают")
        else ShowText(requireContext(), "Заполните все данные")
    }

    private lateinit var formBody: FormBody
    private lateinit var request: Request
    private lateinit var query: Query
    private var client = OkHttpClient()

    private fun createAccount(val_firstName: String, val_login:String, val_password:String) {
        formBody = FormBody.Builder()
            .add("login", login)
            .add("password", password)
            .add("first_name", repeatPassword)
            .build()

        query = Query()
        request = query.getRequest("reg", formBody)

        client.newCall(request).enqueue( object: Callback {
            override fun onFailure(call: Call, e: IOException) { Log.i("tag", "oh no") }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 201) {
                    requireActivity().runOnUiThread { goToUserScreen() }
                } else
                    requireActivity().runOnUiThread { ShowText(requireContext(), "Пользователь с таким именем уже существует") }
            }
        })
    }

    private fun goToUserScreen() {
        ShowText(requireContext(), "Регистрация прошла успешно")
        parentFragmentManager.beginTransaction().replace(R.id.sign_container, SignIn()).commit()
    }

    private fun goToSignInScreen() {
        parentFragmentManager.beginTransaction().replace(R.id.sign_container, SignIn()).commit()
    }
}