package uz.kiverak.micro.planner.todo.service;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.kiverak.micro.planner.plannerentity.entity.Category;
import uz.kiverak.micro.planner.todo.repo.CategoryRepository;

import java.util.List;

@Service

@Transactional
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Cacheable(cacheNames = "categories")
    public List<Category> findAll(String userId) {
        return repository.findByUserIdOrderByTitleAsc(userId);
    }

    public Category add(Category category) {
        return repository.save(category);
    }

    public Category update(Category category) {
        return repository.save(category);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Category> findByTitle(String text, String userId) {
        return repository.findByTitle(text, userId);
    }

    public Category findById(Long id) {
        return repository.findById(id).get();
    }

}
