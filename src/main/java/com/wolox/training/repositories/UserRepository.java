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

    List<User> findByBirthdateBetweenAndNameLikeIgnoreCase(LocalDate start, LocalDate end, String name);


    @Query(value = "SELECT * FROM users u WHERE ((u.birthdate >= COALESCE(?1, u.birthdate) OR ?1 IS NULL) AND (u.birthdate <= COALESCE(?2, u.birthdate) OR ?2 IS NULL)) OR" +
            " (u.birthdate BETWEEN COALESCE(?1, u.birthdate) AND COALESCE(?2, u.birthdate)) AND ((LOWER(u.name) LIKE CONCAT('%', LOWER(COALESCE(?3, u.name)), '%')) OR ?3 IS NULL)", nativeQuery = true)
    List<User> findUsersByBirthdateAndName(LocalDate birthdate, LocalDate maxBirthdate, String name);
}
