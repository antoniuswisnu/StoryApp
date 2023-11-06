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
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.api.response.LoginResponse
import com.example.storyapp.api.response.LoginResult
import com.example.storyapp.api.retrofit.ApiConfig.Companion.getApiService
import com.example.storyapp.data.pref.LoginPreferences
import com.example.storyapp.data.repository.LoginRepository
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.ViewModelFactory
import com.example.storyapp.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar

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

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                loginViewModel.validatePassword(s.toString())
                binding.passwordEditText.error = if (!loginViewModel.isPasswordValid) getString(R.string.error) else null
            }
            override fun afterTextChanged(s: android.text.Editable) {
                // Do nothing.
            }
        })

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                loginViewModel.validateEmail(s.toString())
                binding.emailEditText.error = if (!loginViewModel.isEmailValid) getString(R.string.error_email) else null
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

        Log.d("LoginActivity", "setupAction: $sessionViewModel")

        binding.loginButton.setOnClickListener {
            var email = binding.emailEditText.text.toString()
            var password = binding.passwordEditText.text.toString()
            binding.progressBar.visibility = View.VISIBLE

            if(email.isEmpty() || password.isEmpty()) {
                binding.progressBar.visibility = View.GONE
                Snackbar.make(binding.root, "Email or Password must be filled", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
            loginViewModel.login(email, password).observe(this) { response ->
                    if (response.error == true) {
                        Snackbar.make(binding.root, "Login failed Email must be a valid email or Incorrect password", Snackbar.LENGTH_SHORT).show()
                        email = ""
                        password = ""
                    } else {
                        Snackbar.make(binding.root, "Login Successful", Snackbar.LENGTH_SHORT).show()
                        saveLoginData(response)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
            }
            }
        }
    }

    private fun saveLoginData(loginResponse: LoginResponse) {
        val loginPreference = LoginPreferences(this)
        val loginResult = loginResponse.loginResult
        val loginModel = LoginResult(
            name = loginResult?.name, userId = loginResult?.userId, token = loginResult?.token
        )
        loginPreference.setLogin(loginModel)
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
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)


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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}