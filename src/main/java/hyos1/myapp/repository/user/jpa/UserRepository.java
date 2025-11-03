package hyos1.myapp.repository.user.jpa;

import hyos1.myapp.dto.UserUpdateDto;
import hyos1.myapp.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();
}
