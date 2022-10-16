package com.thesis.tattootopy.service;

import com.thesis.tattootopy.model.Tattoo;
import com.thesis.tattootopy.persistance.interfaces.ITattooRepository;
import com.thesis.tattootopy.service.implementation.TattooService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TattooServiceTest {


    @InjectMocks
    TattooService tattooService;

    @Mock
    ITattooRepository tattooRepository;

    @Test
    public void getAllTattoosTest() {
        List<Tattoo> list = new ArrayList<>();
        Tattoo tattoo1 = new Tattoo("cat");
        Tattoo tattoo2 = new Tattoo("tree");
        Tattoo tattoo3 = new Tattoo("dog");
        Tattoo tattoo4 = new Tattoo("kid");

        list.add(tattoo1);
        list.add(tattoo2);
        list.add(tattoo3);
        list.add(tattoo4);

        when(tattooRepository.findAll()).thenReturn(list);

        // ---------- GET ALL TATTOOS
        List<Tattoo> userList = tattooService.getAllTattoos();

        assertEquals(4, userList.size());
        verify(tattooRepository, times(1)).findAll();
    }

    @Test
    public void findAllTattoosWithDescriptionLikeTest() {
        Tattoo tattoo1 = new Tattoo("cat with pink fur");
        tattoo1.setPublic(true);
        Tattoo tattoo2 = new Tattoo("fur with blue eyes");
        tattoo2.setPublic(true);
        Tattoo tattoo3 = new Tattoo("a land of green cats");
        tattoo3.setPublic(true);
        Tattoo tattoo4 = new Tattoo("a land of pastel fur");
        tattoo4.setPublic(false); // this should not be returned as it's private

        List<Tattoo> list = new ArrayList<>();
        list.add(tattoo1);
        list.add(tattoo2);
        list.add(tattoo3);
        list.add(tattoo4);

        when(tattooRepository.findAll()).thenReturn(list);

        // ---------- find All Tattoos With Description That Contains The Substring "fur"
        List<Tattoo> returned = tattooService.findAllTattoosWithDescriptionLike("fur");

        Assertions.assertThat(returned).extracting(Tattoo::getDescription).contains("cat with pink fur");
        Assertions.assertThat(returned).extracting(Tattoo::getDescription).contains("fur with blue eyes");
        Assertions.assertThat(returned).extracting(Tattoo::getDescription).doesNotContain("a land of pastel fur"); // does not contain

        assertEquals(returned.size(), 2);

        verify(tattooRepository, times(1)).findAll();
    }

    @Test
    public void findAllDemoTattoosTest_ExistingId() {
        Tattoo tattoo1 = new Tattoo("cat with pink fur");
        tattoo1.setDemo(true);
        Tattoo tattoo2 = new Tattoo("fur with blue eyes");
        tattoo2.setDemo(true);
        Tattoo tattoo3 = new Tattoo("a land of green cats");
        tattoo3.setDemo(false);
        Tattoo tattoo4 = new Tattoo("a land of pastel fur");
        tattoo4.setDemo(false);

        List<Tattoo> list = new ArrayList<>();
        list.add(tattoo1);
        list.add(tattoo2);
        list.add(tattoo3);
        list.add(tattoo4);

        when(tattooRepository.findAll()).thenReturn(list);

        // ---------- find All Demo Tattoos
        List<Tattoo> returned = tattooService.findAllDemoTattoos();

        Assertions.assertThat(returned).extracting(Tattoo::getDescription).contains("cat with pink fur");
        Assertions.assertThat(returned).extracting(Tattoo::getDescription).contains("fur with blue eyes");
        Assertions.assertThat(returned).extracting(Tattoo::getDescription).doesNotContain("a land of pastel fur");

        assertEquals(returned.size(), 2);

        verify(tattooRepository, times(1)).findAll();
    }

    @Test
    public void findTattooByIdTest_ExistingId() {
        Tattoo tattoo1 = new Tattoo("cat");
        Tattoo tattoo2 = new Tattoo("tree");

        when(tattooRepository.findById(1L)).thenReturn(Optional.of(tattoo1));
        when(tattooRepository.findById(2L)).thenReturn(Optional.of(tattoo2));

        // ---------- find Tattoo By Id
        Tattoo returned1 = tattooService.findTattooById(1L);
        Tattoo returned2 = tattooService.findTattooById(2L);

        assertEquals(returned1, tattoo1);
        assertEquals(returned2, tattoo2);

        verify(tattooRepository, times(1)).findById(1L);
        verify(tattooRepository, times(1)).findById(2L);
    }

    @Test
    public void findTattooByIdTest_NonExistingId() {
        when(tattooRepository.findById(1L)).thenReturn(Optional.empty());
        when(tattooRepository.findById(2L)).thenReturn(Optional.empty());

        // ---------- find Tattoo By Id
        Tattoo returned1 = tattooService.findTattooById(1L);
        Tattoo returned2 = tattooService.findTattooById(2L);

        assertNull(returned1);
        assertNull(returned2);

        verify(tattooRepository, times(1)).findById(1L);
        verify(tattooRepository, times(1)).findById(2L);
    }

    // updating an already existing tattoo should return true
    @Test
    public void updateTattooTest_ExistingTattoo() {
        Tattoo tattoo = new Tattoo("cat");
        tattoo.setId(1L);

        when(tattooRepository.getById(1L)).thenReturn(tattoo);
        when(tattooRepository.save(tattoo)).thenReturn(tattoo);

        // ---------- update Tattoo
        boolean returned = tattooService.update(tattoo);
        assertTrue(returned);

        verify(tattooRepository, times(1)).getById(1L);
        verify(tattooRepository, times(1)).save(tattoo);
    }

    // deleting a tattoo that exists
    @Test
    public void deleteTattooTest_ExistedTattoo() {
        Tattoo tattoo = new Tattoo("cat");
        tattoo.setId(1L);

        when(tattooRepository.findById(1L)).thenReturn(Optional.of(tattoo));

        // ---------- delete Tattoo
        boolean returned = tattooService.deleteTattooById(1L);
        assertTrue(returned);

        verify(tattooRepository, times(1)).findById(1L);
        verify(tattooRepository, times(1)).deleteById(1L);
    }

    // deleting a non-existing tattoo
    @Test
    public void deleteTattooTest_NonExistingTattoo() {
        when(tattooRepository.findById(1L)).thenReturn(Optional.empty());

        // ---------- delete Tattoo
        boolean returned = tattooService.deleteTattooById(1L);
        assertFalse(returned);

        verify(tattooRepository, times(1)).findById(1L);
        verify(tattooRepository, times(0)).deleteById(1L);
    }


    // generating a tattoo
    @Test
    public void generateTattooTest() throws InterruptedException, IOException {
    }
}
