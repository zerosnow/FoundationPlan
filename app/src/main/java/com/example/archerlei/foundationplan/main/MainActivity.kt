package com.example.archerlei.foundationplan.main

import android.app.Activity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.archerlei.foundationplan.R
import com.example.archerlei.foundationplan.main.adapter.FoundationPlanAdapter
import com.example.archerlei.foundationplan.main.model.FoundationData
import com.example.archerlei.foundationplan.main.presenter.FoundationPlanPresenter
import com.example.archerlei.foundationplan.main.view.IFoundationPlanView
import com.example.archerlei.foundationplan.util.DensityUtil
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity() {
    private lateinit var mRecyclerView: SwipeMenuRecyclerView
    private lateinit var mAdapter: FoundationPlanAdapter
    private lateinit var mFoundationPlanPresenter: FoundationPlanPresenter
    private lateinit var mDrawLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView = findViewById(R.id.recycler_view)
        mDrawLayout = findViewById(R.id.drawer_layout)
        mFoundationPlanPresenter = FoundationPlanPresenter(mFoundationPlanView)
        mFoundationPlanPresenter.requestData()
        mFoundationPlanPresenter.requestGupiao()
        initEvent()
    }

    private fun initEvent() {
        open_button.setOnClickListener {
            mDrawLayout.openDrawer(Gravity.START)
        }

        val swipeMenuCreate = SwipeMenuCreator { _, rightMenu, _ ->
            val deleteItem = SwipeMenuItem(this@MainActivity)
            deleteItem.setImage(R.mipmap.icon_delete)
            deleteItem.width = DensityUtil.dip2px(this, 40f)
            deleteItem.height = ViewGroup.LayoutParams.MATCH_PARENT
            deleteItem.setBackgroundColorResource(R.color.colorGray)
            rightMenu?.addMenuItem(deleteItem)

            val upItem = SwipeMenuItem(this@MainActivity)
            upItem.setImage(R.mipmap.icon_up)
            upItem.width = DensityUtil.dip2px(this, 40f)
            upItem.height = ViewGroup.LayoutParams.MATCH_PARENT
            upItem.setBackgroundColorResource(R.color.colorRed)
            rightMenu?.addMenuItem(upItem)

            val downItem = SwipeMenuItem(this@MainActivity)
            downItem.setImage(R.mipmap.icon_down)
            downItem.width = DensityUtil.dip2px(this, 40f)
            downItem.height = ViewGroup.LayoutParams.MATCH_PARENT
            downItem.setBackgroundColorResource(R.color.colorGreen)
            rightMenu?.addMenuItem(downItem)
        }
        mRecyclerView.setSwipeMenuCreator(swipeMenuCreate)

        val itemClickListener = SwipeMenuItemClickListener { menuBridge, position ->
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu()

            // 菜单在Item中的Position：
            val menuPosition = menuBridge.position

            when(menuPosition) {
                0 -> mFoundationPlanPresenter.deleteItem(position)
                1 -> mFoundationPlanPresenter.updateRange(true, position)
                2 -> mFoundationPlanPresenter.updateRange(false, position)
            }
        }
        mRecyclerView.setSwipeMenuItemClickListener(itemClickListener)

        mAdapter = FoundationPlanAdapter(this)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter


        findViewById<TextView>(R.id.add_new_foundation).setOnClickListener {
            val dialog = AddMoreFoundationDialog(this, mFoundationPlanPresenter)
            dialog.setCancelable(true)
            dialog.show()
        }

        findViewById<TextView>(R.id.update_data).setOnClickListener {
            Toast.makeText(this@MainActivity, "更新中", Toast.LENGTH_LONG).show()
            mFoundationPlanPresenter.requestData()
        }
    }

    private val mFoundationPlanView = object :IFoundationPlanView {
        override fun updateList(list: List<FoundationData>) {
            runOnUiThread {
                mAdapter.updateData(list)
            }
        }

        override fun getActivity() = this@MainActivity

    }
}
