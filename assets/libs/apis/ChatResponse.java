package assets.libs.apis;

public class ChatResponse {
    private boolean success;
    private String content;
    private String errorMessage;
    private int statusCode;

    public ChatResponse(boolean success, String content, String errorMessage, int statusCode) {
        this.success = success;
        this.content = content;
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }

    public boolean isSuccess() { return success; }
    public String getContent() { return content; }
    public String getErrorMessage() { return errorMessage; }
    public int getStatusCode() { return statusCode; }
}
