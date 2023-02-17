package com.wolox.training.repositories;

import com.wolox.training.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(value = "select * from users u where u.birthdate between :date1 and :date2 and lower(u.name) like (concat('%', :name,'%')) " , nativeQuery = true)
    List<User> findByBirthdayBetweenDatesAndCharactersInName(@Param("name") String name,
                                                             @Param("date1") LocalDate date1,
                                                             @Param("date2") LocalDate date2);
}
