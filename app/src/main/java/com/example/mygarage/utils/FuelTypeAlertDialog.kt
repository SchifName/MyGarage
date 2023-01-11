package com.example.mygarage.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.example.mygarage.R
import com.example.mygarage.model.fuelType
import com.google.android.material.textfield.TextInputEditText

fun fuelTypeAlertDialog(context: Context, text: TextInputEditText){

    val values = fuelType.values()

    val items = arrayOfNulls<CharSequence>(values.size)

    for (i in values.indices) {
        items[i] = values[i].toString()
    }

    val builder: AlertDialog.Builder = AlertDialog.Builder(context)

    builder.setTitle(context.getString(R.string.choose_fuel_type_title))

    builder.setSingleChoiceItems(items, -1) { _: DialogInterface, which ->
        text.setText(items[which].toString())
    }

    builder.setItems(items) { _: DialogInterface, which ->
        text.setText(items[which].toString())
    }

    builder.setPositiveButton(context.getString(R.string.delete_car_dialog_positive_button)) { _: DialogInterface, _ -> }

    builder.setNegativeButton(context.getString(R.string.delete_car_dialog_negative_button)) { _: DialogInterface, _ ->
        text.setText("")
        text.text = null
    }

    builder.show()
}