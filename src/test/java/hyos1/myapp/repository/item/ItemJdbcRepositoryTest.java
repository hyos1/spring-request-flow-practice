package hyos1.myapp.repository.item;

import hyos1.myapp.dto.ItemUpdateDto;
import hyos1.myapp.entity.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemJdbcRepositoryTest {

    @Autowired
    ItemJdbcRepository itemRepository;

    @Test
    void 아이템_저장() {
        //given
        Item itemA = Item.createItem("itemA", 10000, 10);

        //when
        itemRepository.save(itemA);

        //then
        System.out.println("itemA.getId() = " + itemA.getId());
        assertThat(itemA.getId()).isEqualTo(1L);
    }

    @Test
    void findById() {
        //given
        Item itemA = Item.createItem("itemA", 10000, 10);
        itemRepository.save(itemA);

        //when
        Item findItem = itemRepository.findById(itemA.getId()).get();

        //then
        System.out.println("itemA = " + itemA);
        System.out.println("findItem = " + findItem);
        assertThat(itemA).isEqualTo(findItem);
    }

    @Test
    void updateItem() {
        //given
        Item itemA = Item.createItem("itemA", 10000, 10);
        itemRepository.save(itemA);

        ItemUpdateDto updateDto = new ItemUpdateDto("water", 2000, 20);
        //when
        itemRepository.update(itemA.getId(), updateDto);
        Item findItem = itemRepository.findById(itemA.getId()).get();

        //then
        assertThat(findItem.getName()).isEqualTo("water");
        assertThat(findItem.getPrice()).isEqualTo(2000);
        assertThat(findItem.getQuantity()).isEqualTo(20);
        System.out.println("findItem = " + findItem);
    }
}