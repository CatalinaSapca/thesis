package com.thesis.tattootopy.repositoryTests;

import com.thesis.tattootopy.model.Tattoo;
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
public class TattooRepositoryTest {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    ITattooRepository tattooRepository;

    @Test
    public void testCreateReadDelete() {
        Tattoo tattoo1 = new Tattoo("cat");
        Tattoo tattoo2 = new Tattoo("tree");
        Iterable<Tattoo> tattoos;

        int elNo = tattooRepository.findAll().size();
        Assertions.assertThat(tattooRepository.findAll()).hasSize(elNo);

        // ---------- SAVE
        tattooRepository.save(tattoo1);
        tattoos = tattooRepository.findAll();
        Assertions.assertThat(tattoos).extracting(Tattoo::getDescription).contains("cat");
        Assertions.assertThat(tattooRepository.findAll()).hasSize(elNo + 1);

        // ---------- SAVE
        tattooRepository.save(tattoo2);
        tattoos = tattooRepository.findAll();
        Assertions.assertThat(tattoos).extracting(Tattoo::getDescription).contains("tree");
        Assertions.assertThat(tattooRepository.findAll()).hasSize(elNo + 2);

        tattooRepository.deleteById(tattooRepository.findAll().get(0).getId());
        Assertions.assertThat(tattooRepository.findAll()).hasSize(elNo + 1);

        tattooRepository.deleteAll();
        Assertions.assertThat(tattooRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindById() {
        Tattoo tattoo1 = new Tattoo("cat");

        int elNo = tattooRepository.findAll().size();

        Assertions.assertThat(tattooRepository.findAll()).hasSize(elNo);

        // ---------- SAVE
        Long id = tattooRepository.save(tattoo1).getId();

        Optional<Tattoo> fromDB = tattooRepository.findById(id);
        assert (fromDB.isPresent());
        assert (fromDB.get().getDescription().equals("cat"));
        Assertions.assertThat(tattooRepository.findAll()).hasSize(elNo + 1);

        // ----------- DELETE
        tattooRepository.deleteAll();
        Assertions.assertThat(tattooRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindAll() {
        Tattoo tattoo1 = new Tattoo("cat");
        Tattoo tattoo2 = new Tattoo("tree");
        Tattoo tattoo3 = new Tattoo("dog");
        Tattoo tattoo4 = new Tattoo("bees");
        List<Tattoo> tattoos;

        int elNo = tattooRepository.findAll().size();

        Assertions.assertThat(tattooRepository.findAll()).hasSize(elNo);

        // ---------- SAVE
        tattooRepository.save(tattoo1);
        tattooRepository.save(tattoo2);
        tattooRepository.save(tattoo3);
        tattooRepository.save(tattoo4);

        tattoos = tattooRepository.findAll();
        assert (tattoos.size() == elNo + 4);

        // --------- DELETE
        tattooRepository.deleteAll();
        Assertions.assertThat(tattooRepository.findAll()).isEmpty();
    }

    @Test
    public void testUpdateTattoo() {
        Tattoo tattoo1 = new Tattoo("cat");
        List<Tattoo> tattoos;

        int elNo = tattooRepository.findAll().size();

        Assertions.assertThat(tattooRepository.findAll()).hasSize(elNo);

        // ---------- SAVE
        Long id = tattooRepository.save(tattoo1).getId();
        tattoos = tattooRepository.findAll();
        Assertions.assertThat(tattoos).extracting(Tattoo::getDescription).contains("cat");
        Assertions.assertThat(tattooRepository.findAll()).hasSize(elNo + 1);

        // ---------- UPDATE
        Tattoo fromDB = tattooRepository.findById(id).get();
        fromDB.setDescription("doggo");

        tattooRepository.save(fromDB);

        Tattoo afterUpdate = tattooRepository.findById(id).get();
        assert (afterUpdate.getDescription().equals("doggo"));

        tattooRepository.deleteAll();
        Assertions.assertThat(tattooRepository.findAll()).isEmpty();
    }
}
