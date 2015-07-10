/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chinook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.TooManyListenersException;
import java.util.TreeMap;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

/**
 *
 * @author club
 */
public class SerialCommunication implements SerialPortEventListener {
    static Enumeration portList;
    
    InputStream inputStream;
    OutputStream outputStream;
    SerialPort serialPort;
    
    DataLogger Logger;
    
    ByteBuffer buff = ByteBuffer.allocate(16+1+4*8+1);
    byte[] readBuffer = new byte[1000];
    int bytesRead = 0;
    int readValues = 0;
    int lines = 0;
    
    
    int eventslogged = 0;
    TreeMap<Integer,Integer> map = new TreeMap<>();
    LinkedList<String> valuesString = new LinkedList<>();
    
    boolean SystemReady = false;
    
    public SerialCommunication(CommPortIdentifier portid, DataLogger logger) {
        Logger = logger;
        
       try {
            serialPort = (SerialPort) portid.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {System.out.println(e);}
        try {
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {System.out.println(e);}
	try {
            serialPort.addEventListener(this);
	} catch (TooManyListenersException e) {System.out.println(e);}
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {System.out.println(e);}
    }
    
    public Boolean sendStartCommand()
    {
        return sendString("Is this the real life?");
        
    }
    public Boolean sendString(String message)
    {
        Boolean succes = true;
        try
        {
            outputStream.write(message.getBytes());
        }
        catch(IOException e)
        {
            succes = false;
        }
        return succes;
    }
    
    public void close()
    {
        try {
            inputStream.close();
            outputStream.close();
            serialPort.close();
        } catch (IOException ex) {
            
        }
        
    }

 

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:

            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                byte read[] = new byte[1000];

                try {
                    int numBytes = 0;
                    
                    while (inputStream.available() > 0) {
                        numBytes = inputStream.read(read);
                    }
                    
                    
                   
                    for(int i = 0; i < numBytes; ++i)
                    {
                        readBuffer[i+bytesRead] = read[i];
                        
                        if(read[i] == 0x09) // 0x09 = \t
                        {
                            readValues++;
                        }
                        
                            if(readValues == 10)
                            {
                                String message = new String(readBuffer);
                                StringTokenizer st = new StringTokenizer(message, "\t");
                                st.nextElement();
                                while(st.hasMoreElements())
                                {
                                    String element = st.nextElement().toString();                                    
                                    valuesString.add(element);
                                }
                                
                                if(eventslogged > 0)
                                {
                                    int j = 0;
                                }
                               // System.out.println(valuesString);
                               // System.out.println(bytesRead);
                                writeDemValues();
                                
                                valuesString = new LinkedList<>();
                                eventslogged++;
                                readBuffer = new byte[1000];
                                bytesRead = 0;
                                readValues = 0;
                            }
                        
                    }
                    
                    bytesRead += numBytes;
                                       
                    //System.out.println(new String(readBuffer));
                    //System.out.println(numBytes);                 
                        
                    
                     
                    
                } catch (IOException e) {System.out.println(e);}
                break;
        }
    }
    
    private void writeDemValues()
    {
        try{
        int   Gear              = Integer.parseInt(valuesString.pop());
        float VitVent           = Float.parseFloat(valuesString.pop());
        float DirVent           = Float.parseFloat(valuesString.pop());
        float DirRelativeMat    = Float.parseFloat(valuesString.pop());
        float RPMEolienne       = Float.parseFloat(valuesString.pop());
        float RPMRoue           = Float.parseFloat(valuesString.pop());
        float Pitch             = Float.parseFloat(valuesString.pop());
        float Thrust            = Float.parseFloat(valuesString.pop());
        float Torque            = Float.parseFloat(valuesString.pop());
        float Power             = -(RPMEolienne*Torque)+(VitVent*Thrust);
        
        
        Logger.startTimer();
        Logger.logData(new Data("Gear",             Gear));
        Logger.logData(new Data("VitVent",          VitVent));
        Logger.logData(new Data("DirVent",          DirVent));
        Logger.logData(new Data("DirRelativeMat",   DirRelativeMat));
        Logger.logData(new Data("RPMEolienne",      RPMEolienne));
        Logger.logData(new Data("RPMRoue",          RPMRoue));
        Logger.logData(new Data("Pitch",            Pitch));
        Logger.logData(new Data("Thrust",           Thrust));
        Logger.logData(new Data("Torque",           Torque));
        Logger.logData(new Data("Power",            Power));

        Logger.done();
        System.out.println(lines++);
        }
        catch(Exception e)
        {
            System.out.println(valuesString);
            System.out.println(e);
        }
                        
    }
    
}
