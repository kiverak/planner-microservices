package uz.kiverak.micro.planner.todo.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uz.kiverak.micro.planner.plannerentity.entity.Priority;
import uz.kiverak.micro.planner.todo.repo.PriorityRepository;

import java.util.List;

@Service
@Transactional
public class PriorityService {

    private final PriorityRepository repository;

    public PriorityService(PriorityRepository repository) {
        this.repository = repository;
    }

    public List<Priority> findAll(String userId) {
        return repository.findByUserIdOrderByIdAsc(userId);
    }

    public Priority add(Priority priority) {
        return repository.save(priority);
    }

    public Priority update(Priority priority) {
        return repository.save(priority);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Priority findById(Long id) {
        return repository.findById(id).get();
    }

    public List<Priority> find(String title, String userId) {
        return repository.findByTitle(title, userId);
    }

}
