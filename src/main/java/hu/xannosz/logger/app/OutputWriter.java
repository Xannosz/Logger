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
		p.write("</head><body><nav><div class=\"navList\"><button id=\"styleButton\">Select Style</button><ul id=\"styleList\">\n");
		writeCSSSwitcherButtons();
		p.write("</ul></div>");
		writeRowSelector();
		writeColumnSelector();
		p.write("</nav><section><table>\n<tr>"
				+ "<th>Level</th><th>Date</th><th>Time</th><th>MiliSec</th><th> </th><th>Class</th>" + "</tr>\n");
		p.flush();
	}

	private void writeRowSelector() {
		p.write("<div class=\"navList\"><button id=\"rowButton\">Select Level</button><ul id=\"rowList\">\n"
				+ "<li><a onclick=\"setActiveStyleSheet('TRACE'); return false;\">TRACE</a></li>\n"
				+ "<li><a onclick=\"setActiveStyleSheet('DEBUG'); return false;\">DEBUG</a></li>\n"
				+ "<li><a onclick=\"setActiveStyleSheet('INFO'); return false;\">INFO</a></li>\n"
				+ "<li><a onclick=\"setActiveStyleSheet('WARN'); return false;\">WARN</a></li>\n"
				+ "<li><a onclick=\"setActiveStyleSheet('ERROR'); return false;\">ERROR</a></li>\n" + "</ul></div>");
	}

	private void writeColumnSelector() {
		p.write("<div class=\"navList\"><button id=\"columnButton\">Select Column</button><ul id=\"columnList\">\n"
				+ "<li><a onclick=\"setActiveStyleSheet('Level'); return false;\">Level</a></li>\n"
				+ "<li><a onclick=\"setActiveStyleSheet('Date'); return false;\">Date</a></li>\n"
				+ "<li><a onclick=\"setActiveStyleSheet('Time'); return false;\">Time</a></li>\n"
				+ "<li><a onclick=\"setActiveStyleSheet('MiliSec'); return false;\">MiliSec</a></li>\n"
				+ "<li><a onclick=\"setActiveStyleSheet('Log'); return false;\">Log</a></li>\n"
				+ "<li><a onclick=\"setActiveStyleSheet('Class'); return false;\">Class</a></li>\n" + "</ul></div>");
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
				log.message, log.clazz.getSimpleName());
	}

	private void writeTDRow(String level, String date, String time, String milisec, String message, String clazz) {
		p.write("<td class=\"level\">" + level + "</td><td class=\"date\">" + date + "</td><td class=\"time\">" + time
				+ "</td><td class=\"milisec\">" + milisec + "</td><td class=\"message\">" + message
				+ "</td><td class=\"clazz\">" + clazz + "</td>\n");
	}
}