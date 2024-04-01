package com.example.wat2eat.widgets

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.play.integrity.internal.i

class MultiSelectDialog @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    private var selectedItems: BooleanArray = booleanArrayOf()
    private val itemList: MutableList<Int> = mutableListOf()
    private lateinit var itemsArray: Array<String>

    init {
        setOnClickListener {
            showMultiSelectDialog()
        }
    }

    fun setItems(items: Array<String>) {
        itemsArray = items
        selectedItems = BooleanArray(items.size)
    }
    fun getItems(): List<String> {
        return itemList.map { itemsArray[it] }
    }

    private fun showMultiSelectDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select Items")
            .setCancelable(true)
            .setMultiChoiceItems(itemsArray, selectedItems) { _, which, isChecked ->
                if (isChecked) {
                    itemList.add(which)
                    itemList.sort()
                } else {
                    itemList.remove(which)
                }
            }
            .setPositiveButton("OK") { dialog, _ ->
                val selectedValues = itemList.map { itemsArray[it] }.joinToString(", ")
                text = selectedValues
                dialog.dismiss()
            }
            .setNeutralButton("Clear") { _, _ ->
                itemList.clear()
                selectedItems.fill(false)
                text = ""
            }

        val dialog = builder.create()
        dialog.show()
    }
}
