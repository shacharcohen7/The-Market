package ServiceLayer;

public class Response<T> {
    private T result;
    private String description;
    private String data; // New field for user ID
    private boolean success;

    public Response(T result, String description, String data) {
        this.result = result;
        this.description = description;
        this.data = data;
        this.success = result != null;
    }

    public Response(T result, String description) {
        this(result, description, "");
    }


    public Response(T result) {
        this(result, "", "");
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
        this.success = result != null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}

