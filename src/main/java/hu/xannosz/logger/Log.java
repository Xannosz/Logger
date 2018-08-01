package hu.xannosz.logger;

import java.io.Serializable;
import java.util.Date;

public class Log implements Serializable {

	private static final long serialVersionUID = 5989401637627507574L;

	public Class<?> clazz;
	public String program;
	public Throwable e;
	public String message;
	public String reason;
	public LogLevel level;
	public Date date;

	public Log(LogLevel level, Date date, String reason, String message, Throwable e, String program, Class<?> clazz) {
		this.level = level;
		this.reason = reason;
		this.message = message;
		this.e = e;
		this.program = program;
		this.clazz = clazz;
		this.date = date;
	}
}
