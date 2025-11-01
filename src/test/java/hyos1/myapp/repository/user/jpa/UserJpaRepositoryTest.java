package hyos1.myapp.repository.user.jpa;

import hyos1.myapp.common.UserType;
import hyos1.myapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserJpaRepositoryTest {

    @Autowired
    UserJpaRepository userRepository;

    @Test
    void saveAndFindById() {
        //given
        User user = User.createUser("userA", "asdf@naver.com", "1234", UserType.USER);

        //when
        User savedUser = userRepository.save(user);
        Optional<User> findUser = userRepository.findById(savedUser.getId());

        //then
        assertThat(findUser).isPresent();
        assertThat(findUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(findUser.get().getName()).isEqualTo("userA");
    }

    @Test
    void findAllUsers() {
        //given
        User user1 = User.createUser("userA", "asdf@naver.com", "1234", UserType.USER);
        User user2 = User.createUser("userB", "asdf@naver.com", "1234", UserType.USER);
        User user3 = User.createUser("userC", "asdf@naver.com", "1234", UserType.USER);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        //when
        List<User> users = userRepository.findAll();
        //then
        assertThat(users).hasSize(3);
        assertThat(users).extracting("name")
                .containsExactly(user1.getName(), user2.getName(), user3.getName());
    }

}