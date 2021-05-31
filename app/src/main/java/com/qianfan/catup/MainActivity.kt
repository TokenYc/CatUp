package com.qianfan.catup

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.text.TextUtils.SimpleStringSplitter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateKeyword()
        updateViewTime()
        btn_open_permission.setOnClickListener {
            if (!isAccessibilitySettingsOn(this, CatService::class.java)) {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }
        btn_save_keyword.setOnClickListener {
            val keyword = et_keyword.text.toString()
            if (!TextUtils.isEmpty(keyword)) {
                Params.keyword = keyword
                updateKeyword()
                Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "请输入关键词", Toast.LENGTH_SHORT).show()
            }
        }

        btn_save_view_time.setOnClickListener {
            val viewTime = et_view_time.text.toString()
            if (!TextUtils.isEmpty(viewTime) && viewTime.isDigitsOnly()) {
                Params.viewTime = viewTime.toLong()
                updateViewTime()
                Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "请输入正确格式的浏览时间", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateKeyword() {
        tv_keyword.text = "当前点击关键词：" + Params.keyword
    }

    @SuppressLint("SetTextI18n")
    fun updateViewTime() {
        tv_view_time.text = "当前页面浏览时间：" + Params.viewTime + "秒"
    }


    fun isAccessibilitySettingsOn(
        mContext: Context,
        clazz: Class<out AccessibilityService?>
    ): Boolean {
        var accessibilityEnabled = 0
        val service: String =
            mContext.getPackageName().toString() + "/" + clazz.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.getApplicationContext().getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        val mStringColonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue: String = Settings.Secure.getString(
                mContext.getApplicationContext().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}