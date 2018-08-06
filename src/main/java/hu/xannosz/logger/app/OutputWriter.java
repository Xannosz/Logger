package hu.xannosz.logger.app;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import hu.xannosz.microtools.logger.Log;

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
		p.write("<!DOCTYPE html><html lang=\"hu\"><head><title>Xannosz Logger</title><meta charset=\"utf-8\">\n");
		p.write("<script type=\"text/javascript\" src=\"js/styleswitcher.js\"></script>\n");
		p.write("<script type=\"text/javascript\"src=\"js/scroll.js\"></script>\n");
		writeCSSs();
		p.write("</head><body><nav><div class=\"navList\"><button id=\"styleButton\">Select Style</button><ul id=\"styleList\">\n");
		writeCSSSwitcherButtons();
		p.write("</ul></div>");
		writeRowSelector();
		writeColumnSelector();
		p.write("</nav><section><button onclick=\"topFunction()\" id=\"topButton\">Top</button><table>\n<tr>"
				+ "<th class=\"level\">Level</th><th class=\"date\">Date</th><th class=\"time\">Time</th><th class=\"millisec\">MilliSec</th><th class=\"message\">  </th><th class=\"clazz\">Class</th>"
				+ "</tr>\n");
		p.flush();
	}

	private void writeRowSelector() {
		p.write("<div class=\"navList\"><button id=\"rowButton\">Select Level</button><ul id=\"rowList\">\n"
				+ "<li><a class=\"trace\" onclick=\"setActiveSelector('trace'); return false;\">TRACE</a></li>\n"
				+ "<li><a class=\"debug\" onclick=\"setActiveSelector('debug'); return false;\">DEBUG</a></li>\n"
				+ "<li><a class=\"info\" onclick=\"setActiveSelector('info'); return false;\">INFO</a></li>\n"
				+ "<li><a class=\"warning\" onclick=\"setActiveSelector('warning'); return false;\">WARN</a></li>\n"
				+ "<li><a class=\"error\" onclick=\"setActiveSelector('error'); return false;\">ERROR</a></li>\n"
				+ "</ul></div>");
	}

	private void writeColumnSelector() {
		p.write("<div class=\"navList\"><button id=\"columnButton\">Select Column</button><ul id=\"columnList\">\n"
				+ "<li><a class=\"level\" onclick=\"setActiveSelector('level'); return false;\">Level</a></li>\n"
				+ "<li><a class=\"date\" onclick=\"setActiveSelector('date'); return false;\">Date</a></li>\n"
				+ "<li><a class=\"time\" onclick=\"setActiveSelector('time'); return false;\">Time</a></li>\n"
				+ "<li><a class=\"millisec\" onclick=\"setActiveSelector('millisec'); return false;\">MilliSec</a></li>\n"
				+ "<li><a class=\"message\" onclick=\"setActiveSelector('message'); return false;\">Log</a></li>\n"
				+ "<li><a class=\"clazz\" onclick=\"setActiveSelector('clazz'); return false;\">Class</a></li>\n"
				+ "</ul></div>");
	}

	private void writeCSSSwitcherButtons() {
		for (File f : cssS) {
			if (!f.getName().equals("fonts")) {
				writeCSSSwitcherButton(f.getName());
			}
		}
	}

	private void writeCSSSwitcherButton(String file) {
		p.write("<li><a onclick=\"setActiveStyleSheet('" + file + "'); return false;\"> " + makeName(file)
				+ "</a></li>\n");
	}

	private String makeName(String file) {
		return file.replaceAll(".css", "").replaceAll("_", " ");
	}

	private void writeCSSs() {
		for (File f : cssS) {
			if (!f.getName().equals("fonts")) {
				writeCSS(f.getName());
			}
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
		writeTDRow(level, (new SimpleDateFormat("yyyy.MM.dd")).format(log.date),
				(new SimpleDateFormat("hh:mm:ss")).format(log.date), (new SimpleDateFormat("SSS")).format(log.date),
				log.message, log.clazz);
	}

	private void writeTDRow(String level, String date, String time, String millisec, String message, String clazz) {
		p.write("<td class=\"level\">" + level + "</td><td class=\"date\">" + date + "</td><td class=\"time\">" + time
				+ "</td><td class=\"millisec\">" + millisec + "</td><td class=\"message\">" + message
				+ "</td><td class=\"clazz\">" + clazz + "</td>\n");
	}
}