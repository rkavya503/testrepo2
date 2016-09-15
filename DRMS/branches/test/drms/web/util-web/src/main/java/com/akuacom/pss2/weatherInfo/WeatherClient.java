package com.akuacom.pss2.weatherInfo;

import gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.FormatType;
import gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.ProductType;
import gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.UnitType;
import gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.WeatherParametersType;
import gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.ZipCodeListType;
import gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListZipCode;
import gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListZipCodeResponse;
import gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgen;
import gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenByDay;
import gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenByDayResponse;
import gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenResponse;
import gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLStub;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Logger;
import org.apache.axis2.AxisFault;


public class WeatherClient {

	private static final String SEI_URL= "http://graphical.weather.gov/xml/SOAP_server/ndfdXMLserver.php";
	private static final String DEFAULT_ZIPCODE= "94118";


    public static HashMap<String, Object> getTodayWeather(double latitude, double longitude) throws AxisFault, RemoteException {
	NdfdXMLStub stub = new NdfdXMLStub(SEI_URL);
 
		NDFDgen par = new NDFDgen();

		Calendar start = GregorianCalendar.getInstance();

		par.setStartTime(start);
		par.setEndTime(start);

		par.setLatitude(new BigDecimal(latitude));
		par.setLongitude(new BigDecimal(longitude));

        par.setProduct(ProductType.value1);
        par.setUnit(UnitType.e);

		WeatherParametersType type = new WeatherParametersType();

		type.setIcons(true);
		type.setMaxt(true);
		type.setMint(true);
		type.setRh(true);
		type.setWx(true);
		type.setWdir(true);
		type.setWspd(true);
		type.setTemp_r(true);
		type.setAppt(true);

		par.setWeatherParameters(type);
        NDFDgenResponse rs = stub.nDFDgen(par);

		InputStream is = new ByteArrayInputStream(rs.getDwmlOut().getBytes());
        HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			WeatherXMLReader myReader = WeatherXMLReaderFactory.getInstance();
			myReader.parserNDFDgenResponse(map, is);

		} finally {
			try {

				is.close();
			} catch (IOException e) {
				System.out.println( e.getMessage());
			}
		}
		return map;
	}



    public static HashMap<String, String> getLanLonByZipcode(String zipcode) throws AxisFault, RemoteException {

        if (zipcode == null || zipcode.trim().length() == 0) {
			zipcode = DEFAULT_ZIPCODE;
		}

		NdfdXMLStub stub = new NdfdXMLStub(SEI_URL);
		LatLonListZipCode request = new LatLonListZipCode();
        ZipCodeListType z = new ZipCodeListType();
        z.setZipCodeListType(zipcode);

        request.setZipCodeList(z);
        LatLonListZipCodeResponse rs = stub.latLonListZipCode(request);

        HashMap<String, String> map = new HashMap<String, String>();
		InputStream is = new ByteArrayInputStream(rs.getListLatLonOut().getListLatLonType().getBytes());

		try {
			WeatherXMLReader myReader = WeatherXMLReaderFactory.getInstance();
			myReader.parserLatLonListZipCodeResponse(map, is);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Logger.getLogger( e.getMessage());
			}
		}
	   return map;
	}

   
    public static HashMap<String, Object> getForecastWeather(double latitude, double longitude) throws AxisFault, RemoteException {

		NdfdXMLStub stub = new NdfdXMLStub(SEI_URL);
		NDFDgenByDay pa = new NDFDgenByDay();

		pa.setLatitude(new BigDecimal(latitude));
		pa.setLongitude(new BigDecimal(longitude));
		pa.setNumDays(BigInteger.valueOf(2L)); 
		pa.setStartDate(new Date());  

        pa.setFormat(FormatType.value1);
        pa.setUnit(UnitType.e);

		NDFDgenByDayResponse rs = stub.nDFDgenByDay(pa);

		InputStream is = new ByteArrayInputStream(rs.getDwmlByDayOut().getBytes());
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			WeatherXMLReader myReader = WeatherXMLReaderFactory.getInstance();
			myReader.parserNDFDgenByDayResponse(map, is);

		} finally {
			try {
				is.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		return map;
	}




}
