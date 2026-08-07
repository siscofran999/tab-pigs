package com.sisco.tabpigs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sisco.tabpigs.ui.theme.TabPigsTheme
import com.sisco.tabpigs.navigation.GameNavigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TabPigsTheme {
                GameNavigation()
            }
        }
    }

//    override fun getViewBinding(): ActivityMainBinding {
//        return ActivityMainBinding.inflate(layoutInflater)
//    }
//
//    override fun initData(savedInstanceState: Bundle?) {
//
//    }
//
//    override fun initListener() {
//        binding.btnPlay.setOnClickListener {
//            startActivity(newIntent(this))
//        }
//    }
}