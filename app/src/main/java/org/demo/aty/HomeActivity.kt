package org.demo.aty

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.demo.guide.R

class HomeActivity : FragmentActivity(), View.OnClickListener {
    private var mBtnList: Button? = null
    private var mBtnMore: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mBtnMore = findViewById<View>(R.id.btn_more) as Button
        mBtnList = findViewById<View>(R.id.btn_list) as Button
        findViewById<View>(R.id.btn_guide_view).setOnClickListener(this)
        mBtnList!!.setOnClickListener(this)
        mBtnMore!!.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_more -> startActivity(Intent(this@HomeActivity, SimpleGuideViewActivity::class.java))
            R.id.btn_list -> startActivity(Intent(this@HomeActivity, MyListActivity::class.java))
            R.id.btn_guide_view -> startActivity(Intent(this@HomeActivity, GuideViewLayoutActivity::class.java))
        }
    }
}