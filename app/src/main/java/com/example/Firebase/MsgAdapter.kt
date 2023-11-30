package com.example.Firebase


import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.firestore.QueryDocumentSnapshot

data class newMessage(var id: String, var subject:String, var content:String, var senderEmail:String, var receiver:String, var receiverEmail:String, var sender:String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(
                doc.id,
                doc["subject"].toString(),
                doc["content"].toString(),
                doc["senderEmail"].toString(),
                doc["receiver"].toString(),
                doc["receiverEmail"].toString(),
                doc["sender"].toString()
            )
    constructor(key: String, map: Map<String, Any>) :
            this(
                key,
                map["subject"].toString(),
                map["content"].toString(),
                map["senderEmail"].toString(),
                map["receiver"].toString(),
                map["receiverEmail"].toString(),
                map["sender"].toString()
                //map["name"].toString()
            )
}

class MsgAdapter(var messages: ArrayList<newMessage>): BaseAdapter(){

    /*lateinit var context:Context
                lateinit var inflater:LayoutInflater
                var sample = ArrayList<item>()

                constructor(context: Context,  data:ArrayList<item>) : this() {
                    this.context = context;
                    sample = data;
                    inflater = LayoutInflater.from(context)
                }*/
    fun updateList(newList: MutableList<newMessage>){
        messages = newList as ArrayList<newMessage>

    }
    override fun getCount(): Int {
        return messages.size
    }
    override fun getItem(position: Int): newMessage {
        return messages[position]
    }

    override fun getItemId(position: Int): Long{
        return position.toLong()
    }


    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var view =
            LayoutInflater.from(parent?.context).inflate(R.layout.msg_list, parent, false)
        val  message = messages[position]
        var sender = view.findViewById<TextView>(R.id.sender)
        var subject = view.findViewById<TextView>(R.id.subject)
        var content = view.findViewById<TextView>(R.id.content)

        sender.text = message.sender
        subject.text = message.subject
        content.text = message.content


        return view
    }

}