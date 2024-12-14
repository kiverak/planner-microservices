package uz.kiverak.micro.planner.plannerentity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.type.NumericBooleanConverter;

import java.util.Objects;

@Entity
@Table(name = "activity", schema = "todo", catalog = "planner_todo")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = NumericBooleanConverter.class) // для автоматической конвертации числа в true/false
    private Boolean activated; // становится true только после подтверждения активации пользователем (обратно false уже стать не может по логике)

    @Column(updatable = false)
    private String uuid;

//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user;
    @Column(name = "user_id")
    private String userId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id.equals(activity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
