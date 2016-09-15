Demand Response Automation Server 3.x 
Web Service Client
Release 3.0
14 June 2007

This package contains an example DRAS web service client written in Java. See 
the DRASWS-API-2.0-R2.pdf file for a description of the DRAS web service.

To unpackage, simply unzip the contents somewhere on your file system.

See the PSS2WSClient.java file for details on how the code is structured and 
how to interface the example with an Energy Management and Control System 
(EMCS). 

Many aspects of the application can be configured using a configuration file.  
By default, this file is in the run directory and is named PSS2WSClient.conf. 
Another file can be used by specifying the full path and name on the command 
line. See the PSS2WSClient.conf file or the source code for the available 
options and their defaults. The configuration file being used and the values 
of all the properties are printed to the console during startup.

At a minimum, the username and password properties need to be configured 
correctly in the PSS2WSClient.conf before running. Your username and password 
should have been provided to you when you received this package. The 
endPointHost property might also need to be set. The endPointHost depends on 
the electrical utility servicing the participant’s facility as follows:
- PG&E: pge.openadr.com
- SCE: sce.openadr.com
- SDG&E: sdge.openadr.com

To run the client, execute the run.bat file. 

To build the client from the Java source files, execute Ant in the root 
directory of the package (where the build.xml file resides).


Changes From Version 2.0
------------------------

- updated API doc to 2.0 (support for DRAS 3.x)

- updated wsdl and stubs for DRAS 3.x API

- replaced getPriceSchedule with getPrice() and added isAPEEventPending() to 
pollServer()


Changes From Version 1.0
------------------------

- added check for null PriceSchedule to avoid null pointer exeception in 
getPriceSchedule()

Changes From Version 0.9
------------------------

- Made PSS2WSClient.getPriceSchedule() return a copy rather than a reference to
allow users to manipluate the dates without modifying the original value

- Changed defaults to point to production server

- Changed the timing defaults to poll and print statistics once per minute.

- Changed default logging level to INFO.

- Added authentication

- Added SSL

- Removed test schedule configuraiton option. A PSS2 operator will specifiy the 
channel that a client connects to based upon the username.

