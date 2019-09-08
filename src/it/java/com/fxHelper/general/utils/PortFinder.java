package com.fxHelper.general.utils;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Random;

import com.fxHelper.general.common.Logs;
import com.fxHelper.general.common.ReadProperties;
import com.fxHelper.general.common.UtilityMethods;

/**
 * Finds an available port on localhost.
 */
public class PortFinder {

	// the ports below 1024 are system ports
	private static  int MIN_PORT_NUMBER ;

	// the ports above 49151 are dynamic and/or private
	private static  int MAX_PORT_NUMBER ;

	/**
	 * Finds a free port between {@link #MIN_PORT_NUMBER} and
	 * {@link #MAX_PORT_NUMBER}.
	 *
	 * @return a free port
	 * @throws Throwable 
	 * @throw RuntimeException if a port could not be found
	 */
	public synchronized static int findFreePort() {
		//Reading ports range from config file
		try {
			MIN_PORT_NUMBER =Integer.parseInt(new ReadProperties().getProperty("browserMobPortMin"));
			MAX_PORT_NUMBER =Integer.parseInt(new ReadProperties().getProperty("browserMobPortMax"));
		} catch (Throwable e) {
			Logs.warn(UtilityMethods.getException(e));

		}
		
		for (int i = MIN_PORT_NUMBER; i <= MAX_PORT_NUMBER; i++) {
			if (available(i)) {
				System.out.println("Avaiable Port: " + i);
				return i;
			}
		}
		throw new RuntimeException(
				"Could not find an available port between " + MIN_PORT_NUMBER + " and " + MAX_PORT_NUMBER);
	}

	/**
	 * Returns true if the specified port is available on this host.
	 *
	 * @param port
	 *            the port to check
	 * @return true if the port is available, false otherwise
	 */
	private static boolean available(final int port) {
		System.out.println("Getting Available port ...");
		ServerSocket serverSocket = null;
		DatagramSocket dataSocket = null;
		try {
			try {
				Random rand = new Random(); 
		        // Generate random integers in range 0 to 999
		        int rand_int1 = rand.nextInt(10);
				Thread.sleep((rand_int1+1)*1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);
			dataSocket = new DatagramSocket(port);
			dataSocket.setReuseAddress(true);
			return true;
		} catch (final IOException e) {
			return false;
		} finally {
			if (dataSocket != null) {
				dataSocket.close();
			}
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (final IOException e) {
				}
			}
		}
	}

	public static void main(String[] args) throws UnknownHostException {
		System.out.println(findFreePort());

	}
}
