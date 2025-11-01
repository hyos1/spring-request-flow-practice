package hyos1.myapp.repository.user.datajpa;

import hyos1.myapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<User, Long> {
}
