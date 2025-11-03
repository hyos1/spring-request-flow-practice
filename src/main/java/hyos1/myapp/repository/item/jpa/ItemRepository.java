package hyos1.myapp.repository.item.jpa;

import hyos1.myapp.entity.Item;
import hyos1.myapp.repository.item.jdbc.ItemSearchCond;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCond cond);

    void deleteItem(Item item);
}
