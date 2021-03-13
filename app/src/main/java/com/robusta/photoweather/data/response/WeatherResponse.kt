
package com.robusta.photoweather.data.response
data class WeatherResponse (
	val message : String,
	val cod : Int,
	val count : Int,
	val list : List<_List>
)