package com.db.dbx.i18n;

import com.db.dbx.enums.StatusCode;

public final class Message {
	
	private final StatusCode statusCode;
	
	private final String text;

	/**
	 * Creates a new Message of a certain type consisting of the text provided.
	 */
	public Message(StatusCode statusCode, String text) {
		this.statusCode = statusCode;
		this.text = text;
	}

	/**
	 * The type of message; such as info, warning, error, or success.
	 */
	public StatusCode getStatus() {
		return statusCode;
	}

	/**
	 * The info text.
	 */
	public String getText() {
		return text;
	}
	
	public String toString() {
		return statusCode + ": " + text;
	}

}
