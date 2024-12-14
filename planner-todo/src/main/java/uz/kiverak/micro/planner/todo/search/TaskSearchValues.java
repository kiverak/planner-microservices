package uz.kiverak.micro.planner.todo.search;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchValues {

    private String title;
    private Integer completed;
    private Long priorityId;
    private Long categoryId;
    private String userId;

    private Date dateFrom;
    private Date dateTo;

    private Integer pageNumber;
    private Integer pageSize;

    private String sortColumn;
    private String sortDirection;

}
