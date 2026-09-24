package com.project.workshopmongo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.workshopmongo.domain.Post;

@Repository 
public interface PostRepository extends MongoRepository<Post, String> {
    // Buscar posts por título (contendo texto - case insensitive)
    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    List<Post> findByTitleContaining(String text);

    // OU usando método com query derivation:
    List<Post> findByTitleContainingIgnoreCase(String title);

    // Buscar posts do autor por ID
    List<Post> findByAuthorId(String authorId);

    // Buscar posts entre duas datas
    List<Post> findByDateBetween(Date startDate, Date endDate);

    // Buscar texto no corpo do post
    @Query("{ 'body': { $regex: ?0, $options: 'i' } }")
    List<Post> findByBodyContaining(String text);

    // Buscar full text em título E corpo
    @Query("{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'body': { $regex: ?0, $options: 'i' } } ] }")
    List<Post> fullTextSearch(String text);
}
