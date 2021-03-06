package ru.sovzond.mgis2.fgistp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Alexander Arakelyan on 05.08.15.
 */
@XmlRootElement
public class ExceptionEntry {
	private String message;

	public ExceptionEntry() {

	}

	public ExceptionEntry(String message) {
		this.message = message;
	}

	@XmlElement(name = "message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
