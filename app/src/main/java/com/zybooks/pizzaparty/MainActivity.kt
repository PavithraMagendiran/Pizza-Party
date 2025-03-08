package com.zybooks.pizzaparty

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

//log messages to Logcat to see how changes to the activity produce calls to the lifecycle callbacks
const val TAG = "MainActivity"
private const val KEY_TOTAL_PIZZAS = "totalPizzas"


class MainActivity : AppCompatActivity() {
    //declaring 3 variable whose value can be changed
    private lateinit var numAttendEditText: EditText
    private lateinit var numPizzasTextView: TextView
    private lateinit var howHungrySpinner: Spinner
    private var totalPizzas = 0

    //  To call the activity_main layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate")

        numAttendEditText = findViewById(R.id.numOfPeople)
        numPizzasTextView = findViewById(R.id.numOfPizzas)
        howHungrySpinner = findViewById(R.id.spinner_size)

        // Restore state
        if (savedInstanceState != null) {

            totalPizzas = savedInstanceState.getInt(KEY_TOTAL_PIZZAS, 0)
            numAttendEditText.setText(savedInstanceState.getString("numAttendStr", ""))
            howHungrySpinner.setSelection(savedInstanceState.getInt("spinnerPosition", 0))
            displayTotal()
        }


        //Text Watcher
        val lengthErrorMessage = findViewById<TextView>(R.id.length_error_message)
        lengthErrorMessage.visibility = View.VISIBLE     // Error Message is Visible
        numAttendEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (s.length > 8) {
                    numAttendEditText.text.clear() // Clear input which typed by the User
                    lengthErrorMessage.visibility = View.VISIBLE     // Error Message is Visible

                } else {

                    lengthErrorMessage.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {}   //Editable text
        })
        // Text Watcher ends

        // Logic for Spinner

        val adapter = ArrayAdapter.createFromResource(
            this, R.array.sizes_array, android.R.layout.simple_spinner_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        howHungrySpinner.adapter = adapter

        howHungrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item = parent?.getItemAtPosition(position) as String
                Toast.makeText(this@MainActivity, item, Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //Spinner Logic ends
    }

    //To restore the full state of an activity,before an activity is stopped
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_TOTAL_PIZZAS, totalPizzas)
        outState.putString("numAttendStr", numAttendEditText.text.toString()) // Save EditText input
        outState.putInt("spinnerPosition", howHungrySpinner.selectedItemPosition) // Save Spinner selection
    }

    //function to calculate number of pizzas
    // Controller
    // @param : view
    fun calculateClick(view: View) {

        //Display the text when the button is clicked
        Toast.makeText(this,"Button Clicked", Toast.LENGTH_SHORT).show()

        val numAttendStr = numAttendEditText.text.toString()
        val numAttend =numAttendStr.toIntOrNull() ?: 0   //Parses the string as an Int and returns the result
                                                         // or null if the string is not a valid representation of a number.

        //Get hunger level selection
        val hungerLevel = when (howHungrySpinner.selectedItemPosition) {
            0-> PizzaCalculator.HungerLevel.LIGHT
            1-> PizzaCalculator.HungerLevel.MEDIUM
            else -> PizzaCalculator.HungerLevel.RAVENOUS
        }

        // Get the number of pizzas needed
        //@Param numAttend, hungerLevel
        val calc = PizzaCalculator(numAttend, hungerLevel)
        totalPizzas = calc.totalPizzas
        displayTotal()

    }
    // Place totalPizzas into the string resource and display
    private fun displayTotal() {
        val totalText = getString(R.string.totalPizzasNum, totalPizzas)
        numPizzasTextView.text = totalText
    }
}

