package com.example.TodoList.controller;

import com.example.TodoList.model.Work;
import com.example.TodoList.service.WorkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/work")
@Tag(name = "Work Management", description = "API để quản lý công việc")

public class WorkController {

    @Autowired
    private WorkService workService;

    @GetMapping("/todoList")
    @Operation(summary = "Lấy tất cả công việc", description = "Trả về danh sách tất cả công việc")
    public String getAllWork(Model model) {
        List<Work> works = workService.getAllWork();
        model.addAttribute("works", works);
        return "workList";
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy công việc theo ID", description = "Trả về thông tin công việc theo ID")
    public String getWorkDetail(@Parameter(description = "ID của công việc cần lấy") @PathVariable String id, Model model) {
        Optional<Work> workOptional = workService.getWorkById(id);
        if (workOptional.isPresent()) {
            model.addAttribute("work", workOptional.get());
        } else {
            model.addAttribute("errorMessage", "Công việc không tồn tại");
        }
        return "workDetail";
    }

    @GetMapping("/sort")
    public String showSortForm(Model model) {
        model.addAttribute("work", new Work()); // Đối tượng chứa thông tin lọc
        return "sortWork";
    }

    @GetMapping("/list")
    @Operation(summary = "Lấy công việc theo điểm số và trạng thái hoàn thành")
    public String getWorkByScoreBtwIsCompleted(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int minScore,
            @RequestParam(defaultValue = "100") int maxScore,
            @RequestParam(defaultValue = "false") boolean isCompleted,
            Model model) {

        List<Work> works = workService.getWorkList(limit, minScore, maxScore, isCompleted);
        model.addAttribute("works", works);
        return "workList";
    }

    @GetMapping("/add")
    public String addWork(Model model) {
        model.addAttribute("work", new Work());
        return "addWork";
    }

    @PostMapping("/insert")
    @Operation(summary = "Thêm công việc mới", description = "Thêm một công việc mới vào danh sách")
    public String insertWork(@Valid Work work, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("work", work);
            return "addWork";
        } else {
            workService.addWork(work);
            return "redirect:/work/" + work.getId();
        }
    }
    @GetMapping("/confirmDelete/{id}")
    @Operation(summary = "Xác nhận xóa công việc theo id", description = "Hiển thị chi tiết công việc và xác nhận xóa")
    public String confirmDelete(@PathVariable(value = "id") String id, Model model) {
        Optional<Work> workOpt = workService.getWorkById(id);
        if (workOpt.isPresent()) {
            model.addAttribute("work", workOpt.get());
            return "confirmDelete"; // Trả về trang xác nhận
        }
        return "redirect:/work/todoList"; // Nếu không tìm thấy công việc, quay lại danh sách
    }


    @PostMapping("/delete/{id}")
    @Operation(summary = "Xóa Công việc theo id ", description = "Xóa một công việc theo id ")
    public String deleteWork(@PathVariable(value = "id") String id) {
        if (workService.getWorkById(id).isPresent()) {
            Work work = workService.getWorkById(id).get();
            workService.deleteWork(id);
        }
        return "redirect:/work/todoList";
    }

    @GetMapping("/update/{id}")
    @Operation(summary = "Lấy công việc để cập nhật", description = "Lấy thông tin công việc theo ID để cập nhật")
    public String updateWork(@Parameter(description = "ID của công việc cần cập nhật") @PathVariable String id, Model model) {
        workService.getWorkById(id).ifPresent(work -> model.addAttribute("work", work));
        return "workUpdate";
    }

    @PostMapping("/save")
    @Operation(summary = "Lưu công việc đã cập nhật", description = "Lưu thông tin công việc sau khi cập nhật")
    public String saveWork(@Valid Work work, BindingResult result) {
        if (result.hasErrors()) {
            return "workUpdate";
        } else {
            workService.addWork(work);
            return "redirect:/work/" + work.getId();
        }
    }
}