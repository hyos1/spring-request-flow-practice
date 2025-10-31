package hyos1.myapp.repository.item;

import hyos1.myapp.dto.ItemUpdateDto;
import hyos1.myapp.entity.Item;
import hyos1.myapp.repository.item.jdbc.ItemSearchCond;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCond cond);
}
