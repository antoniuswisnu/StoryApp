package com.example.storyapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.R
import com.example.storyapp.api.response.LoginResult
import com.example.storyapp.data.pref.LoginPreferences
import com.example.storyapp.ui.main.MainActivity
import com.example.storyapp.ui.welcome.WelcomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var preference: LoginPreferences
    private lateinit var loginModel: LoginResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        preference = LoginPreferences(this)
        loginModel = preference.getUser()

        lifecycleScope.launch{
            withContext(Dispatchers.Main){
                if(loginModel.token != null){
                    Handler(mainLooper).postDelayed({
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 2000)
                }else{
                    Handler(mainLooper).postDelayed({
                        val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 2000)
                }
            }
        }
    }
}