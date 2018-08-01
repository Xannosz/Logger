package hu.xannosz.logger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

public class Logger {

	private static final String LOGGER_JSON = "logger.json";
	private static final String LOGGER_JSON_NOT_FOUND = "The logger.json file not found.";
	private static final String NONAME = "NoName";
	private static List<ObjectOutputStream> out = new ArrayList<>();
	private static List<Pair<String, Integer>> output;
	private static String program = NONAME;

	private Class<?> clazz;

	private Logger() {
	}

	public static Logger getLogger(Class<?> clazz) {
		if (output == null) {
			output = new ArrayList<>();
			readJson();
		}
		Logger logger = new Logger();
		logger.clazz = clazz;
		return logger;
	}

	public static void addServer(String host, int port) {
		output.add(new Pair<>(host, port));
	}

	public static void addProgramName(String name) {
		if (program == NONAME) {
			program = name;
		}
	}

	private static void createConnections() {
		for (int i = 0; i < output.size(); i++) {
			Pair<String, Integer> p = output.get(i);
			try {
				@SuppressWarnings({ "resource" })
				Socket socket = new Socket();
				int tries = 0;
				while (!socket.isConnected() && tries < 10) {
					try {
						tries += 1;
						socket.connect(new InetSocketAddress(p.getValue0(), p.getValue1()));

						out.add(new ObjectOutputStream(socket.getOutputStream()));
					} catch (IOException exp) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
					}
				}
			} finally {
				output.remove(i);
			}
		}
	}

	private static void readJson() {
		JSONObject obj = null;
		try {
			List<String> file = Files.readAllLines(Paths.get(LOGGER_JSON));
			StringBuffer content = new StringBuffer();
			for (String line : file) {
				content.append(line);
			}
			obj = new JSONObject(content.toString());
		} catch (IOException e) {
			System.out.println(LOGGER_JSON_NOT_FOUND);
		}

		if (program == NONAME) {
			try {
				program = obj.getString("programName");
			} catch (Exception e) {
			}
		}

		JSONArray arr;
		try {
			arr = obj.getJSONArray("servers");
		} catch (Exception e) {
			arr = null;
		}

		if (arr != null) {
			for (int i = 0; i < arr.length(); i++) {
				try {
					output.add(
							new Pair<>(arr.getJSONObject(i).getString("server"), arr.getJSONObject(i).getInt("port")));
				} catch (Exception e) {
				}
			}
		} else if (output.isEmpty()) {
			output.add(new Pair<>("127.0.0.1", 1776));
		}
	}

	private static void log(LogLevel level, String reason, String message, Throwable e, Class<?> clazz) {
		Log log = new Log(level, Calendar.getInstance().getTime(), reason, message, e, program, clazz);
		createConnections();
		for (int i = 0; i < out.size(); i++) {
			ObjectOutputStream o = out.get(i);
			try {
				o.writeObject(log);
				o.flush();
			} catch (IOException exp) {
				out.remove(i);
			}
		}
	}

	public void log(LogLevel level, String reason, String message, Throwable e) {
		if (level == null) {
			return;
		}
		log(level, reason, message, e, clazz);
	}

	public void error(String reason, String message, Throwable e) {
		log(LogLevel.ERROR, reason, message, e);
	}

	public void error(String reason, String message) {
		log(LogLevel.ERROR, reason, message, null);
	}

	public void error(String reason) {
		log(LogLevel.ERROR, reason, null, null);
	}

	public void warning(String reason, String message, Throwable e) {
		log(LogLevel.WARNING, reason, message, e);
	}

	public void warning(String reason, String message) {
		log(LogLevel.WARNING, reason, message, null);
	}

	public void warning(String message) {
		log(LogLevel.WARNING, null, message, null);
	}

	public void info(String message) {
		log(LogLevel.INFO, null, message, null);
	}

	public void debug(String message) {
		log(LogLevel.DEBUG, null, message, null);
	}

	public void trace(String message) {
		log(LogLevel.TRACE, null, message, null);
	}
}