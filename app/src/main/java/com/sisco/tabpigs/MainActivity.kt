package com.sisco.tabpigs

import android.os.Bundle
import com.sisco.tabpigs.PlayActivity.Companion.newIntent
import com.sisco.tabpigs.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initListener() {
        binding.btnPlay.setOnClickListener {
            startActivity(newIntent(this))
        }
    }
}