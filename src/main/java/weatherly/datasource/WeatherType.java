package weatherly.datasource;

public enum WeatherType {
    CLEAR_SKY,
    CLOUDS_BROKEN,
    CLOUDS_FEW,
    CLOUDS_OVERCAST,
    CLOUDS_SCATTERED,
    DRIZZLE,
    DRIZZLE_HEAVY,
    DRIZZLE_LIGHT,
    FOG,
    MIST,
    RAIN,
    RAIN_HEAVY,
    RAIN_LIGHT,
    RAIN_VERY_HEAVY,
    SNOW,
    SNOW_HEAVY,
    SNOW_LIGHT,
    THUNDERSTORM,
    THUNDERSTORM_HEAVY,
    THUNDERSTORM_LIGHT;

    public String getReadableDescription() {
	switch (this) {
	case CLOUDS_BROKEN:
	    return "Broken clouds";
	case CLEAR_SKY:
	    return "Clear sky";
	case CLOUDS_FEW:
	    return "Few clouds";
	case MIST:
	    return "Mist";
	case CLOUDS_SCATTERED:
	    return "Scattered clouds";
	case SNOW:
	    return "Snow";
	case THUNDERSTORM:
	    return "Thunderstorm";
	default:
	    return "?";

	}
    }
}
