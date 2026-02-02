package com.example.boardv1.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(UserRepository.class)
@DataJpaTest // EntityManger가 ioc에 등록됨
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void save_fail_test() {
        // given
        User user = new User(); // 비영속 객체
        user.setUsername("ssar");
        user.setPassword("1234");
        user.setEmail("ssar@nate.com");

        // when
        User findUser = userRepository.save(user); // 영속화됨
        // eye
        System.out.println(findUser);
    }

    @Test
    public void save_test() {
        // given
        User user = new User(); // 비영속 객체
        user.setUsername("love");
        user.setPassword("1234");
        user.setEmail("love@nate.com");

        // when
        User findUser = userRepository.save(user); // 영속화됨
        // eye
        System.out.println(findUser);
    }

    @Test
    public void findByUsername_test() {
        // given
        String username = "ssar";

        // when
        User findUser = userRepository.findByUsername(username);

        // eye
        System.out.println(findUser);
    }
}
