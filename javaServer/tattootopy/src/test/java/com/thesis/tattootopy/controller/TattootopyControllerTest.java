package com.thesis.tattootopy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.thesis.tattootopy.model.Tattoo;
import com.thesis.tattootopy.model.User;
import com.thesis.tattootopy.modelDTO.TattooDTO;
import com.thesis.tattootopy.modelDTO.UserDTO;
import com.thesis.tattootopy.service.implementation.TattooService;
import com.thesis.tattootopy.service.implementation.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TattootopyController.class)
public class TattootopyControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    TattooService tattooService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void loginTest_CorrectCredentials() throws Exception {
        User user = new User("mara.mara@gmail.com", "aaa");
        String email = user.getEmail();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user.toDTO());

        Mockito.when(userService.findUserByEmail(email)).thenReturn(user);

        mockMvc.perform(post("/tattOOtopy/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(userService, times(1)).findUserByEmail("mara.mara@gmail.com");
    }

    @Test
    public void loginTest_IncorrectCredentials() throws Exception {
        User user1 = new User("mara.mara@gmail.com", "aaa");
        User userFromDb = new User("mara.mara@gmail.com", "a2a");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user1.toDTO());

        Mockito.when(userService.findUserByEmail("mara.mara@gmail.com")).thenReturn(userFromDb);

        mockMvc.perform(post("/tattOOtopy/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).findUserByEmail("mara.mara@gmail.com");
    }

    @Test
    public void loginTest_NonExistingUser() throws Exception {
        User user = new User("mara.mara@gmail.com", "aaa");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user.toDTO());

        Mockito.when(userService.findUserByEmail("mara.mara@gmail.com")).thenReturn(null);

        mockMvc.perform(post("/tattOOtopy/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findUserByEmail("mara.mara@gmail.com");
    }

    @Test
    public void loginTest_NullEmail() throws Exception {
        User user = new User(null, "aaa");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user.toDTO());

        mockMvc.perform(post("/tattOOtopy/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerTest_CorrectFields() throws Exception {
        User user = new User("mara.mara@gmail.com", "aaa", "first name", "last name");
        String email = user.getEmail();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user.toDTO());

        Mockito.when(userService.findUserByEmail(email)).thenReturn(null);

        mockMvc.perform(post("/tattOOtopy/register")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(userService, times(1)).findUserByEmail("mara.mara@gmail.com");
        verify(userService, times(1)).save(user);
    }

    @Test
    public void registerTest_ExistingUser() throws Exception {
        User user = new User("mara.mara@gmail.com", "aaa", "first name", "last name");
        String email = user.getEmail();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user.toDTO());

        Mockito.when(userService.findUserByEmail(email)).thenReturn(user);

        mockMvc.perform(post("/tattOOtopy/register")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).findUserByEmail("mara.mara@gmail.com");
        verify(userService, times(0)).save(user);
    }

    @Test
    public void registerTest_NullFields() throws Exception {
        User user = new User(null, null, null, null);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user.toDTO());

        mockMvc.perform(post("/tattOOtopy/register")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllTattoosOfUserTest_ValidRequest() throws Exception {
        UserDTO userDTO = new UserDTO("sapca.catalina@gmail.com", "aaa", "Catalina", "Sapca");

        Tattoo tattoo1 = new Tattoo("cat");
        Tattoo tattoo2 = new Tattoo("can");

        List<Tattoo> userTattoos = new ArrayList<>();
        userTattoos.add(tattoo1);
        userTattoos.add(tattoo2);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(userDTO);

        when(userService.findAllTattoosOfUser(userDTO.getEmail())).thenReturn(userTattoos);

        mockMvc.perform(post("/tattOOtopy/all-tattoos-of-user")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(userService, times(1)).findAllTattoosOfUser(userDTO.getEmail());
    }

    @Test
    public void getAllTattoosOfUserTest_NonValidRequest() throws Exception {
        UserDTO userDTO = new UserDTO("", "aaa", "Catalina", "Sapca");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(userDTO);

        mockMvc.perform(post("/tattOOtopy/all-tattoos-of-user")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).findAllTattoosOfUser(userDTO.getEmail());
    }

    @Test
    public void getTattoosWithDescriptionLikeTest_ValidRequest() throws Exception {
        String text = "fur";

        Tattoo tattoo1 = new Tattoo("cat with pink fur");
        tattoo1.setPublic(true);
        Tattoo tattoo2 = new Tattoo("fur with blue eyes");
        tattoo2.setPublic(true);

        List<Tattoo> list = new ArrayList<>();
        list.add(tattoo1);
        list.add(tattoo2);

        when(tattooService.findAllTattoosWithDescriptionLike(text)).thenReturn(list);

        mockMvc.perform(post("/tattOOtopy/search-by-keyword/" + text))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(tattooService, times(1)).findAllTattoosWithDescriptionLike(text);
    }

    @Test
    public void getTattoosWithDescriptionLikeTest_NonValidRequest() throws Exception {
        String text = "";

        mockMvc.perform(post("/tattOOtopy/search-by-keyword/" + text));

        verify(tattooService, times(0)).findAllTattoosWithDescriptionLike(text);
    }

    @Test
    public void getDemoGeneratedTattoosTest() throws Exception {
        Tattoo tattoo1 = new Tattoo("cat with pink fur");
        tattoo1.setDemo(true);
        Tattoo tattoo2 = new Tattoo("fur with blue eyes");
        tattoo2.setDemo(true);

        List<Tattoo> list = new ArrayList<>();
        list.add(tattoo1);
        list.add(tattoo2);

        when(tattooService.findAllDemoTattoos()).thenReturn(list);

        mockMvc.perform(get("/tattOOtopy/demo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(tattooService, times(1)).findAllDemoTattoos();
    }

    @Test
    public void generateTattooTest() throws Exception {
//        String description = "a cat with a red hat psychedelic";
//
//        Tattoo tattoo = new Tattoo("a cat with a red hat psychedelic");
//
//        //when(tattooService.generateTattoo(description)).thenReturn(tattoo);
//
//        mockMvc.perform(get("/tattOOtopy/generate-tattoo")
//                        .contentType(APPLICATION_JSON)
//                        .content(description))
//                .andExpect(status().isOk());
//
//        verify(tattooService, times(1)).generateTattoo(tattoo, "sapca.catalina@gmail.com", description, "");
    }

    @Test
    public void updateTattooTest() throws Exception {
        TattooDTO tattooDTO = new TattooDTO();
        tattooDTO.setDescription("a cat with a red hat psychedelic");
        tattooDTO.setId(1L);
        tattooDTO.setPublic(false);
        tattooDTO.setPath("sdfd");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(tattooDTO);

        when(tattooService.update(tattooDTO.toTattoo())).thenReturn(true);

        mockMvc.perform(post("/tattOOtopy/update-tattoo")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(tattooService, times(1)).update(tattooDTO.toTattoo());
    }

    @Test
    public void deleteTattooTest() throws Exception {
        TattooDTO tattooDTO = new TattooDTO();
        tattooDTO.setId(1L);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(tattooDTO);

        when(tattooService.deleteTattooById(tattooDTO.toTattoo().getId())).thenReturn(true);

        mockMvc.perform(delete("/tattOOtopy/delete-tattoo/"+ 1))
                .andExpect(status().isOk());

        verify(tattooService, times(1)).deleteTattooById(tattooDTO.toTattoo().getId());
    }

}
