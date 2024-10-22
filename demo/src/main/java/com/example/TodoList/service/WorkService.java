
package com.example.TodoList.service;

import com.example.TodoList.model.Work;
import com.example.TodoList.repository.DoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkService {

    @Autowired
    private DoRepository doRepository;

    // Lấy tất cả công việc đã được sắp xếp theo điểm giảm dần
    public List<Work> getAllWork() {
        return doRepository.findAllByOrderByScoreDesc();
    }

    // Lấy công việc theo ID
    public Optional<Work> getWorkById(String id) {
        return doRepository.findWorkById(id);
    }

    // Lấy danh sách công việc theo trọng số và trạng thái hoàn thành, giới hạn số lượng trả về
    public List<Work> getWorkList(int limit, int minScore, int maxScore, boolean isCompleted) {
        return doRepository.findByScoreBetweenAndIsCompleted(minScore, maxScore, isCompleted)
                .stream()
                .limit(limit)
                .toList();
    }

    // Thêm công việc mới
    public Work addWork(Work work) {
        return doRepository.save(work);
    }

    // Cập nhật thông tin công việc
    public Optional<Work> updateWork(String id, Work detail) {
        return doRepository.findWorkById(id).map(work -> {
            work.setTitle(detail.getTitle());
            work.setDescription(detail.getDescription());
            work.setCreateTime(detail.getCreateTime());
            work.setLastModified(detail.getLastModified());
            work.setScore(detail.getScore());
            return doRepository.save(work);
        });
    }

    // Xóa công việc theo ID nếu tồn tại
    public boolean deleteWork(String id) {
        if (doRepository.existsById(id)) {
            doRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
