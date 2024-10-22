package com.example.TodoList.repository;

import com.example.TodoList.model.Work;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;


public interface WorkRepository extends MongoRepository<Work, String> {


    Optional<Work> findById(String id);

    public void deleteById(String id);

    public  boolean existsById(String id);
    List<Work> findAllByOrderByScoreDesc();
    List<Work> findByScoreBetweenAndIsCompleted(int minScore, int maxScore, boolean isCompleted);


}
