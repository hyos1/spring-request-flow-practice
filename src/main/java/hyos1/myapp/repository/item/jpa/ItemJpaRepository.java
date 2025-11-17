package hyos1.myapp.repository.item.jpa;

import hyos1.myapp.dto.request.ItemUpdateRequest;
import hyos1.myapp.entity.Item;
import hyos1.myapp.repository.item.jdbc.ItemSearchCond;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public Optional<Item> findById(Long itemId) {
        return Optional.ofNullable(em.find(Item.class, itemId));
    }

    @Override
    public Optional<Item> findByIdWithLock(Long itemId) {
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.lock.timeout", 3000); // 대기시간 3초

        Item item = em.find(Item.class, itemId, LockModeType.PESSIMISTIC_WRITE, hints);
        return Optional.ofNullable(item);
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

    @Override
    public void deleteItem(Item item) {
        em.remove(item);
    }
}