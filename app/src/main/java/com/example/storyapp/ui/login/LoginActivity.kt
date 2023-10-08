package com.example.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.api.retrofit.ApiConfig.Companion.getApiService
import com.example.storyapp.data.repository.LoginRepository
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.ViewModelFactory
import com.example.storyapp.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private val sessionViewModel by viewModels<SessionViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    binding.passwordEditTextLayout.error = getString(R.string.error)
                } else {
                    binding.passwordEditTextLayout.error = null
                }
            }
            override fun afterTextChanged(s: android.text.Editable) {
                // Do nothing.
            }
        })

        binding.emailEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // error ketika tidak ada @
                if (!s.toString().contains("@email")) {
                    binding.emailEditTextLayout.error = getString(R.string.error_email)
                } else {
                    binding.emailEditTextLayout.error = null
                }
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: android.text.Editable) {
                // Do nothing.
            }
        })
    }

    private fun setMyButtonEnable() {
        val result = binding.emailEditText.text
        binding.loginButton.isEnabled = result != null && result.toString().isNotEmpty()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        loginViewModel = LoginViewModel(LoginRepository(getApiService(sessionViewModel.getSession().toString())))

        binding.loginButton.setOnClickListener {
            var email = binding.emailEditText.text.toString()
            var password = binding.passwordEditText.text.toString()

            loginViewModel.login(email, password).observe(this) { response ->
                    if (response.error == true) {
                        Log.d("LoginActivity", "Login failed ${response.message}")
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        email = ""
                        password = ""
                    } else {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                        val userId = response.loginResult?.userId
                        val name = response.loginResult?.name
                        val token = response.loginResult?.token
                        sessionViewModel.saveSession(
                            UserModel(
                                email,
                                token.toString(),
                                true
                            )
                        )

                        Log.d("LoginActivity", "Login successful $userId $name $token")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

}