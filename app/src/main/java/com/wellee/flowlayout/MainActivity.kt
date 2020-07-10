package com.wellee.flowlayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mItems: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initView()
    }

    private fun initData() {
        mItems = ArrayList()
        mItems.add("哈哈")
        mItems.add("红烧豆腐圣诞快乐")
        mItems.add("开始的减肥")
        mItems.add("水电费")
        mItems.add("坡港口")
        mItems.add("共商共建")
        mItems.add("我告诉你地方")
        mItems.add("和iOS的看法")
        mItems.add("颇高过门石")
        mItems.add("啥感觉噶是的")
        mItems.add("满足你霸道")
        mItems.add("挂十多个")
    }

    private fun initView() {
        fl.setAdapter(mAdapter)
    }

    private val mAdapter = object : BaseAdapter(){
        override fun getItemView(position: Int, parent: ViewGroup): View {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_flow, parent, false) as TextView
            view.text = mItems[position]
            view.setOnClickListener {
                Toast.makeText(this@MainActivity, mItems[position], Toast.LENGTH_SHORT).show()
            }
            return view
        }

        override fun getItemCount(): Int {
            return mItems.size
        }
    }
}
