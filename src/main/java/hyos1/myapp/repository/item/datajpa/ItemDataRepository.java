package hyos1.myapp.repository.item.datajpa;

import hyos1.myapp.entity.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemDataRepository extends JpaRepository<Item, Long>, ItemDataRepositoryCustom {

    // 이름 존재 여부 확인
    boolean existsByName(String name);
}
