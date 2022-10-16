package com.thesis.tattootopy.persistance.interfaces;

import com.thesis.tattootopy.model.Tattoo;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Configurable
@Repository
public interface ITattooRepository extends JpaRepository<Tattoo, Long> {

}

