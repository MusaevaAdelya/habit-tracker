package com.example.habittracker.repositories;

import com.example.habittracker.BaseTest;
import com.example.habittracker.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends BaseTest {

    @Autowired
    private UserRepository underTest;

    @Test
    @DisplayName("Should find user by email")
    void itShouldFindByEmail() {
        //Given
        User newUser=User.builder()
                .firstname("Tanjiro")
                .lastname("Kamado")
                .email("example@gmail.com")
                .build();
        underTest.save(newUser);

        //When
        Optional<User> optionalUser=underTest.findByEmail("example@gmail.com");

        //Then
        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(u -> {
                    assertThat(u).usingRecursiveComparison().isEqualTo(newUser);
                });
    }

    @Test
    @DisplayName("Should return null if a user with a given email does not exist")
    void itShouldReturnNullIfUserDoesNotExist() {
        //Given

        //When
        Optional<User> optionalUser=underTest.findByEmail("example@gmail.com");

        //Then
        assertThat(optionalUser).isEmpty();
    }

    @Test
    @DisplayName("Should return true if a user exist")
    void itShouldReturnTrueIfUserExists() {
        //Given
        User newUser=User.builder()
                .firstname("Tanjiro")
                .lastname("Kamado")
                .email("example@gmail.com")
                .build();
        underTest.save(newUser);

        //When
        boolean userExists=underTest.existsByEmail("example@gmail.com");

        //Then
        assertThat(userExists).isTrue();

    }

    @Test
    @DisplayName("Should return false if a user does not exist")
    void itShouldReturnFalseIfUserDoesNotExist() {
        //Given

        //When
        boolean userExists=underTest.existsByEmail("example@gmail.com");

        //Then
        assertThat(userExists).isFalse();

    }

    @Test
    @DisplayName("Should get user by email")
    void itShouldGetByEmail() {
        //Given
        User newUser=User.builder()
                .firstname("Tanjiro")
                .lastname("Kamado")
                .email("example@gmail.com")
                .build();
        underTest.save(newUser);

        //When
        User user=underTest.getByEmail("example@gmail.com");

        //Then
        assertThat(user).usingRecursiveComparison().isEqualTo(newUser);

    }

    @Test
    @DisplayName("Should return null if user with a given email does not exist ")
    void itShouldThrowExceptionWhenUserNotFound() {
        //Given

        //When
        User user=underTest.getByEmail("example@gmail.com");

        //Then
        assertThat(user).isNull();

    }
}