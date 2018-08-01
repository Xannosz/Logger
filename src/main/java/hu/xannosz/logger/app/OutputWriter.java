package hu.xannosz.logger.app;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import hu.xannosz.logger.Log;

public class OutputWriter {

	private PrintWriter p;
	private List<File> cssS;

	public OutputWriter(String path) {
		try {
			cssS = Arrays.asList((new File("css")).listFiles());
			(new File(path)).mkdirs();
			FileUtils.copyDirectory(new File("css"), new File(path + "/css"));
			FileUtils.copyDirectory(new File("js"), new File(path + "/js"));
			(new File(path + "/index.html")).createNewFile();
			p = new PrintWriter(path + "/index.html", "UTF-8");
		} catch (IOException e) {
			e.printStackTrace(); // TODO
		}
		writeHeader();
	}

	public void addLog(Log log) {
		writeLog(log);
		p.flush();
	}

	public void close() {
		writeFooter();
	}

	private void writeHeader() {
		p.write("<!DOCTYPE html><html lang=\"hu\"><head><title>Xannosz Logger</title><meta charset=\"utf-8\"><script type=\"text/javascript\" src=\"js/styleswitcher.js\"></script>\n");
		writeCSSs();
		p.write("</head><body><nav><ul id=\"style\">");
		writeCSSSwitcherButtons();
		p.write("</ul>" + "</nav><section><table><tr>"
				+ "<th>Level</th><th>Time</th><th>Reason</th><th>Message</th><th>Throwable</th><th>Class</th>"
				+ "</tr>\n");
		p.flush();
	}

	private void writeCSSSwitcherButtons() {
		for (File f : cssS) {
			writeCSSSwitcherButton(f.getName());
		}
	}

	private void writeCSSSwitcherButton(String file) {
		p.write("<li><a onclick=\"setActiveStyleSheet('" + file + "'); return false;\"> " + file + "</a></li>\n");

	}

	private void writeCSSs() {
		for (File f : cssS) {
			writeCSS(f.getName());
		}
	}

	private void writeCSS(String file) {
		p.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/" + file + "\" media=\"screen\" title=\"" + file
				+ "\">\n");
	}

	private void writeFooter() {
		p.write("</table></section></body></html>");
		p.flush();
		p.close();
	}

	private void writeLog(Log log) {
		switch (log.level) {
		case TRACE:
			writeTrace(log);
			break;
		case DEBUG:
			writeDebug(log);
			break;
		case INFO:
			writeInfo(log);
			break;
		case WARNING:
			writeWarning(log);
			break;
		case ERROR:
			writeError(log);
			break;
		}
	}

	private void writeTrace(Log log) {
		p.write("<tr class=\"trace\">");
		writeTDRowFromLog("TRACE", log);
		p.write("</tr>");
	}

	private void writeDebug(Log log) {
		p.write("<tr class=\"debug\">");
		writeTDRowFromLog("DEBUG", log);
		p.write("</tr>");
	}

	private void writeInfo(Log log) {
		p.write("<tr class=\"info\">");
		writeTDRowFromLog("INFO", log);
		p.write("</tr>");
	}

	private void writeWarning(Log log) {
		p.write("<tr class=\"warning\">");
		writeTDRowFromLog("WARN", log);
		p.write("</tr>");
	}

	private void writeError(Log log) {
		p.write("<tr class=\"error\">");
		writeTDRowFromLog("ERROR", log);
		p.write("</tr>");
	}

	private void writeTDRowFromLog(String level, Log log) {
		writeTDRow(level, log.date != null ? (new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")).format(log.date) : "",
				guard(log.reason), guard(log.message),
				log.e != null ? listToString(Arrays.asList(log.e.getStackTrace())) : "", log.clazz.getSimpleName());
	}

	private String guard(String s) {
		if (s == null || s.isEmpty()) {
			return "";
		}
		return s;
	}

	private String listToString(List<StackTraceElement> list) {
		StringBuilder builder = new StringBuilder();
		for (StackTraceElement elem : list) {
			builder.append(elem.toString());
			builder.append("\n");
		}
		return builder.toString();
	}

	private void writeTDRow(String level, String time, String reason, String message, String throwable, String clazz) {
		p.write("<td class=\"level\">" + level + "</td><td class=\"time\">" + time + "</td><td class=\"reason\">"
				+ reason + "</td><td class=\"message\">" + message + "</td><td class=\"throwable\">" + throwable
				+ "</td><td class=\"clazz\">" + clazz + "</td>\n");
	}
}