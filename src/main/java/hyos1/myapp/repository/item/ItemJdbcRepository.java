package hyos1.myapp.repository.item;

import hyos1.myapp.dto.ItemUpdateDto;
import hyos1.myapp.entity.Item;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class ItemJdbcRepository implements ItemRepository {

    private final NamedParameterJdbcTemplate template;

    public ItemJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {

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
