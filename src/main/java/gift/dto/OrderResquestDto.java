package gift.dto;

public class OrderResquestDto {

    private Long optionId;
    private Integer quantity;
    private String message;

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
