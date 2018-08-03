package hu.xannosz.logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
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

	private static void log(LogLevel level, String message, Class<?> clazz) {
		Log log = new Log(level, Calendar.getInstance().getTime(), message, program, clazz);
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

	private void log(LogLevel level, String message, Object... objects) {
		if (objects.length == 0) {
			log(level, guard(message), clazz);
		} else if (objects.length == 1 && message == null) {
			log(level, objectToString(objects[0]), clazz);
		} else {
			log(level, createObjectLog(guard(message), objects), clazz);
		}
	}

	private String objectToString(Object o) {
		if (o instanceof Throwable) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			((Throwable) o).printStackTrace(ps);
			ps.close();
			return baos.toString();
		} else {
			return guard(o);
		}
	}

	private String createObjectLog(String format, Object[] objects) {
		String result = format;
		for (Object o : objects) {
			result = result.replaceFirst("\\{\\}", objectToString(o));
		}
		return result;
	}

	private <T> String guard(T s) {
		if (s == null) {
			return "";
		}
		return s.toString();
	}

	public void error(String s) {
		log(LogLevel.ERROR, s);
	}

	public void error(Object object) {
		log(LogLevel.ERROR, null, object);
	}

	public void error(String s, Object... objects) {
		log(LogLevel.ERROR, s, objects);
	}

	public void warning(String s) {
		log(LogLevel.WARNING, s);
	}

	public void warning(Object object) {
		log(LogLevel.WARNING, null, object);
	}

	public void warning(String s, Object... objects) {
		log(LogLevel.WARNING, s, objects);
	}

	public void info(String s) {
		log(LogLevel.INFO, s);
	}

	public void info(Object object) {
		log(LogLevel.INFO, null, object);
	}

	public void info(String s, Object... objects) {
		log(LogLevel.INFO, s, objects);
	}

	public void debug(String s) {
		log(LogLevel.DEBUG, s);
	}

	public void debug(Object object) {
		log(LogLevel.DEBUG, null, object);
	}

	public void debug(String s, Object... objects) {
		log(LogLevel.DEBUG, s, objects);
	}

	public void trace(String s) {
		log(LogLevel.TRACE, s);
	}

	public void trace(Object object) {
		log(LogLevel.TRACE, null, object);
	}

	public void trace(String s, Object... objects) {
		log(LogLevel.TRACE, s, objects);
	}
}