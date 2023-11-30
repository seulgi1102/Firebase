package com.example.Firebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReceiveMsgActivity : AppCompatActivity(){
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("messages")
    private val itemsCollectionRef2 = db.collection("userID")
    private var adapter:MsgAdapter? = null
    private lateinit var listView: ListView
    private lateinit var messageList: ArrayList<newMessage>

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive)
        listView = findViewById(R.id.listview)

        var userId = intent.getStringExtra("userId")
        var cancel = findViewById<Button>(R.id.closeArchivePage)
        if (userId != null) {
            setUserName(userId)
            showMessage(userId)
        }

        cancel.setOnClickListener {
            val intent = Intent(this, SalesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }
        listView.setOnItemClickListener { parent, view, position, id ->
            val clickedItem = messageList[position];
            if (userId.equals(clickedItem.receiverEmail)) {
                val intent = Intent(this, DetailMessageActivity::class.java)
                intent.putExtra("userId", userId)
                intent.putExtra("sender", clickedItem.sender)
                intent.putExtra("senderEmail",clickedItem.senderEmail)
                intent.putExtra("subject", clickedItem.subject)
                intent.putExtra("content", clickedItem.content)
                startActivity(intent)
                finish()
            }
        }
    }
    private fun setUserName(userId:String){
        var user = findViewById<TextView>(R.id.username)
        itemsCollectionRef2.get().addOnSuccessListener {
                for(doc in it) {
                    val email = doc.getString("email")
                    if(email == userId){
                        val username = doc.getString("username")
                        user.setText(username)
                    }
                }
        }.addOnFailureListener{}
    }
    private fun showMessage(userId:String){
    messageList = ArrayList()
    itemsCollectionRef.get()
    .addOnSuccessListener {
        for (doc in it) {
            var receiverEmail = doc.getString("receiverEmail")
            if (receiverEmail == userId) {
                messageList.add(newMessage(doc))
                var myAdapter = MsgAdapter(messageList)
                listView.adapter = myAdapter
            }
        }
        adapter?.updateList(messageList)
    }.addOnFailureListener {  }
    }
}