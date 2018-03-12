package com.ovt.sale.fcst.exception;

@SuppressWarnings("serial")
public class GettingTypeException extends FileTypeException {

	public GettingTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public GettingTypeException(String message) {
		super(message);
	}

}
