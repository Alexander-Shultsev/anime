package com.example.stimulation2.manager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.stimulation2.R
import com.example.stimulation2.common.Global
import com.example.stimulation2.common.Query
import com.example.stimulation2.common.fragment.UserHeader
import com.example.stimulation2.data.ListUser
import com.example.stimulation2.databinding.FragmentManagerWorkerListBinding
import okhttp3.*
import java.io.IOException

class ManagerWorkerList : Fragment() {

    private lateinit var binding: FragmentManagerWorkerListBinding
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentManagerWorkerListBinding.inflate(inflater, container, false)

        getUsers()

        adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item
        )
        binding.list.adapter = adapter

        binding.list.setOnItemClickListener { adapterView, view, i, l ->
            goToManagerAchievementActivity(l.toInt())
        }

        return binding.root
    }


    private fun goToManagerAchievementActivity(id: Int) {
        Global.id_change_user = data[id].id_user.toInt()
        parentFragmentManager.beginTransaction().replace(R.id.manager_header_container, UserHeader()).commit()
        parentFragmentManager.beginTransaction().replace(R.id.manager_container, ManagerChangeWorker()).commit()
    }

    private lateinit var request: Request
    private lateinit var query: Query
    private var client = OkHttpClient()
    private lateinit var data: Array<ListUser>

    private fun getUsers() {
        query = Query()
        request = query.getRequest("all_user")

        client.newCall(request).enqueue( object: Callback {
            override fun onFailure(call: Call, e: IOException) { Log.i("tag", "oh no") }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    requireActivity().runOnUiThread {
                        data = query.parseJsonUses(response.body!!.string())
                        putUsersInList(data)
                    }
                }
            }
        })
    }

    private fun putUsersInList(data: Array<ListUser>) {
        for(i in data.indices) {
            adapter.add("\t\t#${i+1}  ${data[i].first_name}")
        }
    }

//    companion object {
//
//        @JvmStatic
//        val arg_id_user_for_achievement = "id_user_for_achievement"
//
//        fun newInstance(id_user_for_achievement: Int) {
//            WorkerBonus().apply {
//                arguments = Bundle().apply {
//                    putInt(arg_id_user_for_achievement, id_user_for_achievement)
//                }
//            }
//        }
//    }
}