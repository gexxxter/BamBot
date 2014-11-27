package tsBot;

import java.sql.Timestamp;
import java.util.Date;

public class LogEntry {
	private String message;
	private Timestamp timestamp;
	private String userId;
	private String userName;

	public LogEntry() {
		timestamp = createTimestamp();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private Timestamp createTimestamp() {
		Date date = new Date();
		return new Timestamp(date.getTime());

	}
}
