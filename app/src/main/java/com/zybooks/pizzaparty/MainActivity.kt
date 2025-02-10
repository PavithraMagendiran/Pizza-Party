package com.zybooks.pizzaparty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView


const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    //declaring 3 variable whose value can be changed
    private lateinit var numAttendEditText: EditText
    private lateinit var numPizzasTextView: TextView
    private lateinit var howHungryRadioGroup: RadioGroup

    //  To call the activity_main layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Log.d(TAG, "onCreate was called")
        numAttendEditText = findViewById(R.id.numOfPeople)
        numPizzasTextView = findViewById(R.id.numOfPizzas)
        howHungryRadioGroup = findViewById(R.id.howHungryButton)
    }
    //function to calculate number of pizzas
    // @param : view
    fun calculateClick(view: View) {

        val numAttendStr = numAttendEditText.text.toString()
        val numAttend =numAttendStr.toIntOrNull() ?: 0   //Parses the string as an Int and returns the result
                                                         // or null if the string is not a valid representation of a number.

        // Algo to calculate pizzas based on their hungry level using radio button
        val hungerLevel = when (howHungryRadioGroup.checkedRadioButtonId) {
            R.id.lightRadioButton -> PizzaCalculator.HungerLevel.LIGHT
            R.id.mediumRadioButton -> PizzaCalculator.HungerLevel.MEDIUM
            else -> PizzaCalculator.HungerLevel.RAVENOUS
        }

        // Get the number of pizzas
        //@Param numAttend, hungerLevel
        val calc = PizzaCalculator(numAttend, hungerLevel)
        val totalPizzas = calc.totalPizzas

        // Place totalPizzas into the string resource and display
        val totalText = getString(R.string.totalPizzasNum, totalPizzas)
        numPizzasTextView.text = totalText

    }
}

