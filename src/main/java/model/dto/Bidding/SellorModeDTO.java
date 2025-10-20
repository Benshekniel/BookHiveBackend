package model.dto.Bidding;

public class SellorModeDTO {


    private int user_id;
    private boolean sellor_mode;

    public SellorModeDTO(int user_id, boolean sellor_mode) {
        this.user_id = user_id;
        this.sellor_mode = sellor_mode;
    }

    public SellorModeDTO() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isSellor_mode() {
        return sellor_mode;
    }

    public void setSellor_mode(boolean sellor_mode) {
        this.sellor_mode = sellor_mode;
    }
}
