package com.qianfan.catup

import androidx.annotation.IntDef

object Params {

    var keyword = "去浏览"

    var viewTime: Long = 25L

    var mode = Mode.VIEW_PAGE

    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
    @MustBeDocumented
    @IntDef(Mode.VIEW_PAGE, Mode.CLICK_CAT)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation
    class Mode {
        companion object {

            /**
             * 浏览页面模式
             */
            const val VIEW_PAGE = 1

            /**
             * 点猫猫模式
             */
            const val CLICK_CAT = 2


        }
    }
}