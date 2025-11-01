package hyos1.myapp.repository.item.jpa;

import hyos1.myapp.dto.ItemUpdateDto;
import hyos1.myapp.entity.Item;
import hyos1.myapp.repository.item.ItemRepository;
import hyos1.myapp.repository.item.jdbc.ItemSearchCond;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemJpaRepository implements ItemRepository {

    private final EntityManager em;

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 상품입니다.")
        );
        item.setName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        return Optional.ofNullable(em.find(Item.class, itemId));
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String name = cond.getName();
        Integer maxPrice = cond.getMaxPrice();

        String jpql = "select i from Item i";
        boolean isFirst = true;

        if (StringUtils.hasText(name) || maxPrice != null) {
            jpql += " where";
        }

        if (StringUtils.hasText(name)) {
            jpql += " i.name like concat('%', :name, '%')";
            isFirst = false;
        }

        if (maxPrice != null) {
            if (!isFirst) {
                jpql += " and";
            }
            jpql += " i.price <= :maxPrice";
        }

        TypedQuery<Item> query = em.createQuery(jpql, Item.class);

        // 파라미터 바인딩
        if (StringUtils.hasText(name)) {
            query.setParameter("name", name);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }

        return query.getResultList();
    }
}
