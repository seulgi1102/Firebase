package com.example.Firebase

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SendMsgActivity : AppCompatActivity(){
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("messages")
    private val itemsCollectionRef2 = db.collection("userID")
    private val content by lazy { findViewById<EditText>(R.id.content) }
    private val subject by lazy { findViewById<EditText>(R.id.subject) }
    private var sender = ""
    private var adapter:MsgAdapter? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        var receiver = findViewById<TextView>(R.id.receiver)
        var userId = intent.getStringExtra("userId")
        var sellerEmail = intent.getStringExtra("email")
        var sellerId = intent.getStringExtra("seller")

        if (userId != null) {
            setUserName(userId)
        }
        receiver.setText(sellerId)

        val cancel = findViewById<Button>(R.id.closeSendPage)
        cancel.setOnClickListener {
            val intent = Intent(this, SalesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }
        val send = findViewById<Button>(R.id.send)
        send.setOnClickListener {

            if (sellerId != null) {
                if (userId != null) {
                    if (sellerEmail != null) {
                        addMessage(sellerId, userId, sellerEmail)
                    }
                }
            }
//
        }

    }
    private fun setUserName(userId:String){
        var user = findViewById<TextView>(R.id.sendername)
        itemsCollectionRef2.get().addOnSuccessListener {
            for(doc in it) {
                val email = doc.getString("email")
                if(email == userId){
                    val username = doc.getString("username")
                    user.setText(username)
                    if (username != null) {
                        sender = username
                    }
                }
            }
        }.addOnFailureListener{}
    }
    private fun addMessage(sellerId:String, userId:String, sellerEmail:String){
        var sendText = findViewById<TextView>(R.id.sendText)
        var content =  content.text.toString()
        var subject = subject.text.toString()
        if(content.isNotBlank() && subject.isNotBlank()){
            val itemMap = hashMapOf (
                "subject" to subject,
                "content" to content,
                "receiver" to sellerId,
                "senderEmail" to userId,
                "receiverEmail" to sellerEmail,
                "sender" to sender
            )
            itemsCollectionRef.add(itemMap)
                .addOnSuccessListener { updateList() }.addOnFailureListener {  }
            val intent = Intent(this, SalesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }else{
            sendText.text = "Please fill it out without any blanks"
            val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.textview_warning2, null)
            sendText.background= gradientDrawable
        }
    }
    private fun updateList() {
        itemsCollectionRef.get().addOnSuccessListener {
            val messages = mutableListOf<newMessage>()
            for (doc in it) {
                messages.add(newMessage(doc))
            }
            adapter?.updateList(messages)
        }
    }

}
