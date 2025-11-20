package hyos1.myapp.repository.item.jpa;

import hyos1.myapp.entity.Item;
import hyos1.myapp.repository.item.jdbc.ItemSearchCond;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    Optional<Item> findById(Long itemId);

    // 비관적 락 + 3초 대기
    Optional<Item> findByIdWithLock(Long itemId);

    // 검색 조건 name, maxPrice
    List<Item> findAll(ItemSearchCond cond);

    void deleteItem(Item item);

    // 중복 아이템 여부 확인
    boolean existsByName(String name);
}
