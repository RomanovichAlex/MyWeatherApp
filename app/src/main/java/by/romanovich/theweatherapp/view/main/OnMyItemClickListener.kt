package by.romanovich.theweatherapp.view.main

import by.romanovich.theweatherapp.model.Weather

interface OnMyItemClickListener {
    fun onItemClick(weather: Weather)
}