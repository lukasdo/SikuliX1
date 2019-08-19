/*
 * Copyright (c) 2010-2019, sikuli.org, sikulix.com - MIT license
 */
package py4Java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Utility class used to perform network operations.
 * </p>
 *
 * @author Barthelemy Dagenais
 *
 */
public class NetworkUtil {

	private final static Logger logger = Logger.getLogger(NetworkUtil.class.getName());

	/**
	 *
	 * @param reader
	 * @param addSpace
	 * @return A non-null String with an optional space if it is empty.
	 * @throws IOException
	 */
	public static String safeReadLine(BufferedReader reader, boolean addSpace) throws IOException {
		String line = reader.readLine();
		if (line == null || (line.length() == 0 && addSpace)) {
			if (addSpace) {
				line = " ";
			} else {
				line = "";
			}
		}

		return line;
	}

	/**
	 *
	 * @param reader
	 * @return A String of at least one character (space if null or empty).
	 * @throws IOException
	 */
	public static String safeReadLine(BufferedReader reader) throws IOException {
		return safeReadLine(reader, true);
	}

	public static void quietlyClose(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (Exception e) {
			logger.log(Level.FINE, "Closeable cannot be closed.", e);
		}
	}

	public static void quietlyClose(ServerSocket closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (Exception e) {
			logger.log(Level.FINE, "Socket cannot be closed.", e);
		}
	}

	public static void quietlyClose(Socket closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (Exception e) {
			logger.log(Level.FINE, "Socket cannot be closed.", e);
		}
	}

	/**
	 * <p>Will send a RST packet on close, which should make both remote
	 * write and read operations fail.</p>
	 *
	 * @param socket
	 */
	public static void quietlySetLinger(Socket socket) {
		try {
			socket.setSoLinger(true, 0);
		} catch (Exception e) {
			logger.log(Level.FINE, "Cannot set linger on socket.", e);
		}
	}

	/**
	 * <p>Performs authentication on the reader / writer representing a
	 * connection to a server.</p>
	 *
	 * <p>To be reusable, this function performs the read and write through raw sockets,
	 * and inspects the output immediately. It is essential that we do not try to evaluate
	 * the output or we could end up executing a non-authenticated method or raising an
	 * unexpected exception.</p>
	 *
	 * @param reader Reader connected to the remote endpoint.
	 * @param writer Writer connected to the remote endpoint.
	 * @param authToken The auth token.
	 * @throws IOException On I/O error, or if authentication fails.
	 */
	static void authToServer(BufferedReader reader, BufferedWriter writer, String authToken) throws IOException {
		writer.write(Protocol.getAuthCommand(authToken));
		writer.flush();

		String returnCommand = reader.readLine();
		if (returnCommand == null || !returnCommand.equals(Protocol.getOutputVoidCommand().trim())) {
			logger.log(Level.SEVERE, "Could not authenticate connection. Received this response: " + returnCommand);
			throw new IOException("Authentication with callback server unsuccessful.");
		}
	}

}
