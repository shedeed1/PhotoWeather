package com.robusta.photoweather.data.response
data class _List (

	val id : Int,
	val name : String,
	val coord : Coord,
	val main : Main,
	val dt : Int,
	val wind : Wind,
	val sys : Sys,
	val rain : String,
	val snow : String,
	val clouds : Clouds,
	val weather : List<Weather>
)