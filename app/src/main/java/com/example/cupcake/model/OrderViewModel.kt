package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00
class OrderViewModel: ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price){
        NumberFormat.getCurrencyInstance().format(it)
    }// Formatted price will be in the form of a string containing currency symbol, map() takes 2 args: LiveData to be transformed, function.

    val dateOptions = getPickUpOptions()

    init {
        resetOrder()
    }

    fun setName(receiver_name: String){
        _name.value = receiver_name
    }

    fun setQuantity(numberCupcakes: Int){
        _quantity.value = numberCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String){
        _flavor.value = desiredFlavor
    }

    private fun updatePrice(){
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE // ?: 0 means if quantity is null then consider 0
        if (dateOptions[0] == _date.value){
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }
    fun resetOrder(){
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    fun setDate(pickUpDate: String){
        _date.value = pickUpDate
        updatePrice()
    }

    fun hasNoFlavorSet(): Boolean{
        return _flavor.value.isNullOrEmpty()
    }

    private fun getPickUpOptions(): List<String>{
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        // Create a list of dates starting with the current date and the following 3 dates
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }
}