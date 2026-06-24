package com.sisco.tabpigs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding>: AppCompatActivity() {

    protected lateinit var binding: VB

    protected abstract fun getViewBinding(): VB
    protected abstract fun initData(savedInstanceState: Bundle?)
    protected abstract fun initListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        initData(savedInstanceState)
        initListener()
    }

}