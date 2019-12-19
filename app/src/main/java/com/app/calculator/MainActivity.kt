package com.app.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NumberFormatException

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1Stored"

class MainActivity : AppCompatActivity() {


    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    //Variable to hold the operands

    private var operand1: Double? = null
    private var pendingOperand = "="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)



        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            }catch (e: NumberFormatException){
                newNumber.setText("")
            }
            pendingOperand = op
            displayOperation.text = pendingOperand
        }

        buttonEquals.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMultiple.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)

    }



    private fun performOperation(value: Double, operation: String) {



        if (operand1 == null) {
            operand1 = value
        } else {


            if (pendingOperand == "=") {
                pendingOperand = operation
            }

            when (pendingOperand) {
                "=" -> operand1 = value
                "/" -> operand1 = if(value==0.0){
                    Double.NaN
                }else{
                    operand1!! /value
                }
                "+"-> operand1=operand1!!+value
                "-"-> operand1=operand1!!-value
                "*"-> operand1=operand1!!*value
            }
        }

        result.setText(operand1.toString())
        newNumber.setText("")
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(operand1!=null){
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperand)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if(savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)){
            savedInstanceState.getDouble(STATE_OPERAND1)
        }else{
            null
        }

        pendingOperand= savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        displayOperation.text=pendingOperand
    }


    fun buttonNegClicked(view: View) {
        if(newNumber.text.isEmpty()){
            newNumber.append("-")
        }
        else{
            try {
                var neg = newNumber.text.toString().toDouble()
                neg*=-1
                newNumber.setText(neg.toString())
            }catch (e: NumberFormatException){
                newNumber.setText("")
            }

        }
    }


}
