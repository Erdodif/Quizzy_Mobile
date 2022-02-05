package hu.petrik.quizion.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import hu.petrik.quizion.R
import hu.petrik.quizion.activities.LoginActivity
import hu.petrik.quizion.activities.RegisterActivity
import hu.petrik.quizion.components.ViewSwapper
import hu.petrik.quizion.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var bind: FragmentLoginBinding

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentLoginBinding.inflate(inflater, container, false)
        init()
        return bind.root
    }

    fun clearErrorMessages(){
        bind.textInputPassword.error = null
        bind.textInputUID.error = null
    }

    private fun init() {
        clearErrorMessages()
        bind.textInputUID.setOnClickListener{
            bind.textInputUID.error = null
        }
        bind.textInputPassword.setOnClickListener{
            bind.textInputPassword.error = null
        }
        bind.buttonLogin.setOnClickListener {
            val activity = requireActivity()
            val uID = bind.textInputUID.editText!!.text.toString()
            val pass = bind.textInputPassword.editText!!.text.toString()
            var noError = true
            if(uID.isEmpty()){
                bind.textInputUID.error = activity.getString(R.string.uid_field_missing)
                noError = false
            }
            if(pass.isEmpty()){
                bind.textInputPassword.error = activity.getString(R.string.password_field_missing)
                noError = false
            }
            if(noError){
                clearErrorMessages()
                (activity as LoginActivity).login(
                    uID,
                    pass
                )
            }
        }
        bind.buttonRegister.setOnClickListener {
            ViewSwapper.swapActivity(activity as Context, RegisterActivity())
        }
        bind.textInputPassword.editText!!.setOnLongClickListener {
            bind.textInputPassword.editText!!.text = "".toEditable()
            bind.textInputPassword.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            false
        }
        try {
            val arguments = requireArguments()
            if (arguments.containsKey("userID")
                && arguments.containsKey("password")
            ) {
                val userID = arguments.getString("userID")!!
                val password = arguments.getString("password")!!
                fillFromRegister(userID, password)
            }
        }
        catch (e:IllegalStateException){}
    }

    private fun fillFromRegister(userID: String, password: String) {
        bind.textInputUID.editText!!.text = userID.toEditable()
        with(bind.textInputPassword) {
            bind.textInputPassword.endIconMode = TextInputLayout.END_ICON_NONE
            editText!!.text = password.toEditable()
            editText!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s.isNullOrEmpty()) {
                        this@with.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                        this@with.editText!!.removeTextChangedListener(this)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?, start: Int, before: Int, count: Int
                ) {
                }
            })
        }
    }


}