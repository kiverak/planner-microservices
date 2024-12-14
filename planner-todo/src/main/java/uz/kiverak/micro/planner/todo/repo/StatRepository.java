package uz.kiverak.micro.planner.todo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.kiverak.micro.planner.plannerentity.entity.Stat;

@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

    Stat findByUserId(String userId);
}
