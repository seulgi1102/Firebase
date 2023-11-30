package com.example.Firebase

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.firestore.QueryDocumentSnapshot


data class newItem(var id: String,var price:Int,var status:String,var description:String,var seller:String, var title:String, var email:String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(
                doc.id,
                doc["price"].toString().toIntOrNull() ?: 0,
                doc["status"].toString(),
                doc["description"].toString(),
                doc["seller"].toString(),
                doc["title"].toString(),
                doc["email"].toString()
               // doc["name"].toString()
            )
    constructor(key: String, map: Map<String, Any>) :
            this(
                key,
                map["price"].toString().toIntOrNull() ?: 0,
                map["status"].toString(),
                map["description"].toString(),
                map["seller"].toString(),
                map["title"].toString(),
                map["email"].toString()
                //map["name"].toString()
            )
}

class MyAdapter(var items: ArrayList<newItem>): BaseAdapter(){

    /*lateinit var context:Context
                lateinit var inflater:LayoutInflater
                var sample = ArrayList<item>()

                constructor(context: Context,  data:ArrayList<item>) : this() {
                    this.context = context;
                    sample = data;
                    inflater = LayoutInflater.from(context)
                }*/
    fun updateList(newList: MutableList<newItem>){
        items = newList as ArrayList<newItem>

    }
    override fun getCount(): Int {
        return items.size
    }
    override fun getItem(position: Int): newItem {
        return items[position]
    }

    override fun getItemId(position: Int): Long{
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var view =
            LayoutInflater.from(parent?.context).inflate(R.layout.item_list, parent, false)
        val item = items[position]
        var seller = view.findViewById<TextView>(R.id.seller)
        var sub = view.findViewById<TextView>(R.id.subj)
        var price = view.findViewById<TextView>(R.id.price)
        var status = view.findViewById<TextView>(R.id.condition)
        var description = view.findViewById<TextView>(R.id.description)
        val context: Context = status.context
        seller.text = item.seller
        sub.text = item.title
        description.text = item.description
        price.text = item.price.toString()
        status.text = item.status

        if(status.text.equals("Sold out")){
            //status.setBackgroundColor(Color.parseColor("#ED6262"))
            val gradientDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.status_soldout, null)
            status.background= gradientDrawable
            //status.backgroundTintMode
        }
        if(status.text.equals("On sale")){
            val gradientDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.status_onsale, null)
            status.background= gradientDrawable
            //status.backgroundTintMode
        }
        //condition.text = item.Condition()

        return view
    }

}