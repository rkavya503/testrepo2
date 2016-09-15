package com.akuacom.pss2.facdash.web.listener.uk;

public class TemperatureUtil {

	public static double FahrenheitToCelsius(double fah){
		double cel=0;
		cel = (fah-32)*5/9;
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		cel = Double.parseDouble(df.format(cel));
		return cel;
	}
	public static double CelsiusToFahrenheit(double cel){
		double fah=0;
		fah = cel*9/5+32;
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		fah = Double.parseDouble(df.format(fah));
		return fah;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(TemperatureUtil.CelsiusToFahrenheit(8));
		System.out.println(TemperatureUtil.FahrenheitToCelsius(80));
	}

}
