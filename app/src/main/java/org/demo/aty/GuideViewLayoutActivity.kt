package org.demo.aty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.demo.guide.R
import org.daimhim.guideview.view.GuideLayout

class GuideViewLayoutActivity: FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val guideLayout1 = GuideLayout(this)
        guideLayout1.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        setContentView(R.layout.test_guide_layout_activity)
//        val findViewById = findViewById<ViewGroup>(android.R.id.content)
//        val inflate = LayoutInflater.from(this).inflate(R.layout.guide_activity_layout,null,false)
//        val guideLayout = inflate.findViewById<GuideLayout>(R.id.gl_layout)
//        guideLayout.setOriginalLayout(findViewById)
//        findViewById.addView(inflate)
    }
}