package de.hems.arduinocnc.arduino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;

public class SerialWrapper implements SerialPortEventListener {
    SerialPort serialPort;
	/** Buffered input stream from the port */
	private InputStream input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT	= 2000;
	/** Default bits per second for COM port. */
	private static final int BAUD_RATE	= 9600;
	
	public Enumeration<CommPortIdentifier> listPorts() {
		return this.listPorts(false);
	}
	
	public Enumeration<CommPortIdentifier> listPorts(boolean info) {
		PrintStream tmp = System.out;
		if(!info) {
			System.setOut(new PrintStream(new OutputStream() {
				@Override
				public void write(int b) throws IOException {
					//do nothing here...
				}
			}));
		}
		
		Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();
		
		if(!info) {
			System.setOut(tmp);
		} else {
			System.out.println("=========================================");
			System.out.println();
		}
		
		if(!ports.hasMoreElements()) {
			System.err.println("No ports available!");
		}
		
		return ports;
	}
	
	/**
	 * Try to open connection to specified COM port
	 * @param port
	 * @return void
	 */
	public synchronized boolean open(CommPortIdentifier port) {
		if(port == null) {
	        System.err.println("Invalid port given");
	        return false;
	    }
		
		if(port.isCurrentlyOwned()) {
			System.err.println("Port is currently owned by " + port.getCurrentOwner());
			return false;
		}
		
		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) port.open(
					this.getClass().getName(),
			        TIME_OUT
			);
			
			// set port parameters
			serialPort.setSerialPortParams(
				BAUD_RATE,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE
			);
			
			// open the streams
			input	= serialPort.getInputStream();
			output	= serialPort.getOutputStream();
			
			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			
			return true;
	    } catch (Exception e) {
	        System.err.println(e.toString());
	        return false;
	    }
	}
	
	/**
	* This should be called when you stop using the port.
	* This will prevent port locking on platforms like Linux.
	*/
	public synchronized boolean close() {
	    if (serialPort != null) {
	        serialPort.removeEventListener();
	        serialPort.close();
	        
	        //System.out.println("Closed COM Port: " + serialPort.getName());
	        return true;
	    } else {
	    	return false;
	    }
	}
	
	/**
	* Handle an event on the serial port. Read the data and print it.
	*/
	public synchronized void serialEvent(SerialPortEvent oEvent) {
	    if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
	        try {
	            int available = input.available();
	            byte chunk[] = new byte[available];
	            input.read(chunk, 0, available);
	
	            // Displayed results are codepage dependent
	            System.out.print(">>> Serial INPUT: ");
	            System.out.print(new String(chunk) + "\n");
	        } catch (Exception e) {
	            System.err.println(e.toString());
	        }
	    }
	    // Ignore all the other eventTypes, but you should consider the other ones.
	}
	
	public synchronized void write(String s) throws IOException {
		this.output.write(s.getBytes());
	}
	
	public synchronized void write(char c) throws IOException {
		this.output.write((byte) c);
	}
	
	public synchronized void write(byte b) throws IOException {
		this.output.write(b);
	}
	
	public synchronized void writeln(String s) throws IOException {
		s += "\n";
		this.output.write(s.getBytes());
	}
	
	public synchronized void writeln(char c) throws IOException {
		this.output.write((c + "\n").getBytes());
	}
	
	public synchronized void writeln(byte b) throws IOException {
		this.output.write(((char) b + "\n").getBytes());
	}
}