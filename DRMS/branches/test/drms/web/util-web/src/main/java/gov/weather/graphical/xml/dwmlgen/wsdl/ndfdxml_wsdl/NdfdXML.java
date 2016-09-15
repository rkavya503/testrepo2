

/**
 * NdfdXML.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 */

    package gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl;

    /*
     *  NdfdXML java interface
     */

    public interface NdfdXML {
          

        /**
          * Auto generated method signature
          * Returns a list of latitude and longitude pairs along a line defined by the latitude and longitude of the 2 endpoints
                    * @param latLonListLine0
                
         */

         
                     public gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListLineResponse latLonListLine(

                        gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListLine latLonListLine0)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns a list of latitude and longitude pairs along a line defined by the latitude and longitude of the 2 endpoints
                * @param latLonListLine0
            
          */
        public void startlatLonListLine(

            gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListLine latLonListLine0,

            final gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Returns National Weather Service digital weather forecast data
                    * @param nDFDgen2
                
         */

         
                     public gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenResponse nDFDgen(

                        gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgen nDFDgen2)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns National Weather Service digital weather forecast data
                * @param nDFDgen2
            
          */
        public void startnDFDgen(

            gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgen nDFDgen2,

            final gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Returns a list of latitude and longitude pairs in a rectangular subgrid defined by the lower left and upper right points
                    * @param latLonListSubgrid4
                
         */

         
                     public gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListSubgridResponse latLonListSubgrid(

                        gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListSubgrid latLonListSubgrid4)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns a list of latitude and longitude pairs in a rectangular subgrid defined by the lower left and upper right points
                * @param latLonListSubgrid4
            
          */
        public void startlatLonListSubgrid(

            gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListSubgrid latLonListSubgrid4,

            final gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Returns National Weather Service digital weather forecast data
                    * @param nDFDgenLatLonList6
                
         */

         
                     public gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenLatLonListResponse nDFDgenLatLonList(

                        gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenLatLonList nDFDgenLatLonList6)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns National Weather Service digital weather forecast data
                * @param nDFDgenLatLonList6
            
          */
        public void startnDFDgenLatLonList(

            gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenLatLonList nDFDgenLatLonList6,

            final gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Returns National Weather Service digital weather forecast data summarized over either 24- or 12-hourly periods
                    * @param nDFDgenByDayLatLonList8
                
         */

         
                     public gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenByDayLatLonListResponse nDFDgenByDayLatLonList(

                        gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenByDayLatLonList nDFDgenByDayLatLonList8)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns National Weather Service digital weather forecast data summarized over either 24- or 12-hourly periods
                * @param nDFDgenByDayLatLonList8
            
          */
        public void startnDFDgenByDayLatLonList(

            gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenByDayLatLonList nDFDgenByDayLatLonList8,

            final gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Returns a list of latitude and longitude pairs in a rectangle defined by a central point and distance from that point in the latitudinal and longitudinal directions
                    * @param latLonListSquare10
                
         */

         
                     public gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListSquareResponse latLonListSquare(

                        gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListSquare latLonListSquare10)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns a list of latitude and longitude pairs in a rectangle defined by a central point and distance from that point in the latitudinal and longitudinal directions
                * @param latLonListSquare10
            
          */
        public void startlatLonListSquare(

            gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListSquare latLonListSquare10,

            final gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Returns National Weather Service digital weather forecast data encoded in GML for a single time
                    * @param gmlLatLonList12
                
         */

         
                     public gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.GmlLatLonListResponse gmlLatLonList(

                        gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.GmlLatLonList gmlLatLonList12)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns National Weather Service digital weather forecast data encoded in GML for a single time
                * @param gmlLatLonList12
            
          */
        public void startgmlLatLonList(

            gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.GmlLatLonList gmlLatLonList12,

            final gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Returns National Weather Service digital weather forecast data summarized over either 24- or 12-hourly periods
                    * @param nDFDgenByDay14
                
         */

         
                     public gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenByDayResponse nDFDgenByDay(

                        gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenByDay nDFDgenByDay14)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns National Weather Service digital weather forecast data summarized over either 24- or 12-hourly periods
                * @param nDFDgenByDay14
            
          */
        public void startnDFDgenByDay(

            gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenByDay nDFDgenByDay14,

            final gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Returns National Weather Service digital weather forecast data encoded in GML for a time period
                    * @param gmlTimeSeries16
                
         */

         
                     public gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.GmlTimeSeriesResponse gmlTimeSeries(

                        gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.GmlTimeSeries gmlTimeSeries16)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns National Weather Service digital weather forecast data encoded in GML for a time period
                * @param gmlTimeSeries16
            
          */
        public void startgmlTimeSeries(

            gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.GmlTimeSeries gmlTimeSeries16,

            final gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Returns a list of latitude and longitude pairs with each pair corresponding to an input zip code.
                    * @param latLonListZipCode18
                
         */

         
                     public gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListZipCodeResponse latLonListZipCode(

                        gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListZipCode latLonListZipCode18)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns a list of latitude and longitude pairs with each pair corresponding to an input zip code.
                * @param latLonListZipCode18
            
          */
        public void startlatLonListZipCode(

            gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListZipCode latLonListZipCode18,

            final gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Returns a list of latitude and longitude pairs paired with the city names they correspond to
                    * @param latLonListCityNames20
                
         */

         
                     public gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListCityNamesResponse latLonListCityNames(

                        gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListCityNames latLonListCityNames20)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns a list of latitude and longitude pairs paired with the city names they correspond to
                * @param latLonListCityNames20
            
          */
        public void startlatLonListCityNames(

            gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.LatLonListCityNames latLonListCityNames20,

            final gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Returns four latitude and longitude pairs for corners of an NDFD grid and the minimum resolution that will return the entire grid
                    * @param cornerPoints22
                
         */

         
                     public gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.CornerPointsResponse cornerPoints(

                        gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.CornerPoints cornerPoints22)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns four latitude and longitude pairs for corners of an NDFD grid and the minimum resolution that will return the entire grid
                * @param cornerPoints22
            
          */
        public void startcornerPoints(

            gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.CornerPoints cornerPoints22,

            final gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    