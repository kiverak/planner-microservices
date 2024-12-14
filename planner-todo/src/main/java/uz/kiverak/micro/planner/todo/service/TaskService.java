package uz.kiverak.micro.planner.todo.service;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.kiverak.micro.planner.plannerentity.entity.Task;
import uz.kiverak.micro.planner.todo.repo.TaskRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Cacheable(cacheNames = "tasks")
    public List<Task> findAll(String userId) {
        return repository.findByUserIdOrderByTitleAsc(userId);
    }

    public Task add(Task task) {
        return repository.save(task);
    }

    public Task update(Task task) {
        return repository.save(task);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Cacheable(cacheNames = "tasks")
    public Page<Task> findByParams(String text, Boolean completed, Long priorityId, Long categoryId, String userId, Date dateFrom, Date dateTo, PageRequest paging) {
        return repository.findByParams(text, completed, priorityId, categoryId, userId, dateFrom, dateTo, paging);
    }

    public Task findById(Long id) {
        return repository.findById(id).get();
    }


}
