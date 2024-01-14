package com.example.customdemo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.customdemo.databinding.ActivityMain2Binding
import com.example.customdemo.databinding.ActivityMainBinding
import com.example.customdemo.demoInterface.MessageShow
import com.example.customdemo.demoSingleton.DemoSdk

class MainActivity : AppCompatActivity() {


private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)

        binding.countTimeDowner.startTimer(10,40)

    }

    override fun onResume() {
        super.onResume()
        binding.countTimeDowner.timerStart()
    }

    override fun onPause() {
        super.onPause()
        binding.countTimeDowner.stopTimer()
    }

}