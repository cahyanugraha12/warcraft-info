package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.calculator.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.R

class CalculatorActivity : AppCompatActivity() {
    private var currentTotal: Int = 0
    private var currentNumberInString: String = ""
    private var prevOp: String = "<>"

    private external fun nativeAdd(first: Int, second: Int): Int
    private external fun nativeSubtract(first: Int, second: Int): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calculator_activity)

        initNumberButtons()
        initOpButtons()
    }

    private fun initNumberButtons() {
        setNumberButtonOnClickListener(findViewById(R.id.number_zero), "0")
        setNumberButtonOnClickListener(findViewById(R.id.number_one), "1")
        setNumberButtonOnClickListener(findViewById(R.id.number_two), "2")
        setNumberButtonOnClickListener(findViewById(R.id.number_three), "3")
        setNumberButtonOnClickListener(findViewById(R.id.number_four), "4")
        setNumberButtonOnClickListener(findViewById(R.id.number_five), "5")
        setNumberButtonOnClickListener(findViewById(R.id.number_six), "6")
        setNumberButtonOnClickListener(findViewById(R.id.number_seven), "7")
        setNumberButtonOnClickListener(findViewById(R.id.number_eight), "8")
        setNumberButtonOnClickListener(findViewById(R.id.number_nine), "9")
    }

    private fun setNumberButtonOnClickListener(button: Button, number: String) {
        button.setOnClickListener {
            val calcDisplay: TextView = findViewById(R.id.calc_display)
            if (this.currentNumberInString != "") {
                calcDisplay.text = "${this.currentNumberInString}${number}"
            } else {
                calcDisplay.text = number
            }
            this.currentNumberInString = calcDisplay.text.toString()
        }
    }

    private fun initOpButtons() {
        setOpButtonOnClickListener(findViewById(R.id.op_plus), "+")
        setOpButtonOnClickListener(findViewById(R.id.op_minus), "-")
        setOpButtonOnClickListener(findViewById(R.id.op_result), "=")
        findViewById<Button>(R.id.op_clear).setOnClickListener {
            currentTotal = 0
            currentNumberInString = ""
            prevOp = "<>"
            findViewById<TextView>(R.id.calc_display).text = ""
        }
    }

    private fun setOpButtonOnClickListener(button: Button, op: String) {
        button.setOnClickListener {
            val calcDisplay: TextView = findViewById(R.id.calc_display)

            // Process previous operation
            when (this.prevOp) {
                "+" ->  {
                    this.currentTotal = nativeAdd(this.currentTotal, calcDisplay.text.toString().toInt())
                }
                "-" -> {
                    this.currentTotal = nativeSubtract(this.currentTotal, calcDisplay.text.toString().toInt())
                }
                // <> indicate reset. That is, no operation so just store the displayed text as is.
                "<>" -> {
                    this.currentTotal = calcDisplay.text.toString().toInt()
                }
            }

            // Store the pressed button operation to be processed later when plus, minus, or equal button pressed.
            this.prevOp = op
            // If operation is result (equal button), then display the result and reset the operation.
            if (op == "=") {
                calcDisplay.text = this.currentTotal.toString()
                this.prevOp = "<>"
            }

            this.currentNumberInString = ""
        }
    }

    companion object {
        // Used to load the 'native_math' library on application startup.
        init {
            System.loadLibrary("native_math")
        }
    }
}