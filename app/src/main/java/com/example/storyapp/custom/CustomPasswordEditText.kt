package com.example.storyapp.custom

// PasswordEditText.kt
import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

class CustomPasswordEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = if (Locale.getDefault().language == "in"){
            "Masukkan password Anda"
        } else {
            "Enter your password"
        }
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                val parent = parent.parent as? TextInputLayout
                if (password.length < 8) {
                    if (Locale.getDefault().language == "in"){
                        parent?.error = "Password kurang dari 8 karakter"
                    } else {
                        parent?.error = "Password less than 8 characters"
                    }
                } else {
                    parent?.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}
