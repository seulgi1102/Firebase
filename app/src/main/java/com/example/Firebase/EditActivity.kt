package com.example.Firebase

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditActivity :AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")
    private val editTitle by lazy { findViewById<TextView>(R.id.editTitle) }
    private val editPrice by lazy { findViewById<TextView>(R.id.editPrice) }
    private val editText by lazy { findViewById<TextView>(R.id.editText) }
    private val editDescription by lazy { findViewById<TextView>(R.id.editDescription) }
    private val editStatus by lazy { findViewById<RadioGroup>(R.id.editRadioG)}
    private val onSale by lazy { findViewById<RadioButton>(R.id.onSale)}
    private val soldOut by lazy { findViewById<RadioButton>(R.id.soldOut)}
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        var userId = intent.getStringExtra("userId")
        var price = intent.getStringExtra("price")
        var status = intent.getStringExtra("status")
        var defaultTitle = intent.getStringExtra("title")
        var description = intent.getStringExtra("description")

        if (defaultTitle != null && price !=null && description !=null && status !=null) {
        setDefaultItem(defaultTitle, description, price, status )
        }

        val cancel = findViewById<Button>(R.id.closeEditPage)
        cancel.setOnClickListener {
            val intent = Intent(this, SalesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }

        val edit = findViewById<Button>(R.id.editBtn)
        edit.setOnClickListener {
            if (defaultTitle != null) {
                if (userId != null) {
                    updateItem(defaultTitle, userId)
                }
            }
        }
    }

    private fun setDefaultItem(title:String, description:String, price:String, status:String){
        editText.text = "Edit the sales article"
        editTitle.text = title
        editDescription.text = description
        editPrice.text = price
        if(status.equals("On sale")){
            editStatus.check(R.id.onSale)
        }
        if(status.equals("Sold out")){
            editStatus.check(R.id.soldOut)
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun updateItem(dTitle:String, userId: String){
        var title = editTitle.text.toString()
        var price = editPrice.text.toString()
        var description = editDescription.text.toString()
        var selectedStatus = editStatus.checkedRadioButtonId
        var status = if (selectedStatus == R.id.onSale) "On sale" else "Sold out"
        try{
        if (title.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty()) {
        val updates = mutableMapOf<String, Any>()
        updates["title"] = title
        updates["price"] = price
        updates["description"] = description
        updates["status"] = status // Change "status" to the actual field name

        // Update the document in Firestore
        /*itemsCollectionRef.document(dTitle).update(updates)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
        } else {

        }*/
            itemsCollectionRef.get().addOnSuccessListener {
                for (doc in it) {
                    var title = doc.getString("title")
                    if(title == dTitle) {
                        itemsCollectionRef.document(doc.id).update(updates)
                            .addOnSuccessListener {
                                val intent = Intent(this, SalesActivity::class.java)
                                intent.putExtra("userId", userId)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                // Handle failure (e.g., show an error message or log)
                            }
                    }
                }
            }
        }else{
            editText.text = "Please fill it out without any blanks"
            editText.setBackgroundColor(Color.parseColor("#D3C7FF"))
        }
    }catch (e: IllegalArgumentException){

        }

    }
}