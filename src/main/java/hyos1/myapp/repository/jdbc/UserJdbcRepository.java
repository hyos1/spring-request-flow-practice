package hyos1.myapp.repository.jdbc;

import hyos1.myapp.dto.ItemUpdateDto;
import hyos1.myapp.entity.Item;

import java.util.List;
import java.util.Optional;

public class UserJdbcRepository implements ItemRepository{
    @Override
    public Item save(Item item) {
        return null;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {

    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        return null;
    }
}
