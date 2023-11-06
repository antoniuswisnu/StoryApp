package com.example.storyapp.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.util.PatternsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

class CustomEmailEditText : TextInputEditText {

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
            "Masukkan email Anda"
        } else {
            "Enter your email"
        }
        textAlignment = TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                val parent = parent.parent as? TextInputLayout
                if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (Locale.getDefault().language == "in"){
                        parent?.error = "Format email tidak valid"
                    } else {
                        parent?.error = "Invalid email format"
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