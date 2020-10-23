package com.qianfan.catup

import android.accessibilityservice.AccessibilityService
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi
import androidx.core.os.postDelayed

class CatService : AccessibilityService() {

    private val TAG: String = "cat_up"

    private var mLastStateTime: Long = 0
    private var mHasCat: Boolean = false
    private var uiHandler: Handler = Handler(Looper.getMainLooper())

    override fun onInterrupt() {
        Log.d(TAG, "onInterrupt")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event?.eventType == TYPE_WINDOW_STATE_CHANGED) {
            if (event?.source != null) {
                if (isCatPage(event?.source)) {
                    mHasCat = false
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - mLastStateTime > 2 * 1000) {
                        mLastStateTime = currentTime
                        Log.d(TAG, "开始做任务")
                        autoPerform(event?.source)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun autoPerform(rootNode: AccessibilityNodeInfo) {
        if (rootNode != null) {
            val nodeWebView = findWebViewNode(rootNode)
            if (nodeWebView != null) {
                performSth(nodeWebView)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun isCatPage(rootNode: AccessibilityNodeInfo): Boolean {
        if (rootNode != null) {
            val nodeWebView = findWebViewNode(rootNode)
            if (nodeWebView != null) {
                hasCatInfo(nodeWebView)
                return mHasCat
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun hasCatInfo(rootNode: AccessibilityNodeInfo) {
        if (rootNode.childCount > 0) {
            for (i in 0 until rootNode.childCount) {
                if (rootNode.getChild(i) != null) {
                    hasCatInfo(rootNode.getChild(i))
                }
            }
        } else {
            if (rootNode.text != null) {
                if (rootNode.text.toString() == "累计任务奖励") {
                    mHasCat = true
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun performSth(rootNode: AccessibilityNodeInfo) {
        if (rootNode.childCount > 0) {
            for (i in 0 until rootNode.childCount) {
                if (rootNode.getChild(i) != null) {
                    performSth(rootNode.getChild(i))
                }
            }
        } else {
            if (rootNode.text != null) {

                Log.d(TAG, rootNode.text.toString() + " " + rootNode.viewIdResourceName)
//                if (rootNode.viewIdResourceName == "wall-warper") {
//                    Log.d(TAG, "wall childCount-->" + rootNode.childCount)
//                    rootNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//                }
//                if (rootNode.text.toString() == "我的猫，点击撸猫") {
//                    rootNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//                }
                if (rootNode.text.toString().contains(Params.keyword)) {
                    Log.d(TAG, "执行点击事件 keyword:" + Params.keyword)
                    rootNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//                    uiHandler.postDelayed(Runnable {
//                        performGlobalAction(GESTURE_SWIPE_DOWN)
//                    }, 2000)
                    uiHandler.postDelayed(Runnable {
                        performGlobalAction(GLOBAL_ACTION_BACK)
                    }, Params.viewTime * 1000)
                    return
                }
            }
        }
    }

    private fun findWebViewNode(rootNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        val nodeInfo: AccessibilityNodeInfo
        for (i in 0 until rootNode?.childCount) {
            val child = rootNode.getChild(i)
            if (child != null) {
                if ("com.uc.webview.export.WebView" == child.className) {
                    nodeInfo = child
                    return nodeInfo
                }
            }
            if (child != null && child.childCount > 0) {
                findWebViewNode(child)
            }
        }
        return null
    }

//    }

}