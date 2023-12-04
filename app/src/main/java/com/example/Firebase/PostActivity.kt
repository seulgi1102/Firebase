package com.example.Firebase

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {
    private var adapter:MyAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")
    private val itemsCollectionRef2 = db.collection("userID")
    private val title by lazy { findViewById<EditText>(R.id.enterTitle) }
    private val price by lazy { findViewById<EditText>(R.id.enterPrice) }
    private val description by lazy { findViewById<EditText>(R.id.detailDescription) }
    private var status = "1"
    private var user = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val userId = intent.getStringExtra("userId")
        //여기에 원래
        itemsCollectionRef2.get()
            .addOnSuccessListener {
                for (doc in it) {
                    val email = doc.getString("email")
                    if (email==userId) {
                        var username = doc.getString("username")
                        if(username != null) user = username else userId

                    }
                }
            }.addOnFailureListener {}

        val cancel = findViewById<Button>(R.id.cancel)
        cancel.setOnClickListener {
            val intent = Intent(this, SalesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }

        val post = findViewById<Button>(R.id.post)
        post.setOnClickListener {
            if (userId != null) {

                addItem(userId)
            }

        }
    }



    private fun addItem(userId:String) {
        //auth = FirebaseAuth.getInstance()
        //user = auth!!.currentUser
        var postText = findViewById<TextView>(R.id.postText)
        var email = userId
        val description = description.text.toString()
        var price = price.text.toString()
        val seller = user
        var status = ""
        var title = title.text.toString()
        if (this.status.equals("1")) status = "On sale"
        if (title.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty()) {
            val itemMap = hashMapOf(
                "title" to title,
                "description" to description,
                "price" to price,
                "seller" to seller,
                "status" to status,
                "email" to email
            )
            /*
            itemsCollectionRef.document(title).set(itemMap)
                .addOnSuccessListener { updateList() }.addOnFailureListener {  }*/
            itemsCollectionRef.add(itemMap)
                .addOnSuccessListener { updateList() }.addOnFailureListener {}
            val intent = Intent(this, SalesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()


        }else{
        postText.text = "Please fill it out without any blanks"
            val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.textview_warning2, null)
            postText.background= gradientDrawable
        }
    }
    private fun updateList() {
        itemsCollectionRef.get().addOnSuccessListener {
            val items = mutableListOf<newItem>()
            for (doc in it) {
                items.add(newItem(doc))
            }
            adapter?.updateList(items)
        }
    }

}
