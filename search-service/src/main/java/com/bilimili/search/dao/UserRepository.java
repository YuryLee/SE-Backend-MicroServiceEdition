package com.bilimili.search.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findByName(String name);

    List<User> findBySid(String sid);
    @Transactional
    void deleteBySid(String sid);
}

