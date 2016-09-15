/**
 * Copyright 2010 - All Rights Reserved.
 */
package com.akuacom.pss2.obix.simulator;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import obix.Abstime;
import obix.Obj;
import obix.Real;
import obix.Uri;
import obix.io.ObixEncoder;
import obix.net.ObixSession;

/**
 * - Insert description here.
 * 
 * @author e333812
 * @creation Jun 30, 2010
 * 
 */
public class Simulator
{
    private long updateInterval = 15 * 60 * 1000;

    private long historyInterval = 24 * 60 * 60 * 1000;

    private boolean stopped = true;

    java.util.List<String> names = new java.util.ArrayList<String>();

    String baseUri = "";

    ObixSession session;

    Obj usage;

    Obj hisUsage;

    Timer timer;

    Map<String, java.util.List<Obj>> hisCache = new HashMap<String, java.util.List<Obj>>();

    public Simulator(java.util.List<String> names, String baseUri, ObixSession session)
    {
        super();
        this.names = names;
        this.baseUri = baseUri;
        this.session = session;

        usage = new Obj("usage");
        usage.setHref(new Uri(baseUri));

        hisUsage = new Obj("usage");
        hisUsage.setHref(new Uri(baseUri));
    }

    public void addMeter(String meter)
    {
        names.add(meter);
    }

    public void removeMeter(String meter)
    {
        names.remove(meter);
    }

    public void start()
    {
        hisCache.clear();

        for (String name : names)
        {
            java.util.List<Obj> list = new java.util.ArrayList<Obj>();
            hisCache.put(name, list);
        }

        generateData(1, names);
        stopped = false;

        new Thread(new Runnable()
        {
            public void run()
            {
                int value = 1;

                while (!stopped)
                {
                    value += 1;

                    try
                    {
                        Thread.sleep(updateInterval);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    System.out.println();
                    generateData(value, names);
                }
            }
        }).start();
    }

    //
    public void generateData(int value, java.util.List<String> nameList)
    {
        try
        {
            for (int i = 0; i < nameList.size(); i++)
            {
                obix.List meter = new obix.List(nameList.get(i));
                Obj obj = new Obj("point" + hisCache.get(nameList.get(i)).size());

                Real real = new Real("value");
                real.setReal(value);

                long currenttime=System.currentTimeMillis();
//                long currenttime= Long.parseLong("1279702004000");
                System.out.println(currenttime);
                Abstime at = new Abstime("time", currenttime);
//                Abstime at = new Abstime("time", System.currentTimeMillis());
                at.setTz("Asia/Beijing");

                obj.add(real);
                obj.add(at);

                meter.add(obj);
                usage.add(meter);

                String xml = ObixEncoder.toString(usage);
                System.out.println(xml);
                Obj response = session.write(usage);

                System.out.println("response:");
                xml = ObixEncoder.toString(response);
                System.out.println(xml);

                if (!response.getStatus().equals(obix.Status.ok))
                    hisCache.get(nameList.get(i)).add(obj);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            Obj[] kids = usage.list();
            int count = kids.length;
            for (int i = 0; i < count; i++)
            {
                usage.remove(kids[i]);
            }

        }
    }

    public void setTimer(long delay)
    {
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                sendHistory(names);
            }

        };

        timer = new Timer("history");
        timer.schedule(task, delay, this.getHistoryInterval());
    }

    public void sendHistory(java.util.List<String> nameList)
    {
        try
        {
            for (int i = 0; i < nameList.size(); i++)
            {
                obix.List meter;
                if (hisUsage.get(nameList.get(i))==null) {
//                    meter = new obix.List(nameList.get(i));
                    hisUsage.add(new obix.List(nameList.get(i)));
                }
//                else
//                    meter = (obix.List)hisUsage.get(nameList.get(i));
                Obj[] objs = new Obj[hisCache.get(nameList.get(i)).size()];
                for (Obj obj : hisCache.get(nameList.get(i)).toArray(objs))
                {
                    obj.removeThis();
                    ((obix.List)hisUsage.get(nameList.get(i))).add(obj);
                }
//                if (meter.list() != null && 
//                        meter.list().length != 0 && hisUsage.get(nameList.get(i))==null)
//                {
//                    hisUsage.add(meter);
//                }

            }
            if (hisUsage.list() != null && hisUsage.list().length != 0)
            {
                System.out.println("history:");
                String xml = ObixEncoder.toString(hisUsage);
                System.out.println(xml);
                Obj obj = session.write(hisUsage);
                
                System.out.println("history response:");
                xml = ObixEncoder.toString(obj);
                System.out.println(xml);
                
                if (obj.getStatus().equals(obix.Status.ok)){
                    Obj[] kids = hisUsage.list();
                    int count = kids.length;
                    for (int i = 0; i < count; i++)
                    {
                        hisUsage.remove(kids[i]);
                    }
        
                    for (String name : nameList)
                    {
                        hisCache.get(name).clear();
                    }                    
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Simulator simulator = null;
        ObixSession session = null;
        try
        {
            
        	
            System.setProperty("javax.net.ssl.trustStore", "./cacerts.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "epriceLBL");
            
            Uri lobbyUri = new Uri("https://localhost:8443/obixserver/obix/dataService/");
            String user = "obix.meter1";
            String pwd = "Test_1234";

            session = new ObixSession(lobbyUri, user, pwd);

            java.util.ArrayList<String> names = new java.util.ArrayList<String>();
            names.add("meter1");
            // names.add("meter2");

            String baseUri = "https://localhost:8443/obixserver/obix/dataService/";

            simulator = new Simulator(names, baseUri, session);
            // for test: 10 seconds
            simulator.setUpdateInterval(60000);

            simulator.start();

            // Calendar calendar = Calendar.getInstance();
            // int year = calendar.get(Calendar.YEAR);
            // int month = calendar.get(Calendar.MONTH)+1;
            // int day = calendar.get(Calendar.DAY_OF_MONTH);
            // calendar.set(year, month, day, 15, 49, 00);
            // Date date = calendar.getTime();

            simulator.setHistoryInterval(120000);
            simulator.setTimer(0);

            System.in.read();

        }
        catch (Exception e)
        {

            e.printStackTrace();
        }
        finally
        {
            if (session != null)
                session.close();
            session = null;
            System.out.println("session closed");

            simulator.stop();
            simulator.close();
        }

    }

    public void stop()
    {
        stopped = true;
    }

    public void close()
    {
        if (timer != null)
            timer.cancel();
        timer = null;
    }

    public long getUpdateInterval()
    {
        return updateInterval;
    }

    public void setUpdateInterval(long updateInterval)
    {
        this.updateInterval = updateInterval;
    }

    public long getHistoryInterval()
    {
        return historyInterval;
    }

    public void setHistoryInterval(long historyInterval)
    {
        this.historyInterval = historyInterval;
    }

}
