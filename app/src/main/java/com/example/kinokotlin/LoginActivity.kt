package com.example.kinokotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        initUI()
    }

    private fun initUI() {
        signUpBtn.setOnClickListener{
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        loginBtn.setOnClickListener {
            validateAndSignIn(inputLogin.text.toString(), inputPass.text.toString())
        }
    }

    private fun validateAndSignIn(email: String, password: String) {
        if(isValid(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        PreferenceUtils.saveLogged(this, true)
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(baseContext, "Authentication failed. Due " + task.exception,
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun isValid(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) return false
        return true
    }
}
