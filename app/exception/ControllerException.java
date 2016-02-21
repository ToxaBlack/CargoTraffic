package exception;


public class ControllerException extends Exception {
	public ControllerException(String message){
		super(message);
	}

	public ControllerException(String message, Throwable throwable){
		super(message, throwable);
	}
}
