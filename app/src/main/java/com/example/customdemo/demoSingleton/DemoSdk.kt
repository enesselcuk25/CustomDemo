package com.example.customdemo.demoSingleton

import android.content.Context
import android.content.Intent
import com.example.customdemo.MainActivity


object DemoSdk {

    var demoHairOrder:((String) -> Unit)? = null



    fun setMessageHair(context: Context,demo:(String) -> Unit ){
       demo.let {
           demoHairOrder = demo
       }

        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

}