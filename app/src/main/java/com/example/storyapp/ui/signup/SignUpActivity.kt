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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.api.retrofit.ApiConfig
import com.example.storyapp.custom.CustomButton
import com.example.storyapp.custom.CustomEditText
import com.example.storyapp.data.repository.RegisterRepository
import com.example.storyapp.databinding.ActivitySignUpBinding
import com.example.storyapp.ui.ViewModelFactory
import com.example.storyapp.ui.login.SessionViewModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.HttpException

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var customButton: CustomButton
    private lateinit var usernameEditText: CustomEditText
    private lateinit var viewModel: SignUpViewModel
    private val sessionViewModel by viewModels<SessionViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        customButton = findViewById(R.id.signupButton)
        usernameEditText = findViewById(R.id.nameEditTextLayout)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        usernameEditText.addTextChangedListener(object : TextWatcher{
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
                viewModel.validatePassword(s.toString())
                binding.passwordEditTextLayout.error = if (!viewModel.isPasswordValid) getString(R.string.error) else null
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
                viewModel.validateEmail(s.toString())
                binding.emailEditTextLayout.error = if (!viewModel.isEmailValid) getString(R.string.error_email) else null
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: android.text.Editable) {
                // Do nothing.
            }
        })

    }

    private fun setMyButtonEnable() {
        val result = usernameEditText.text
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
        viewModel = SignUpViewModel(RegisterRepository(
            ApiConfig.getApiService(
                sessionViewModel.getSession().toString()
            )
        ))

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditTextLayout.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            binding.progressBar.visibility = View.VISIBLE

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                binding.progressBar.visibility = View.GONE
                Snackbar.make(binding.root, "Please fill all the fields", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(name, email, password).observe(this) { result ->
                when (result) {
                    is SignUpViewModel.RegisterResult.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val response = result.response
                        if (response.error) {
                            Log.d("SignUpActivity", "Registration failed ${response.message}")
                            Snackbar.make(binding.root, "Email is already Taken!", Snackbar.LENGTH_SHORT).show()
                        } else {
                            Log.d("SignUpActivity", "Registration successful ${response.message}")
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
                    is SignUpViewModel.RegisterResult.Error -> {
                        Log.e("SignUpActivity", "Error during registration: ${result.message}")
                        Snackbar.make(binding.root, "Email is already Taken!", Snackbar.LENGTH_SHORT).show()
                    }
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}