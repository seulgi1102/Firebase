package com.example.Firebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var name: EditText
    private lateinit var birth: EditText
    private lateinit var signUpBtn: Button
    private lateinit var textSignUp: TextView
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("userID")


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        //위젯찾기
        email = findViewById<EditText>(R.id.signUpEmail)
        password = findViewById<EditText>(R.id.signUpPass)
        name = findViewById<EditText>(R.id.signUpName)
        birth = findViewById<EditText>(R.id.signUpBirth)
        signUpBtn = findViewById<Button>(R.id.signUpBtn)
        textSignUp = findViewById<TextView>(R.id.textViewSignUp)
        val signin = findViewById<TextView>(R.id.signIn2)

        val intent = Intent(this, SalesActivity::class.java)

        signin.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
        signUpBtn.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            val name = name.text.toString()
            val birth = birth.text.toString()
            //회원가입
            if (email.isNotBlank() && password.isNotBlank() && name.isNotBlank() && birth.isNotBlank()) {
                Firebase.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { // it: Task<AuthResult!>
                        if (it.isSuccessful) {
                            addUser(email, name, birth)
                            Firebase.auth.signInWithEmailAndPassword(email, password)

                            intent.putExtra("userId", email)

                            startActivity(intent)
                            finish()
                        } else {
                            textSignUp.text = "sign-up failed"
                            val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.textview_warning, null)
                            textSignUp.background= gradientDrawable
                        }

                    }
            } else {
                textSignUp.text = "Please fill it out without any blanks"
                val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.textview_warning, null)
                textSignUp.background= gradientDrawable
            }
        }
    }
    private fun addUser(email: String, name: String, birth: String){

        val itemMap = hashMapOf (
            "email" to email,
            "birth" to birth,
            "username" to name
        )

        itemsCollectionRef.add(itemMap)
            .addOnSuccessListener { }.addOnFailureListener{}
    /*
        itemsCollectionRef.document(email).set(itemMap)
            .addOnSuccessListener { }.addOnFailureListener {  }*/
    }

}