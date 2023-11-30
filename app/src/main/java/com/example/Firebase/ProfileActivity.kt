package com.example.Firebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity :AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("userID")


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        var userId = intent.getStringExtra("userId")
        if (userId != null) {
            setDefaultProfile(userId)
        }
        var logout = findViewById<TextView>(R.id.logout)
        logout.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun setDefaultProfile(userId:String){
        var userName = findViewById<TextView>(R.id.profileUserName)
        val userEmail = findViewById<TextView>(R.id.profileEmail)
        val userBirth = findViewById<TextView>(R.id.profileBirth)
        itemsCollectionRef.get().addOnSuccessListener {
            for(doc in it){
                var email = doc.getString("email")
                if(email == userId){
                var name = doc.getString("username")
                var birth = doc.getString("birth")
                userName.text = name
                userEmail.text = email
                userBirth.text = birth
                }
            }
        }.addOnFailureListener{}

        val cancel = findViewById<Button>(R.id.closeProfilePage)
        cancel.setOnClickListener {
            val intent = Intent(this, SalesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()

        }

    }
}