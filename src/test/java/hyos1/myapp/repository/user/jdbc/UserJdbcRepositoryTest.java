package hyos1.myapp.repository.user.jdbc;

import hyos1.myapp.enums.UserRole;
import hyos1.myapp.dto.request.UserUpdateRequestDtoWithJdbc;
import hyos1.myapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserJdbcRepositoryTest {

    @Autowired
    private UserJdbcRepository userRepository;

    @Test
    void save() {
        //given
        User userA = User.createUser("userA", "asdf@naver.com", "1234", UserRole.ROLE_USER);

        //when
        userRepository.save(userA);

        //then
        System.out.println("userA = " + userA);
        assertThat(userA.getId()).isNotNull();
    }

    @Test
    void findById() {
        //given
        User userA = User.createUser("userA", "asdf@naver.com", "1234", UserRole.ROLE_USER);
        userRepository.save(userA);

        //when
        User findUser = userRepository.findById(userA.getId()).get();

        //then
        System.out.println("userA = " + userA);
        System.out.println("findUser = " + findUser);
//        Assertions.assertThat(userA.getId()).isEqualTo(1L);
    }

    @Test
    void updateUser() {
        //given
        User userA = User.createUser("userA", "asdf@naver.com", "1234", UserRole.ROLE_USER);
        userRepository.save(userA);

        //when
        UserUpdateRequestDtoWithJdbc updateDto = new UserUpdateRequestDtoWithJdbc("AAA", "wwwwww");
        userRepository.update(userA.getId(), updateDto);
        User findUser = userRepository.findById(userA.getId()).get();

        //then
        System.out.println("userA = " + userA);
        assertThat(findUser.getName()).isEqualTo(updateDto.getName());
        assertThat(findUser.getEmail()).isEqualTo(updateDto.getEmail());
    }

    @Test
    void findAll() {
        //given
        User userA = User.createUser("userA", "1111@naver.com", "1111", UserRole.ROLE_USER);
        User userB = User.createUser("userB", "2222@naver.com", "2222", UserRole.ROLE_USER);
        User userC = User.createUser("userC", "3333@naver.com", "3333", UserRole.ROLE_USER);
        userRepository.save(userA);
        userRepository.save(userB);
        userRepository.save(userC);
        //when
        List<User> result = userRepository.findAll();

        //then
        for (User user : result) {
            System.out.println("user = " + user);
        }
        assertThat(result.size()).isEqualTo(3);
    }
}