CAISO DRAS Demo
Initiate Event Example
Release 0.3
25 Nov 2008

===================
 0.3 Release Notes:
-------------------

1) Made the  “OPENADR” and “OPENADR EMERGENCY” day-of events meaning that
    their issue date must be the same as their start date.

===================
 0.2 Release Notes:
-------------------

1) Added code showing the creation of events in multiple programs. There are now
    three programs available: “OPENADR”, “OPENADR EMERGENCY”, and “TEST”.

2) Added code showing how to cancel an event using the "modifyDREvent" method.

3) Added code showing basic HTTP authentication. For now, the username and 
    password are hard coded to the same value: "utilopws".
   
 4) Added an example showing how to use the "getPrograms" method which can be
     be used to determine if the server is up and running.
   
===================
 0.1 Release Notes:
-------------------

1) To run, execute "run.bat"

2) Java 6 is required to run this code

3) A build.xml is included for source compilation, but you will have to setup the ant environment to use it.