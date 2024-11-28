package uz.kiverak.micro.planner.users.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchValues {

    private String email;
    private String username;

    private Integer pageNumber;
    private Integer pageSize;

    private String sortColumn;
    private String sortDirection;

}
