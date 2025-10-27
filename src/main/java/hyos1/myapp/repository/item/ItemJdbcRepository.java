package hyos1.myapp.repository.item;

import hyos1.myapp.dto.ItemUpdateDto;
import hyos1.myapp.entity.Item;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class ItemJdbcRepository implements ItemRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public ItemJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("items")
                .usingGeneratedKeyColumns("item_id");
    }

//    @Override
//    public Item save(Item item) {
//        String sql = "insert into Item (name, price, stock_quantity) " +
//                "values (:itemName, :price, :quantity)";
//        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
//
//        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
//        template.update(sql, param, keyHolder);
//
//        long key = keyHolder.getKey().longValue();
//        item.setId(key);
//        return item;
//    }
    @Override
    public Item save(Item item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        Number key = jdbcInsert.executeAndReturnKey(param);
        item.setId(key.longValue());
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update Items " +
                "set item_name = : itemName, price = :price, quantity: :quantity " +
                "where item_id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId);

        template.update(sql, param);
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select item_id, name, price, stock_quantity from Items where item_id = :id";

        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("id", id);
            Item item = template.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String name = cond.getName();
        Integer maxPrice = cond.getMaxPrice();

        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);
        String sql = "select item_id, name, price, stock_quantity from Items";
        //동적 쿼리
        if (StringUtils.hasText(name) || maxPrice != null) {
            sql += " where";
        }
        boolean andFlag = false;
        if (StringUtils.hasText(name)) {
            sql += " name like concat('%', :name, '%')";
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                sql += " and price <= :maxPrice";
            }
            sql += " price <= :maxPrice";
        }

        return template.query(sql, param, itemRowMapper());
    }

    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class);
    }
}
