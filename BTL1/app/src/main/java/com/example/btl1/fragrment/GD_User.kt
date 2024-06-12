package com.example.btl1.fragrment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.btl1.R
import com.example.btl1.activity.LoginActivity
import com.example.btl1.dal.SQLiteHelper


class GD_User : Fragment() {

    private var imgout : ImageView? = null
    // TODO: Rename and change types of parameters
    private lateinit var iduser : String
    private var ten : TextView? = null
    private var db : SQLiteHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            iduser = it.getString("iduser").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgout = view.findViewById(R.id.logoutImageView)
        ten = view.findViewById(R.id.userNameTextView)
        db = SQLiteHelper(requireContext())

        ten?.text = db!!.getUserByID(iduser.toInt())
        imgout?.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finishAffinity()
        }

    }

    companion object{
        @JvmStatic
        fun newInstance(iduser: String) =
            GD_User().apply {
                arguments = Bundle().apply {
                    putString("iduser", iduser)

                }
            }
    }
}