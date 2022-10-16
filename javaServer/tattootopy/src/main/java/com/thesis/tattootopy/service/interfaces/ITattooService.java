package com.thesis.tattootopy.service.interfaces;

import com.thesis.tattootopy.model.Tattoo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Service
public interface ITattooService {
    List<Tattoo> getAllTattoos();
    List<Tattoo> findAllTattoosWithDescriptionLike(String like);
    List<Tattoo> findAllDemoTattoos();
    Tattoo findTattooById(Long id);
    Tattoo save(Tattoo tattoo);
    boolean update(Tattoo tattoo);
    boolean deleteTattooById(Long id);
    void generateTattoo(Tattoo tattoo, String userEmailAddress, String description, String startImagePath) throws IOException;
}
