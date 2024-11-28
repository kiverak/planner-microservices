package uz.kiverak.micro.planner.users.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.kiverak.micro.planner.plannerentity.entity.User;
import uz.kiverak.micro.planner.users.repo.UserRepository;

import java.util.Optional;

@Service

@Transactional
public class UserService {

    private final UserRepository repository; // сервис имеет право обращаться к репозиторию (БД)

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User add(User user) {
        return repository.save(user);
    }

    public User update(User user) {
        return repository.save(user);
    }

    public void deleteByUserId(Long id) {
        repository.deleteById(id);
    }

    public void deleteByUserEmail(String email) {
        repository.deleteByEmail(email);
    }

    public Page<User> findByParams(String username, String password, PageRequest paging) {
        return repository.findByParams(username, password, paging);
    }

}
