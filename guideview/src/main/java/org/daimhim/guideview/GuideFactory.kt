package org.daimhim.guideview

import org.daimhim.guideview.guide.AbsGuide
import org.daimhim.guideview.guide.DialogGuideImpl
import org.daimhim.guideview.guide.GuideLayoutGuideImpl
import org.daimhim.guideview.guide.ViewGuideImpl
import org.daimhim.guideview.view.MaskView

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
            MaskView.GUIDE_LAYOUT_SHOW->{
                GuideLayoutGuideImpl()
            }
            else->{
                null
            }
        }
    }
}