package com.devas.currency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager


class MainActivity : AppCompatActivity() {
    var baseCurrency = "INR"
    var convertedCurrency = "GBP"
    var conversionRate = 0f

    var apiKey = "";

   //"a70f6edc195f15238cdd"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val ai: ApplicationInfo = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
       val key = ai.metaData["keyValue"].toString()
       apiKey = key
       spinnerSetup()
       textChange()
    }

    private fun textChange(){
        et_firstConversion.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Main", "Before Text Changed")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Main", "on Text Changed")
            }

            override fun afterTextChanged(p0: Editable?) {
                try{
                    getConversionRate()
                }catch (e: Exception){
                    Log.e("Main", "$e")
                    Toast.makeText(applicationContext, "Type a value", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun spinnerSetup() {
        val spinner: Spinner = findViewById(R.id.spinner_firstConversion)
        val spinner2: Spinner = findViewById(R.id.spinner_secondConversion)
        ArrayAdapter.createFromResource(this,R.array.currencies,android.R.layout.simple_spinner_item).also {
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter =adapter}
        ArrayAdapter.createFromResource(this,R.array.currencies,android.R.layout.simple_spinner_item).also {
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter =adapter}
        spinner2.setSelection(2)
        spinner.onItemSelectedListener = (object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                @Override

                baseCurrency = spinner.selectedItem.toString()
                getConversionRate()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d("Main", "NO item Selected")
            }

        })
        spinner2.onItemSelectedListener = (object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                convertedCurrency = spinner2.selectedItem.toString()
                getConversionRate()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d("Main", "NO item Selected")
            }

        })


    }

    private fun getConversionRate(){
      if(et_firstConversion!=null && et_firstConversion.text.isNotEmpty() && et_firstConversion.text.isNotBlank()){
          val apiString = "https://free.currconv.com/api/v7/convert?q=${baseCurrency}_${convertedCurrency}&compact=ultra&apiKey=${apiKey}"
          if(et_firstConversion==et_secondConversion){
              Toast.makeText(applicationContext,"Both currencies cannot be same",Toast.LENGTH_SHORT).show();
          }
          else{
              GlobalScope.launch(Dispatchers.IO){
                  try{
                      val apiResult = URL(apiString).readText()
                      Log.d("Main", "$apiResult")
                      val jsonObject = JSONObject(apiResult)
                      conversionRate = jsonObject.getString("${baseCurrency}_${convertedCurrency}").toFloat()
                      Log.d("Main","$conversionRate")


                      withContext(Dispatchers.Main){
                        et_secondConversion.setText((et_firstConversion.text.toString().toFloat()*conversionRate).toString())
                      }

                  }catch (e :Exception){ Log.e("Main", "$e") }
              }
          }
      }

    }

}