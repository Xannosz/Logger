package hu.xannosz.logger.app;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import hu.xannosz.logger.Log;

public class LogReader extends Thread {

	private Socket socket;
	private ObjectInputStream in;
	private OutputWriter writer;
	private String outputFolder;

	public LogReader(Socket socket, String outputFolder) {
		this.socket = socket;
		this.outputFolder = outputFolder;
	}

	public void run() {
		try {
			in = new ObjectInputStream(socket.getInputStream());

			Log log = (Log) in.readObject();

			writer = new OutputWriter(outputFolder + "/" + log.program + "/"
					+ (new SimpleDateFormat("yyyyMMdd_HHmmss_SSS")).format(Calendar.getInstance().getTime()));

			writer.addLog(log);
			
			while (true) {
				log = (Log) in.readObject();
				if (log == null) {
					break;
				}
				writer.addLog(log);
			}
		} catch (Exception e) {
		} finally {
			try {
				writer.close();
				socket.close();
			} catch (Exception e) {
			}
		}
	}
}