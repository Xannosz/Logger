package hu.xannosz.logger.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class LogManager {

	private static final String LOG_MANAGER_JSON = "logManager.json";
	private static final String THE_LOG_MANAGER_JSON_FILE_NOT_FOUND = "The " + LOG_MANAGER_JSON + " file not found.";

	public static void main(String[] args) {
		JSONObject obj = null;

		String outputFolder;
		int port;

		try {
			List<String> file = Files.readAllLines(Paths.get(LOG_MANAGER_JSON));
			StringBuffer content = new StringBuffer();
			for (String line : file) {
				content.append(line);
			}
			obj = new JSONObject(content.toString());
		} catch (IOException e) {
			System.out.println(THE_LOG_MANAGER_JSON_FILE_NOT_FOUND);
		}

		try {
			port = obj.getInt("port");
		} catch (JSONException e) {
			port = 1776;
		}

		try {
			outputFolder = obj.getString("outputFolder");
		} catch (JSONException e) {
			outputFolder = "/tmp";
		}

		try {
			ServerSocket listener = new ServerSocket(port);
			try {
				while (true) {
					new LogReader(listener.accept(), outputFolder).start();
				}
			} finally {
				listener.close();
			}
		} catch (IOException e) {
		}
	}
}