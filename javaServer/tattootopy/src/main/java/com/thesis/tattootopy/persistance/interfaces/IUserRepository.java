package com.thesis.tattootopy.persistance.interfaces;

import com.thesis.tattootopy.model.User;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Configurable
@Repository
public interface IUserRepository extends JpaRepository<User, String> {

}
