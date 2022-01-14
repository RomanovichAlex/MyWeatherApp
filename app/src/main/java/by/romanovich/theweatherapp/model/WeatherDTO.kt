package by.romanovich.theweatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherDTO (
    val now: Long,

    @SerializedName( "now_dt")
    val nowDt: String,

    val info: Info,
    val fact: Fact,
    val forecast: Forecast
)

data class Fact (
    @SerializedName("obs_time")
    val obsTime: Long,

    val temp: Long,

    @SerializedName("feels_like")
    val feelsLike: Long,

    val icon: String,
    val condition: String,

    @SerializedName("wind_speed")
    val windSpeed: Double,

    @SerializedName("wind_dir")
    val windDir: String,

    @SerializedName("pressure_mm")
    val pressureMm: Long,

    @SerializedName("pressure_pa")
    val pressurePa: Long,

    val humidity: Long,
    val daytime: String,
    val polar: Boolean,
    val season: String,
    val season2: String,

    @SerializedName("wind_gust")
    val windGust: Double
)

data class Forecast (
    val date: String,

    @SerializedName("date_ts")
    val dateTs: Double,

    val week: Double,
    val sunrise: String,
    val sunset: String,

    @SerializedName("moon_code")
    val moonCode: Long,

    @SerializedName("moon_text")
    val moonText: String,

    val parts: List<Part>
)

data class Part (
    @SerializedName("part_name")
    val partName: String,

    @SerializedName("temp_min")
    val tempMin: Long,

    @SerializedName("temp_avg")
    val tempAvg: Long,

    @SerializedName("temp_max")
    val tempMax: Long,

    @SerializedName("wind_speed")
    val windSpeed: Double,

    @SerializedName("wind_gust")
    val windGust: Double,

    @SerializedName("wind_dir")
    val windDir: String,

    @SerializedName("pressure_mm")
    val pressureMm: Double,

    @SerializedName("pressure_pa")
    val pressurePa: Double,

    val humidity: Double,

    @SerializedName("prec_mm")
    val precMm: Double,

    @SerializedName("prec_prob")
    val precProb: Double,

    @SerializedName("prec_period")
    val precPeriod: Double,

    val icon: String,
    val condition: String,

    @SerializedName("feels_like")
    val feelsLike: Long,

    val daytime: String,
    val polar: Boolean
)

data class Info (
    val url: String,
    val lat: Double,
    val lon: Double
)