package com.example.kinokotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        initUI()

    }
    private fun initUI() {
        regLoginBtn.setOnClickListener {
            validateAndSignUp(regEmail.text.toString(), regPass.text.toString())
        }
    }

    private fun validateAndSignUp(email: String, password: String){
        if (isValid(email, password)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        Toast.makeText(baseContext, "Authentication failed. Due to " + task.exception,
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
