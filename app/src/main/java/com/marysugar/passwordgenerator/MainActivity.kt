package com.marysugar.passwordgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.apache.commons.lang3.RandomStringUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val passwordList = ArrayList<String>()
        var numberOfCharacters = 8
        var passwordType = "alphanumeric"

        (1..12).forEach { _ ->
            passwordList.add(generatePassword(numberOfCharacters, passwordType))
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

        val rgPasswordType = findViewById<RadioGroup>(R.id.rg_password_type)
        rgPasswordType.check(R.id.rb_alphanumeric)

        rgPasswordType.setOnCheckedChangeListener { _, checkId ->
            val radioButton = findViewById<RadioButton>(checkId)
            passwordType = when (radioButton.text) {
                getString(R.string.rb_alphanumeric) -> "alphanumeric"
                getString(R.string.rb_alphanumeric_and_symbol) -> "alphanumericAndSymbol"
                else -> "alphanumeric"
            }
        }

        val rgNumOfChar = findViewById<RadioGroup>(R.id.rg_num_of_char)
        rgNumOfChar.check(R.id.rb_8_char)

        rgNumOfChar.setOnCheckedChangeListener { _, checkId ->
            val radioButton = findViewById<RadioButton>(checkId)
            numberOfCharacters = when (radioButton.text) {
                getString(R.string.rb_8_char) -> 8
                getString(R.string.rb_12_char) -> 12
                getString(R.string.rb_16_char) -> 16
                else -> 8
            }
        }

        val button = findViewById<Button>(R.id.bt_generate)
        button.setOnClickListener {
            passwordList.clear()
            (1..12).forEach { _ ->
                passwordList.add(generatePassword(numberOfCharacters, passwordType))
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun generatePassword(numberOfCharacters: Int, passwordType: String) : String {
        if (passwordType == "alphanumeric") {
            return RandomStringUtils.randomAlphanumeric(numberOfCharacters)
        }
        if (passwordType == "alphanumericAndSymbol") {
            return RandomStringUtils.randomAscii(numberOfCharacters)
        }
        return "Error"
    }

    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val item = parent?.getItemAtPosition(position)

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("password", item.toString())
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this@MainActivity, getString(R.string.cb_copy), Toast.LENGTH_SHORT).show()
        }
    }
}