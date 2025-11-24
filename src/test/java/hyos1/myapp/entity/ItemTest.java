package hyos1.myapp.entity;

import hyos1.myapp.common.exception.ServerException;
import hyos1.myapp.common.exception.constant.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private Item item;
    @BeforeEach
    void beforeEach() {
        item = Item.createItem("itemA", 1000, 10);
    }
    @Test
    void 아이템_이름_변경() {
        //when
        item.changeName("newItem");

        //then
        assertThat(item.getName()).isEqualTo("newItem");
    }

    @Test
    void 재고_감소_정상() {
        //when
        item.decreaseStock(1);

        //then
        assertThat(item.getStock()).isEqualTo(9);
    }
    @Test
    void 재고_감소_예외() {
        //given
        item.setStock(0);

        //when & then
        assertThatThrownBy(() -> item.decreaseStock(1))
                .isInstanceOf(ServerException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ITEM_STOCK_EMPTY);
    }

    @Test
    void 재고_증가() {
        //given
        int addStock = 5;

        //when
        item.addQuantity(addStock);

        //then
        assertThat(item.getStock()).isEqualTo(10 + addStock);
    }
    @Test
    void 가격_수량_업데이트() {
        //given
        int price = 2000;
        int stock = 5;

        //when
        item.updatePriceAndStock(price, stock);

        //then
        assertThat(item.getPrice()).isEqualTo(price);
        assertThat(item.getStock()).isEqualTo(stock);
    }
}