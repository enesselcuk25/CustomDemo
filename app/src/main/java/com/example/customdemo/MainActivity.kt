package com.example.customdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private lateinit var countDownTimerView: CountDownTimerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        countDownTimerView = findViewById(R.id.countTimeDowner)

        countDownTimerView.startTimer(10)
    }
}