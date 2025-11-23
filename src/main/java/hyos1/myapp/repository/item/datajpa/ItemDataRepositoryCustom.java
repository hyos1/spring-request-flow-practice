package hyos1.myapp.repository.item.datajpa;

import hyos1.myapp.entity.Item;
import hyos1.myapp.repository.item.jdbc.ItemSearchCond;

import java.util.List;
import java.util.Optional;

public interface ItemDataRepositoryCustom {

    Optional<Item> findByIdWithLock(Long itemId);

    List<Item> findAll(ItemSearchCond cond);
}
