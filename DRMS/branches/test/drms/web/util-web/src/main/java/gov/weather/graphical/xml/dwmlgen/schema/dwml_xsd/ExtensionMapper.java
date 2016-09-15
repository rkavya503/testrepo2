
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:19:26 CET)
 */

        
            package gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd;
        
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd".equals(namespaceURI) &&
                  "formatType".equals(typeName)){
                   
                            return  gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.FormatType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd".equals(namespaceURI) &&
                  "listLatLonType".equals(typeName)){
                   
                            return  gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.ListLatLonType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd".equals(namespaceURI) &&
                  "unitType".equals(typeName)){
                   
                            return  gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.UnitType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd".equals(namespaceURI) &&
                  "weatherParametersType".equals(typeName)){
                   
                            return  gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.WeatherParametersType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd".equals(namespaceURI) &&
                  "featureTypeType".equals(typeName)){
                   
                            return  gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.FeatureTypeType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd".equals(namespaceURI) &&
                  "displayLevelType".equals(typeName)){
                   
                            return  gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.DisplayLevelType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd".equals(namespaceURI) &&
                  "sectorType".equals(typeName)){
                   
                            return  gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.SectorType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd".equals(namespaceURI) &&
                  "listCityNamesType".equals(typeName)){
                   
                            return  gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.ListCityNamesType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd".equals(namespaceURI) &&
                  "compTypeType".equals(typeName)){
                   
                            return  gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.CompTypeType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd".equals(namespaceURI) &&
                  "productType".equals(typeName)){
                   
                            return  gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.ProductType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd".equals(namespaceURI) &&
                  "zipCodeListType".equals(typeName)){
                   
                            return  gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.ZipCodeListType.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    