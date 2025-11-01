package hyos1.myapp.repository.item.datajpa;

import hyos1.myapp.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDataRepository extends JpaRepository<Item, Long> {

}
