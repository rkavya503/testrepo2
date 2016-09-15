package com.akuacom.pss2.weather;

import org.apache.commons.lang.StringUtils;

public class WeatherUtils {

	public static final String SAND_STORM = "du";
	public static final String THUNDER_STORM="tsra";
	public static final String RAIN_SNOW_HAIL="mix";
	public static final String RAIN_HAIL="raip";
	public static final String HAIL="ip";
	public static final String SLEET="rasn";
	public static final String SNOW="sn";
	public static final String SHOWER="hi_shwrs";
	public static final String RAIN="ra";
	public static final String FOG="fg";
	public static final String FROST_RAIN="fzrn";
	public static final String FROST="fr";
	public static final String HOT="hot";
	public static final String COLD="cold";
	public static final String WINDY="wind";
	public static final String OVERCAST="ovc";
	public static final String CLOUDY="bkn";
	public static final String MOSTLY_SUNNY="sct";
	public static final String SUNNY="skc";
	
	public static final String SAND_STORM_CATE = "sand_storm";
	public static final String THUNDER_STORM_CATE="thunder_storm";
	public static final String RAIN_SNOW_HAIL_CATE="mix";
	public static final String RAIN_HAIL_CATE="rain_hail";
	public static final String HAIL_CATE="hail";
	public static final String SLEET_CATE="sleet";
	public static final String SNOW_CATE="snow";
	public static final String SHOWER_CATE="shower";
	public static final String RAIN_CATE="rain";
	public static final String FOG_CATE="fog";
	public static final String FROST_RAIN_CATE="forst_rain";
	public static final String FROST_CATE="frost";
	public static final String HOT_CATE="hot";
	public static final String COLD_CATE="cold";
	public static final String WINDY_CATE="windy";
	public static final String OVERCAST_CATE="overcast";
	public static final String CLOUDY_CATE="cloudy";
	public static final String MOSTLY_SUNNY_CATE="mostly_sunny";
	public static final String SUNNY_CATE="sunny";

	public static WeatherCategory getIconBytype(String type){
		WeatherCategory entity = new WeatherCategory();
		if(StringUtils.isBlank(type)){
			return entity;
		}
		String in = type.toLowerCase();
		//1.storm Sandstorm, Duststorm, Sand, Dust
		if(in.contains("sandstorm")||in.contains("duststorm")||in.contains("sand")||in.contains("dust")){
			entity.setIcon(SAND_STORM);
			entity.setCategory(SAND_STORM_CATE);
			return entity;
		}
		//2.Thunderstorms, Thundershowers, Storm, Lightning
		if(in.contains("thunder")||in.contains("thunderstorms")||in.contains("thundershowers")||in.contains("storm")||in.contains("lightning")){
			entity.setIcon(THUNDER_STORM);
			entity.setCategory(THUNDER_STORM_CATE);
			return entity;
		}
		//3.Hail, rain and snow mixed
		if(in.contains("hail")&&(in.contains("rain")||in.contains("shower"))&&in.contains("snow")){
			entity.setIcon(RAIN_SNOW_HAIL);
			entity.setCategory(RAIN_SNOW_HAIL_CATE);
			return entity;
		}
		//4.Hail, rain  mixed
		if(in.contains("hail")&&(in.contains("rain")||in.contains("shower"))){
			entity.setIcon(RAIN_HAIL);
			entity.setCategory(RAIN_HAIL_CATE);
			return entity;
		}
		//5.Hail
		if(in.contains("hail")){
			entity.setIcon(HAIL);
			entity.setCategory(HAIL_CATE);
			return entity;
		}
		//6.Sleet rain and snow
		if((in.contains("snow")&&(in.contains("rain")||in.contains("shower")))||(in.contains("sleet"))){
			entity.setIcon(SLEET);
			entity.setCategory(SLEET_CATE);
			return entity;
		}
		//7 Snow Showers, Flurries Snow, Heavy Snow, Snowfall Light Snow
		if(in.contains("snow")||in.contains("flurries")||in.contains("snowfall")){
			entity.setIcon(SNOW);
			entity.setCategory(SNOW_CATE);
			return entity;
		}
		//8 Showers, Heavy Showers, Rainshower Light Showers
		if(in.contains("showers")||in.contains("shower")||in.contains("rainshower")){
			entity.setIcon(SHOWER);
			entity.setCategory(SHOWER_CATE);
			return entity;
		}
		//TODO: heavy rain?
		//9 Rain Drizzle, Light Rain
		if(in.contains("drizzle")||in.contains("rain")){
			entity.setIcon(RAIN);
			entity.setCategory(RAIN_CATE);
			return entity;
		}
		//10 Fog  Mist Haze
		if(in.contains("fog")||in.contains("mist")||in.contains("haze")){
			entity.setIcon(FOG);
			entity.setCategory(FOG_CATE);
			return entity;
		}
		//11 Frost rain snow
		if(in.contains("frost")&&(in.contains("rain")||in.contains("shower"))&&in.contains("snow")){
			entity.setIcon(RAIN_SNOW_HAIL);
			entity.setCategory(RAIN_SNOW_HAIL_CATE);
			return entity;
		}
		//12 Frost rain
		if(in.contains("frost")&&(in.contains("rain")||in.contains("shower"))){
			entity.setIcon(FROST_RAIN);
			entity.setCategory(FROST_RAIN_CATE);
			return entity;
		}
		//13 frost
		if(in.contains("frost")){
			entity.setIcon(FROST);
			entity.setCategory(FROST_CATE);
			return entity;
		}
		//14 Hot
		if(in.contains("hot")){
			entity.setIcon(HOT);
			entity.setCategory(HOT_CATE);
			return entity;
		}
		//15 Cold, Chilly
		if(in.contains("cold")||in.contains("chilly")){
			entity.setIcon(COLD);
			entity.setCategory(COLD_CATE);
			return entity;
		}
		//16 Windy, Squall, Gale
		if(in.contains("windy")||in.contains("wind")||in.contains("squall")||in.contains("gale")){
			entity.setIcon(WINDY);
			entity.setCategory(WINDY_CATE);
			return entity;
		}
		//17 Overcast
		if(in.contains("overcast")){
			entity.setIcon(OVERCAST);
			entity.setCategory(OVERCAST_CATE);
			return entity;
		}
		//18 Cloudy, Mostly Cloudy Partly Cloudy,
		if(in.contains("cloudy")||in.contains("mostly cloudy")||in.contains("partly cloudy")||in.contains("cloud")){
			entity.setIcon(CLOUDY);
			entity.setCategory(CLOUDY_CATE);
			return entity;
		}
		//19 Bright, Sunny, Fair Fine, Clear
		if ((in.contains("bright") || in.contains("sunny")|| in.contains("fair") || in.contains("fine") || in.contains("clear"))
				&& (in.contains("almost") || in.contains("mostly")|| in.contains("partly") || in.contains("period"))) {
			entity.setIcon(MOSTLY_SUNNY);
			entity.setCategory(MOSTLY_SUNNY_CATE);
			return entity;
		}
		//20 Sunny Period, Partly Bright, Mild  Partly Bright Mostly sunny.
		if (in.contains("bright") || in.contains("sunny")|| in.contains("fair") || in.contains("fine") || in.contains("clear")) {
			entity.setIcon(SUNNY);
			entity.setCategory(SUNNY_CATE);
			return entity;
		}

		return entity;

	}
}
