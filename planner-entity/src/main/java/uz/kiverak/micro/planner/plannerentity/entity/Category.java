package uz.kiverak.micro.planner.plannerentity.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.Objects;

/*
справочное значение - категория пользователя
может использовать для своих задач
содержит статистику по каждой категории
 */

@Entity
@Table(name = "category", schema = "todo", catalog = "planner_todo")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;

    @Column(name = "completed_count", updatable = false) // т.к. это поле высчитывается автоматически в триггерах - вручную его не обновляем (updatable = false)
    private Long completedCount;

    @Column(name = "uncompleted_count", updatable = false) // т.к. это поле высчитывается автоматически в триггерах - вручную его не обновляем (updatable = false)
    private Long uncompletedCount;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "id") // по каким полям связаны эти 2 объекта (foreign key)
//    private User user;
@Column(name = "user_id")
private String userId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return title;
    }
}
