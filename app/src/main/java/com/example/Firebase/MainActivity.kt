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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signInBtn: Button
    private lateinit var signup: TextView
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef2 = db.collection("userID")
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //위젯 찾기
// 위젯 초기화
        textView = findViewById(R.id.textView)
        email = findViewById(R.id.Email)
        password = findViewById(R.id.Password)
        signInBtn = findViewById(R.id.signInBtn)
        signup = findViewById(R.id.signUp)

        signup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))

        }
        signInBtn.setOnClickListener {
                val userId = email.text.toString()
                val password = password.text.toString()
                val intent = Intent(this, SalesActivity::class.java)
            try {
                Firebase.auth.signInWithEmailAndPassword(userId, password)
                    .addOnCompleteListener(this) { // it: Task<AuthResult!>
                        if (it.isSuccessful) {
                            //addUser()
                            //이메일 전달, 판매글목록으로 이동
                            intent.putExtra("userId", userId) //수정 email -> userid
                            startActivity(intent)
                            finish()
                            //displayimage()

                        } else {
                            /*
                    Log.w("LoginActivity", "signInWithEmail", it.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()*/
                            textView.text = "Email or password is incorrect."
                            val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.textview_warning, null)
                            textView.background= gradientDrawable
                        }
                    }
            }
            catch (e: IllegalArgumentException){
                textView.text = "Please enter your email and password"
                val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.textview_warning2, null)
                textView.background= gradientDrawable
            }
        }
        //remoteconfig설정, fetch 설정
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1 // For test purpose only, 3600 seconds for production
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config) // xml추가
        //목록 표시
       /* val textView2 = findViewById<TextView>(R.id.textView)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { // it: task
                val test = remoteConfig.getBoolean("test")//키 Long타입으로 가져오기
                textView2.text = "{$test}"
            }*/
    }
    /*fun displayimage() { // storage에서 이미지 가져오기
        //val storageRef = Firebase.storage.reference // reference to root
        //val imageRef1 = storageRef.child("images/computer_sangsangbugi.jpg")
        val imageRef = Firebase.storage.getReferenceFromUrl(
            "gs://fir-e354f.appspot.com/Deadpool Collection.jpg"
        )
        //이미지뷰에 이미지 넣기
        val view = findViewById<ImageView>(R.id.imageView)
        //이미지 표시하기
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
// Failed to download the image
        }

    }*/

    private fun addUser(){

        var userId = email.text.toString()
        var birth = ""
        var name = ""
        val itemMap = hashMapOf (
            "birth" to birth,
            "email" to userId,
            "name" to name
        )

        itemsCollectionRef2.document(userId).set(itemMap)
            .addOnSuccessListener { }.addOnFailureListener {  }
    }


}