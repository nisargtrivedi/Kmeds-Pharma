package listeners;

import model.My_order_model;

public interface RepeatOrder {
    void onRepeat(My_order_model my_order_model);
    void showOrder(My_order_model my_order_model);
}
