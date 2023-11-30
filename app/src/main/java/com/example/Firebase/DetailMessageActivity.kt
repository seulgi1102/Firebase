package com.example.Firebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailMessageActivity : AppCompatActivity() {

    private val msgSubject by lazy { findViewById<TextView>(R.id.msgtitle) }
    private val msgContent by lazy { findViewById<TextView>(R.id.msgcontent) }
    private val msgSenderEmail by lazy { findViewById<TextView>(R.id.msgsenderemail) }
    private val msgSender by lazy { findViewById<TextView>(R.id.msgsender) }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msgdetail)

        var userId = intent.getStringExtra("userId")
        var senderEmail = intent.getStringExtra("senderEmail")
        var sender = intent.getStringExtra("sender")
        var content = intent.getStringExtra("content")
        var subject = intent.getStringExtra("subject")


        msgSubject.text = subject
        msgContent.text = content
        msgSenderEmail.text = senderEmail
        msgSender.text = sender
        val cancel = findViewById<Button>(R.id.closeMsgDetailPage)
        cancel.setOnClickListener {
            val intent = Intent(this, ReceiveMsgActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()

        }

    }
}