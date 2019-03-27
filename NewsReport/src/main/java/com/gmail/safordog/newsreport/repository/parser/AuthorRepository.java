package com.gmail.safordog.newsreport.repository.parser;

import com.gmail.safordog.newsreport.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsAuthorByName(String name);

//    @Query("SELECT a.id FROM authors a WHERE a.name like:n")
//    Long findIdByName(@Param("n") String n);

    Author findAuthorByName(String name);

}
