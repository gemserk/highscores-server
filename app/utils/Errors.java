package utils;

public enum Errors {
	InvalidApiKey(601), InvalidLeaderboard(602), InvalidUser(603), UserAuthenticationFailed(604), GenericError(699);
	
	public final int errorCode;

	private Errors(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public static int MAINERROR = 600;
}
