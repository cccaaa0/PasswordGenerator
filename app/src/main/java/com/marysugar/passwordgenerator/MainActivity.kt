package com.marysugar.passwordgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val passwordList = ArrayList<String>()

        (1..12).forEach { _ ->
            passwordList.add(generatePassword())
        }

        val gridView = findViewById<GridView>(R.id.gv_passwords)
        val adapter = ArrayAdapter(
            this,
            R.layout.password_view,
            R.id.tv_password,
            passwordList
        )

        gridView.adapter = adapter
        gridView.onItemClickListener = ListItemClickListener()

        val checkBox = findViewById<CheckBox>(R.id.cb_alphanumeric_char)
        checkBox.isChecked = true

        val radioGroup = findViewById<RadioGroup>(R.id.rg_num_char)
        radioGroup.check(R.id.rb_8_char)

        radioGroup.setOnCheckedChangeListener { _, checkId ->
            val radioButton = findViewById<RadioButton>(checkId)
            Toast.makeText(this, radioButton.text, Toast.LENGTH_SHORT).show()
        }

        val button = findViewById<Button>(R.id.bt_generate)
        button.setOnClickListener {
            passwordList.clear()
            (1..12).forEach { _ ->
                passwordList.add(generatePassword())
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun generatePassword() : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..8)
                .map { allowedChars.random() }
                .joinToString("")
    }

    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val item = parent?.getItemAtPosition(position)

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // Creates a new text clip to put on the clipboard
            val clip: ClipData = ClipData.newPlainText("simple text", item.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this@MainActivity, "Copy", Toast.LENGTH_SHORT).show()
        }
    }
}