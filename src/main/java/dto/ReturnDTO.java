package dto;

public class ReturnDTO {

    private int id;
    private String message;

    /**
     * The ReturnDTO class it's used to transfer data between the received json and the DB
     */

    public ReturnDTO(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
