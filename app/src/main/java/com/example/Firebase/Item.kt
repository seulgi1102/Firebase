package com.example.Firebase

import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.concurrent.locks.Condition

class Item{
    var price = 0
    var subject =""
    var condition = ""
    var description = ""
    var seller = ""
    constructor(subject: String, price:Int, condition:String, description:String, seller:String){
        this.price = price
        this.condition = condition
        this.subject=subject
        this.description=description
        this.seller = seller
    }
    fun Seller():String{
        return seller
    }
    fun Description():String {
        return description
    }
    fun Title():String{
        return subject
    }
    fun Price():Int{
        return price
    }
    fun Condition():String{
        return condition
    }

}