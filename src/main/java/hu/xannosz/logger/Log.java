package hu.xannosz.logger;

import java.io.Serializable;
import java.util.Date;

public class Log implements Serializable {

	private static final long serialVersionUID = 5989401637627507574L;

	public Class<?> clazz;
	public String program;
	public String message;
	public LogLevel level;
	public Date date;

	public Log(LogLevel level, Date date, String message, String program, Class<?> clazz) {
		this.level = level;
		this.message = message;
		this.program = program;
		this.clazz = clazz;
		this.date = date;
	}
}
