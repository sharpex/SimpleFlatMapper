package org.sfm.map;

public interface RowHandlerErrorHandler {
	/**
	 * callback method when an exception is thrown from the handler.
	 * @param error the exception
	 * @param target the object that was passed to the row handler
	 */
	void handlerError(Throwable error, Object target);
}

