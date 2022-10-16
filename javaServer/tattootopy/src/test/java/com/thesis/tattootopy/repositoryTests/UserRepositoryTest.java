package com.thesis.tattootopy.repositoryTests;

import com.thesis.tattootopy.model.Tattoo;
import com.thesis.tattootopy.model.User;
import com.thesis.tattootopy.persistance.interfaces.ITattooRepository;
import com.thesis.tattootopy.persistance.interfaces.IUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    ITattooRepository tattooRepository;

    @Test
    public void testCreateReadDelete() {
        User user1 = new User("mara.mara@gmail.com", "aaa");
        User user2 = new User("ana.ana@gmail.com", "bbb");
        Iterable<User> users;

        int elNo = userRepository.findAll().size();
        Assertions.assertThat(userRepository.findAll()).hasSize(elNo);

        // ---------- SAVE
        userRepository.save(user1);
        users = userRepository.findAll();
        Assertions.assertThat(users).extracting(User::getEmail).contains("mara.mara@gmail.com");
        Assertions.assertThat(userRepository.findAll()).hasSize(elNo + 1);

        // ---------- SAVE
        userRepository.save(user2);
        users = userRepository.findAll();
        Assertions.assertThat(users).extracting(User::getEmail).contains("ana.ana@gmail.com");
        Assertions.assertThat(userRepository.findAll()).hasSize(elNo + 2);

        userRepository.deleteById("mara.mara@gmail.com");
        Assertions.assertThat(userRepository.findAll()).hasSize(elNo + 1);

        userRepository.deleteAll();
        Assertions.assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindById() {
        User user1 = new User("mara.mara@gmail.com", "aaa", "Mara", "Rara");
        User user2 = new User("ana.ana@gmail.com", "bbb");
        Iterable<User> users;

        int elNo = userRepository.findAll().size();

        Assertions.assertThat(userRepository.findAll()).hasSize(elNo);

        // ---------- SAVE
        userRepository.save(user1);

        Optional<User> fromDB = userRepository.findById("mara.mara@gmail.com");
        assert (fromDB.isPresent());
        assert (fromDB.get().getFirstName().equals("Mara"));
        assert (fromDB.get().getLastName().equals("Rara"));
        Assertions.assertThat(userRepository.findAll()).hasSize(elNo + 1);

        // ----------- DELETE
        userRepository.deleteAll();
        Assertions.assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindAll() {
        User user1 = new User("mara.mara@gmail.com", "aaa", "Mara", "Rara");
        User user2 = new User("ana.ana@gmail.com", "bbb");
        User user3 = new User("dan.dan@gmail.com", "ccc");
        User user4 = new User("cristi.cristi@gmail.com", "ddd");
        List<User> users;

        int elNo = userRepository.findAll().size();
        Assertions.assertThat(userRepository.findAll()).hasSize(elNo);

        // ---------- SAVE
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        users = userRepository.findAll();
        assert (users.size() == elNo + 4);

        // --------- DELETE
        userRepository.deleteAll();
        Assertions.assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    public void testAddUserWithTattoos() {
        Tattoo tattoo1 = new Tattoo("a cat with a hat");
        Tattoo tattoo2 = new Tattoo("tree");
        User user1 = new User("mara.mara@gmail.com", "aaa");
        user1.addTattoo(tattoo1);
        user1.addTattoo(tattoo2);

        int elNo = tattooRepository.findAll().size();
        int elNoUsers = userRepository.findAll().size();

        Assertions.assertThat(tattooRepository.findAll()).hasSize(elNo);
        tattooRepository.save(tattoo1);
        tattooRepository.save(tattoo2);
        Assertions.assertThat(tattooRepository.findAll()).hasSize(elNo + 2);

        Assertions.assertThat(userRepository.findAll()).hasSize(elNoUsers);
        userRepository.save(user1);
        Assertions.assertThat(userRepository.findAll()).hasSize(elNoUsers + 1);

        Optional<User> fromDB = userRepository.findById("mara.mara@gmail.com");
        assert (fromDB.isPresent());
        User a = fromDB.get();
        assert (a.getUserTattoos().contains(tattoo1));
        assert (a.getUserTattoos().contains(tattoo2));

        userRepository.deleteAll();
        Assertions.assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    public void testUpdateUser() {
        User user1 = new User("mara.mara@gmail.com", "aaa");
        List<User> users;

        int elNo1 = userRepository.findAll().size();
        Assertions.assertThat(userRepository.findAll()).hasSize(elNo1);

        // ---------- SAVE
        userRepository.save(user1);
        users = userRepository.findAll();
        Assertions.assertThat(users).extracting(User::getEmail).contains("mara.mara@gmail.com");
        Assertions.assertThat(userRepository.findAll()).hasSize(elNo1 + 1);

        // ---------- UPDATE
        User fromDB = userRepository.findById("mara.mara@gmail.com").get();
        fromDB.setFirstName("Mara");
        fromDB.setLastName("Rara");

        userRepository.save(fromDB);

        User afterUpdate = userRepository.findById("mara.mara@gmail.com").get();
        assert (afterUpdate.getFirstName().equals("Mara"));
        assert (afterUpdate.getLastName().equals("Rara"));

        userRepository.deleteAll();
        Assertions.assertThat(userRepository.findAll()).isEmpty();
    }
}
