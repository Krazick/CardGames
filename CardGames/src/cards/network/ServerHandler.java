package cards.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ServerHandler implements Runnable {
    private final static int DefaultTimeout = 2000;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private boolean continueRunning;
	protected String name;
	private String host;
	private int port;
	private Logger logger;
	private NetworkGameSupport gameManager;
	
	public ServerHandler (Socket aServerSocket, NetworkGameSupport aGameManager) {
		setValues (aServerSocket);
		gameManager = aGameManager;
		setupLogger ();
	}
	
	private void setupLogger () {
		String tXMLBaseDir;
		
		tXMLBaseDir = gameManager.getXMLBaseDirectory ();
		System.setProperty ("log4j.configurationFile", tXMLBaseDir + "log4j2.xml");
		logger = LogManager.getLogger (ServerHandler.class);
		logger.info ("Logger setup in Server Handler");
	}

	public ServerHandler (String aHost, int aPort, NetworkGameSupport aGameManager) throws ConnectException {
		boolean tContinueRunning = false;
		
		gameManager = aGameManager;
		setupLogger ();
		try {
			setHost (aHost);
			setPort (aPort);
            establishSocketConnection ();
			tContinueRunning = true;
		} catch (UnknownHostException tException) {
			log ("Unkown Host Exception thrown when creating Socket to Server", tException);
		} catch (ConnectException tException) {
			throw tException;
		} catch (SocketTimeoutException tException) {
			log ("Connect SocketTimeoutException thrown when trying to Connect to Server", tException);
		} catch (IOException tException) {
			log ("IOException thrown when creating Socket to Server", tException);
		}
		
		setContinueRunning (tContinueRunning);
	}

	private void establishSocketConnection ()
			throws UnknownHostException, IOException, SocketException {
		InetAddress tIPAddress;
		
        Socket tSocket = new Socket ();
        tIPAddress = InetAddress.getByName (host);
        logger.info ("Attempting Socket Connection to Host " + host + " using IP " + tIPAddress + " Port " + port);
		tSocket.connect (new InetSocketAddress (tIPAddress, port), DefaultTimeout);
        logger.info ("Socket Connection Established");
		tSocket.setKeepAlive (true);
		setValues (tSocket);
	}

	protected void setContinueRunning (boolean aContinueRunning) {
		continueRunning = aContinueRunning;
	}

	private void setHost (String aHost) {
		host = aHost;
	}
	
	private void setPort (int aPort) {
		port = aPort;
	}

	private void setValues (Socket aServerSocket) {
		socket = aServerSocket;
		setupBufferedReader ();
		setupPrintWriter ();
	}
	
	public void setName (String aName) {
		name = aName;
	}
	
	private void setupBufferedReader () {
		try {
			in = new BufferedReader (new InputStreamReader (socket.getInputStream ()));
		} catch (IOException tException) {
			log ("IOException thrown when seting up Buffered Reader", tException);
		}		
	}
	
	private void setupPrintWriter () {
		try {
			out = new PrintWriter (socket.getOutputStream (), true);
		} catch (IOException tException) {
			log ("IOException thrown when seting up PrintWriter", tException);
		}		
	}
	
	@Override
	public void run () {
		String tString;

		if (in != null) {
			try {
				while (continueRunning) {
					tString = in.readLine ();
//					System.out.println ("RAW Line: | " + tString + " |");
					if (tString == null) {
						setContinueRunning (false);
					} else if (tString.startsWith ("[") && tString.endsWith ("]")) {
						handleServerCommands (tString);
					} else {
						handleServerMessage (tString);
					}
				}
			} catch (IOException tException) {
				log ("Exception thrown when Reading from Server", tException);
			} finally {
				closeAll ();
			}		
		}
	}
	
	protected abstract void startHeartbeat ();
	
	protected boolean tryReConnect () {
		boolean tContinue;
		
		try {
			Thread.sleep (10000);
			logger.warn ("Attempting once to Reconnect to the Server");
			socket = null;
			System.gc ();
			establishSocketConnection ();
			logger.warn ("Success on creating a new Socket to the Server");
			handleChatReconnect ();
			tContinue = true;	
		} catch (Exception tException) {
			String message = "ReConnect not successful " + tException.getMessage ();
			log (message, tException);
			tContinue = false;	
		}
		
		return tContinue;
	}

	public boolean isConnected () {
		return continueRunning;
	}
	
	// Abstract Classes to handle Content from the Server -- 

	protected abstract void handleServerMessage (String tString);

	protected abstract void handleServerCommands (String aString);
	
	// Generate Client Requests to the Server ---
	
	public void println (String tString) {
		if (out != null) {
			tString = tString.replaceAll ("\r", "").replaceAll ("\n", "");
			out.println (tString);
		}
	}

	// --- End of Client Requests to the Server
	
    private void log (String aMessage, Exception aException) {
		System.err.println (aMessage);
		aException.printStackTrace ();
    }

	public void closeAll () {
		continueRunning = false;
		if (in != null) {
			try {
				in.close ();
			} catch (IOException tException) {
				log ("IOException thrown when Closing the Server InputStreamReader", tException);
			}
		}
		if (out != null) {
			out.close ();
			try {
				socket.close ();
			} catch (IOException tException) {
				log ("IOException thrown when Closing Socket", tException);
			}		
		}
	}

	public void shutdown () {
		// Report to Server this Client is stopping
		println ("stop");
		closeAll ();
	}
	
	protected abstract void handleChatReconnect ();

	protected abstract boolean sendGameSupport (String aRequest);

	protected abstract String buildGameSupportXML (String tGameID, String string);
}
