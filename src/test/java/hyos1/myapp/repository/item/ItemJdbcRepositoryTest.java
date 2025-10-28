package hyos1.myapp.repository.item;

import hyos1.myapp.entity.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Assertions.assertThat(itemA.getId()).isEqualTo(1L);
    }

}