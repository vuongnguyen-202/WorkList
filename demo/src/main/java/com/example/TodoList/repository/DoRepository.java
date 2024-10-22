package com.example.TodoList.repository;

import com.example.TodoList.model.Work;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DoRepository {
    private final MongoTemplate mongoTemplate;
    public DoRepository(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    // Truy vấn thủ công để tìm Work theo ID
    public Optional<Work> findWorkById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Work work = mongoTemplate.findOne(query, Work.class);
        return Optional.ofNullable(work);
    }
    // Truy vấn thủ công để xóa Work theo ID
    public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Work.class);
    }
    // Truy vấn thủ công để kiểm tra xem Work có tồn tại hay không
    public boolean existsById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return mongoTemplate.exists(query, Work.class);
    }
    // Truy vấn thủ công để tìm tất cả Work và sắp xếp theo Score giảm dần
    public List<Work> findAllByOrderByScoreDesc() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "score"));
        return mongoTemplate.find(query, Work.class);
    }
    // Truy vấn thủ công để tìm Work theo khoảng điểm và trạng thái hoàn thành
    public List<Work> findByScoreBetweenAndIsCompleted(int minScore, int maxScore, boolean isCompleted) {
        Query query = new Query();
        query.addCriteria(Criteria.where("score").gte(minScore).lte(maxScore)
                .and("isCompleted").is(isCompleted));
        return mongoTemplate.find(query, Work.class);
    }
    public Work save(Work work) {
        return mongoTemplate.save(work);
    }


}