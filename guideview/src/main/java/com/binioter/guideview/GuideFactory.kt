package com.binioter.guideview

import com.binioter.guideview.guide.AbsGuide
import com.binioter.guideview.guide.DialogGuideImpl
import com.binioter.guideview.guide.ViewGuideImpl
import com.binioter.guideview.view.MaskView

/**
 * @date: 2020/5/25
 * @author: Zhangx
 * @remark: 这个人很懒，什么都没有留下
 */
class GuideFactory {

    fun makeGuide(showMode:Int): AbsGuide?{
        return when(showMode){
            MaskView.VIEW_SHOW->{
                ViewGuideImpl()
            }
            MaskView.DIALOG_SHOW->{
                DialogGuideImpl()
            }
            else->{
                null
            }
        }
    }
}