package com.example.shop

import android.app.Application

class MyApplication : Application() {
    companion object {
        var selectedLanguage: String = ""
    }
}