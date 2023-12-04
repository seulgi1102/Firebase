package com.example.Firebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity :AppCompatActivity() {

    private val detailTitle by lazy { findViewById<TextView>(R.id.detailTitle) }
    private val detailPrice by lazy { findViewById<TextView>(R.id.detailPrice) }
    private val detailDescription by lazy { findViewById<TextView>(R.id.detailDescription) }
    private val detailStatus by lazy { findViewById<TextView>(R.id.detailStatus)}
    private val detailSeller by lazy { findViewById<TextView>(R.id.detailSeller) }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var userId = intent.getStringExtra("userId")
        var sellerEmail = intent.getStringExtra("email")
        var seller = intent.getStringExtra("seller")
        var price = intent.getStringExtra("price")
        var status = intent.getStringExtra("status")
        var title = intent.getStringExtra("title")
        var description = intent.getStringExtra("description")

        detailTitle.text = title
        detailDescription.text = description
        detailSeller.text = seller
        detailPrice.text = price
        detailStatus.text = status
        detailDescription.movementMethod = ScrollingMovementMethod.getInstance()
        val cancel = findViewById<Button>(R.id.closeDetailPage)
        cancel.setOnClickListener {
            val intent = Intent(this, SalesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()

        }
        val message = findViewById<Button>(R.id.msgBtn)
        message.setOnClickListener {
            val intent = Intent(this, SendMsgActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("seller", seller)
            intent.putExtra("email", sellerEmail)
            startActivity(intent)
            finish()
        }

    }
}