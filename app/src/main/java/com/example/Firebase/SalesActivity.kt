package com.example.Firebase

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


//private lateinit var listView: ListView
//private lateinit var itemList: ArrayList<newItem>
//private var adapter:MyAdapter? = null

class SalesActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")

    private var adapter:MyAdapter? = null
    private lateinit var listView: ListView
    private lateinit var itemList: ArrayList<newItem>

    @SuppressLint("MissingInflatedId", "WrongViewCast")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales)
        listView = findViewById(R.id.listview)

        var userId = intent.getStringExtra("userId")

        showDefault()

        //다이얼로그
        val options = findViewById<TextView>(R.id.options)
        options.setOnClickListener{
            val items = arrayOf("All", "On sale", "Sold out")
            var selectedItem: String? = "All"
            val builder = AlertDialog.Builder(this).apply {
                setTitle("Options")
                setSingleChoiceItems(items, -1) { dialog, which ->
                    selectedItem = items[which]

                }
                setPositiveButton("OK" ,DialogInterface.OnClickListener{ dialog, which ->
                    when(selectedItem){
                        "All" ->showDefault()
                        "On sale" ->showOnsale()
                        "Sold out" ->showSoldout()
                    }

                    //Toast.makeText(this.context,selectedItem,Toast.LENGTH_SHORT).show()*/
                })
                create()
                show()
            }
        }
        val writeSalesPost = findViewById<Button>(R.id.writeBtn)
        writeSalesPost.setOnClickListener {
            var intentToPostAct = Intent(this, PostActivity::class.java)
            // startActivity(Intent(this, PostActivity::class.java))
            intentToPostAct.putExtra("userId",userId)
            startActivity(intentToPostAct)
            //finish()
        }
        val checkArchive = findViewById<Button>(R.id.archiveBtn)
        checkArchive.setOnClickListener {
            var intentToReceiveAct = Intent(this, ReceiveMsgActivity::class.java)
            intentToReceiveAct.putExtra("userId", userId)
            startActivity(intentToReceiveAct)
        }
        val profileBtn = findViewById<ImageButton>(R.id.profileBtn)
        profileBtn.setOnClickListener {
            var intentToProfileAct = Intent(this, ProfileActivity::class.java)
            intentToProfileAct.putExtra("userId", userId)
            startActivity(intentToProfileAct)
        }
        //파이어스토어에서 토큐먼트 데이터 가져와서 넣어주기
        //필터를 구현하기위해 프래그먼트나 다이얼로그를 이용, 판매여부에따라 다른 리스트를 보여줌다.
        //리스트의 판매글을 클릭하면 판매글보기화면으로 이동하고 그 화면에는 메시지보내기버튼이존재, 누르면 받은 메시지 보임-firebase
        //판매글 추가 버튼, 버튼 누르면 제목, 판매여부, 가격받아서 추가
       // ㅇ ㅕ ㄱ ㅣ 부 ㅌ ㅓ
        listView.setOnItemClickListener{  parent, view, position, id ->
            val clickedItem = itemList[position];
            if (userId.equals(clickedItem.email)){
                val intent = Intent(this, EditActivity::class.java)
                val price = clickedItem.price.toString()
                intent.putExtra("price", price)
                intent.putExtra("title", clickedItem.title)
                intent.putExtra("status", clickedItem.status)
                intent.putExtra("description", clickedItem.description)
                intent.putExtra("userId", userId)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, DetailActivity::class.java)
                val price = clickedItem.price.toString()
                intent.putExtra("price", price)
                intent.putExtra("title", clickedItem.title)
                intent.putExtra("seller", clickedItem.seller)
                intent.putExtra("status", clickedItem.status)
                intent.putExtra("description", clickedItem.description)
                intent.putExtra("userId", userId)
                intent.putExtra("email", clickedItem.email)
                startActivity(intent)
                finish()

                }

/*
        listView.setOnItemClickListener{  parent, view, position, id ->
            val clickedItem = itemList[position];
            itemsCollectionRef.get()
                .addOnSuccessListener {
                    for (doc in it) {
                        val email = doc.id
                        if (email==userId) {
                            val intent = Intent(this, EditActivity::class.java)
                            val price = clickedItem.price.toString()
                            intent.putExtra("price", price)
                            intent.putExtra("title", clickedItem.title)
                            intent.putExtra("status", clickedItem.status)
                            intent.putExtra("description", clickedItem.description)
                            startActivity(intent)

                        }
                        else{
                            val intent = Intent(this, DetailActivity::class.java)
                            val price = clickedItem.price.toString()
                            intent.putExtra("price", price)
                            intent.putExtra("title", clickedItem.title)
                            intent.putExtra("seller", clickedItem.seller)
                            intent.putExtra("status", clickedItem.status)
                            intent.putExtra("description", clickedItem.description)
                            startActivity(intent)
                        }
                    }

                }.addOnFailureListener(){}
*/
            /*
            if (userId.equals(clickedItem.)){
                val intent = Intent(this, EditActivity::class.java)
                val price = clickedItem.price.toString()
                intent.putExtra("price", price)
                intent.putExtra("title", clickedItem.title)
                intent.putExtra("status", clickedItem.status)
                intent.putExtra("description", clickedItem.description)
                startActivity(intent)
            }
            else {
                val intent = Intent(this, DetailActivity::class.java)
                val price = clickedItem.price.toString()
                intent.putExtra("price", price)
                intent.putExtra("title", clickedItem.title)
                intent.putExtra("seller", clickedItem.seller)
                intent.putExtra("status", clickedItem.status)
                intent.putExtra("description", clickedItem.description)
                startActivity(intent)
            }

             */
        }

    }

    fun showDefault(){
        itemList = ArrayList()
        itemsCollectionRef.get()
            .addOnSuccessListener {
                for (doc in it) {
                    // Firestore 문서에서 데이터를 가져와 newItem 객체 생성
                    itemList.add(newItem(doc))
                    var myAdapter = MyAdapter(itemList)
                    listView.adapter = myAdapter
                }
                adapter?.updateList(itemList)
            }.addOnFailureListener {  }

    }
    fun showOnsale(){
        itemList = ArrayList()
        itemsCollectionRef.get()
            .addOnSuccessListener {
                for (doc in it) {
                    val status = doc.getString("status")
                    if (status == "On sale") {
                        itemList.add(newItem(doc))
                        var myAdapter = MyAdapter(itemList)
                        listView.adapter = myAdapter
                    }
                }
                adapter?.updateList(itemList)
            }.addOnFailureListener {  }
    }
    fun showSoldout(){
        itemList = ArrayList()
        itemsCollectionRef.get()
            .addOnSuccessListener {
                for (doc in it) {
                    val status = doc.getString("status")
                    if (status == "Sold out") {
                        itemList.add(newItem(doc))
                        var myAdapter = MyAdapter(itemList)
                        listView.adapter = myAdapter
                    }
                }
                adapter?.updateList(itemList)
            }.addOnFailureListener {  }
    }
}
