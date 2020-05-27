package org.daimhim.guideview

import android.R
import android.os.Parcel
import android.os.Parcelable
import org.daimhim.guideview.view.MaskView

/**
 * 遮罩系统创建时配置参数的封装 <br></br>
 * Created by binIoter
 */
class Configuration() : Parcelable {
    /**
     * 高亮区域的padding
     */
    var mPadding = 0

    /**
     * 是否可以透过蒙层点击，默认不可以
     */
    var mOutsideTouchable = false

    /**
     * 遮罩透明度
     */
    
    var mAlpha = 255

    /**
     * 遮罩覆盖区域控件Id
     *
     *
     * 该控件的大小既该导航页面的大小
     */
    var mFullingViewId = -1

    /**
     * 高亮区域的圆角大小
     */
    
    var mCorner = 0

    /**
     * 高亮区域的图形样式，默认为矩形
     */
    
    var mGraphStyle = MaskView.ROUNDRECT

    /**
     * 遮罩背景颜色id
     */
    
    var mFullingColorId = R.color.black

    /**
     * 是否在点击的时候自动退出导航
     */
    
    var mAutoDismiss = true

    /**
     * 是否覆盖目标控件
     */
    
    var mOverlayTarget = false

    /**
     * ，默认 仅焦点可点击
     */
    var focusClick = true
    var mShowCloseButton = false
    
    var mEnterAnimationId = -1
    
    var mExitAnimationId = -1
    
    var mShowMode = MaskView.VIEW_SHOW

    /**
     * 是否可以取消
     */
    var mCancelable = true

    constructor(parcel: Parcel) : this() {
        mPadding = parcel.readInt()
        mOutsideTouchable = parcel.readByte() != 0.toByte()
        mAlpha = parcel.readInt()
        mFullingViewId = parcel.readInt()
        mCorner = parcel.readInt()
        mGraphStyle = parcel.readInt()
        mFullingColorId = parcel.readInt()
        mAutoDismiss = parcel.readByte() != 0.toByte()
        mOverlayTarget = parcel.readByte() != 0.toByte()
        focusClick = parcel.readByte() != 0.toByte()
        mShowCloseButton = parcel.readByte() != 0.toByte()
        mEnterAnimationId = parcel.readInt()
        mExitAnimationId = parcel.readInt()
        mShowMode = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mPadding)
        parcel.writeByte(if (mOutsideTouchable) 1 else 0)
        parcel.writeInt(mAlpha)
        parcel.writeInt(mFullingViewId)
        parcel.writeInt(mCorner)
        parcel.writeInt(mGraphStyle)
        parcel.writeInt(mFullingColorId)
        parcel.writeByte(if (mAutoDismiss) 1 else 0)
        parcel.writeByte(if (mOverlayTarget) 1 else 0)
        parcel.writeByte(if (focusClick) 1 else 0)
        parcel.writeByte(if (mShowCloseButton) 1 else 0)
        parcel.writeInt(mEnterAnimationId)
        parcel.writeInt(mExitAnimationId)
        parcel.writeInt(mShowMode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Configuration> {
        override fun createFromParcel(parcel: Parcel): Configuration {
            return Configuration(parcel)
        }

        override fun newArray(size: Int): Array<Configuration?> {
            return arrayOfNulls(size)
        }
    }


}