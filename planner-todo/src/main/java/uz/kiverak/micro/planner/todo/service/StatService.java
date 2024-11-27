package uz.kiverak.micro.planner.todo.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uz.kiverak.micro.planner.plannerentity.entity.Stat;
import uz.kiverak.micro.planner.todo.repo.StatRepository;

@Service
@Transactional
public class StatService {

    private final StatRepository repository;

    public StatService(StatRepository repository) {
        this.repository = repository;
    }

    public Stat findStat(Long userId) {
        return repository.findByUserId(userId);
    }

}
