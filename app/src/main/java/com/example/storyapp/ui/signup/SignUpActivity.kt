package com.example.storyapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.R
import com.example.storyapp.api.response.ErrorResponse
import com.example.storyapp.custom.CustomButton
import com.example.storyapp.custom.CustomEditText
import com.example.storyapp.databinding.ActivitySignUpBinding
import com.google.gson.Gson
import retrofit2.HttpException

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var customButton: CustomButton
    private lateinit var customEditText: CustomEditText
    private lateinit var viewModel: SignUpViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        customButton = findViewById(R.id.signupButton)
        customEditText = findViewById(R.id.nameEditTextLayout)

        setMyButtonEnable()

        customEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: android.text.Editable) {
                // Do nothing.
            }
        })

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
                if (!s.toString().contains("@email")) {
                    binding.emailEditTextLayout.error = getString(R.string.error_email)
                } else {
                    binding.emailEditTextLayout.error = null
                }
            }
            override fun afterTextChanged(s: android.text.Editable) {
                // Do nothing.
            }
        })

    }

    private fun setMyButtonEnable() {
        val result = customEditText.text
        customButton.isEnabled = result != null && result.toString().isNotEmpty()
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
//        val apiService = ApiConfig().getApiService()
//        val registerRepository = RegisterRepository(apiService)
//        viewModel = SignUpViewModel(registerRepository)

//        viewModel = SignUpViewModel(RegisterRepository(ApiConfig().getApiService())

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditTextLayout.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            try{
                viewModel.register(name, email, password).observe(this) { response ->
                    if (response != null) {
                        if (response.error) {
                            Log.d("SignUpActivity", "Registration failed ${response.message}")
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        } else {

                            Log.d("SignUpActivity", "Registration successful ${response.message} $")
                            Toast.makeText(
                                this,
                                "Registration successful ${response.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                Log.e("SignUpActivity", errorMessage.toString())
            }

            AlertDialog.Builder(this).apply {
                setTitle("Yeah!")
                setMessage("Akun dengan $email sudah jadi nih. Yuk, Buat Story Kamu!")
                setPositiveButton("Lanjut") { _, _ ->
                    finish()
                }
                create()
                show()
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
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}