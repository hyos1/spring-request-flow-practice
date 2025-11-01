package hyos1.myapp.repository.item.jpa;

import hyos1.myapp.dto.ItemUpdateDto;
import hyos1.myapp.entity.Item;
import hyos1.myapp.repository.item.jdbc.ItemSearchCond;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemJpaRepositoryTest {

    @Autowired
    ItemJpaRepository itemRepository;

    @Test
    void save() {
        //given
        Item itemA = Item.createItem("itemA", 10000, 10);

        //when
        Item savedItem = itemRepository.save(itemA);

        //then
        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getName()).isEqualTo(itemA.getName());
        assertThat(savedItem.getPrice()).isEqualTo(itemA.getPrice());
        assertThat(savedItem.getQuantity()).isEqualTo(itemA.getQuantity());
    }

    @Test
    void update() {
        //given
        Item itemA = Item.createItem("itemA", 10000, 10);
        itemRepository.save(itemA);
        System.out.println("변경 전 itemA = " + itemA);

        //when
        ItemUpdateDto updateDto = new ItemUpdateDto("updateA", 5000, 5);
        itemRepository.update(itemA.getId(), updateDto);
        System.out.println("변경 후 itemA = " + itemA);

        //then
        Item findItem = itemRepository.findById(itemA.getId()).get();
        assertThat(findItem.getName()).isEqualTo(updateDto.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateDto.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateDto.getQuantity());
    }

    @Test
    void findById() {
        //given
        Item itemA = Item.createItem("itemA", 10000, 10);
        itemRepository.save(itemA);

        //when
        Optional<Item> findItem = itemRepository.findById(itemA.getId());

        //then
        assertThat(findItem).isPresent();
        assertThat(findItem.get().getName()).isEqualTo(itemA.getName());
    }
    @Test
    void findAll() {
        //given
        Item item1 = Item.createItem("갤럭시10", 10000, 10);
        Item item2 = Item.createItem("갤럭시11", 20000, 10);
        Item item3 = Item.createItem("아이폰10", 30000, 20);
        Item item4 = Item.createItem("아이폰11", 40000, 20);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);

        //when&then
        //둘 다 없는 경우
        test(null, null, item1, item2, item3, item4);
        test("", null, item1, item2, item3, item4);

        //ItemName 검증
        test("갤럭시", null, item1, item2);
        test("아이폰", null, item3, item4);
        test("이폰", null, item3, item4);

        //maxPrice 검증
        test(null, 10000, item1);
        test(null, 40000, item1, item2, item3, item4);

        //둘 다 있는 경우
        test("갤럭시", 40000, item1, item2);
        test("아이폰", 40000, item3, item4);
    }

    void test(String itemName, Integer maxPrice, Item... items) {
        ItemSearchCond cond = new ItemSearchCond(itemName, maxPrice);
        List<Item> result = itemRepository.findAll(cond);
        assertThat(result).containsExactly(items);
    }
}