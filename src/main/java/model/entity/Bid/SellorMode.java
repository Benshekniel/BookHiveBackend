package model.entity.Bid;


import jakarta.persistence.*;
import lombok.Data;

//@Entity
//@Table(name = "sellor_mode")
//@Data
//public class SellorMode {
//
//    @Id
//    @Column(name="user_id")
//    private int userId;
//
//    @Column(name="sellor_mode")
//    private boolean sellorMode;
//
//    public SellorMode(int userId, boolean sellorMode) {
//        this.userId = userId;
//        this.sellorMode = sellorMode;
//    }
//
//    public SellorMode() {
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public boolean isSellorMode() {
//        return sellorMode;
//    }
//
//    public void setSellorMode(boolean sellorMode) {
//        this.sellorMode = sellorMode;
//    }
//}

@Entity
@Table(name = "sellor_mode")
public class SellorMode {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "sellor_mode")
    private boolean sellorMode;

    public SellorMode() {}

    public SellorMode(int userId, boolean sellorMode) {
        this.userId = userId;
        this.sellorMode = sellorMode;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public boolean isSellorMode() { return sellorMode; }
    public void setSellorMode(boolean sellorMode) { this.sellorMode = sellorMode; }
}
