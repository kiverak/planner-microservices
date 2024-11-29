package uz.kiverak.micro.planner.todo.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.kiverak.micro.planner.plannerentity.entity.Task;
import uz.kiverak.micro.planner.todo.search.TaskSearchValues;
import uz.kiverak.micro.planner.todo.service.TaskService;
import uz.kiverak.micro.planner.utils.rest.webclient.UserWebclientBuilder;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/task") // базовый URI
public class TaskController {

    public static final String ID_COLUMN = "id";
    private final TaskService taskService;
    private UserWebclientBuilder userWebclientBuilder;

    public TaskController(TaskService taskService, UserWebclientBuilder userWebclientBuilder) {
        this.taskService = taskService;
        this.userWebclientBuilder = userWebclientBuilder;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Task>> findAll(@RequestBody Long userId) {
        return ResponseEntity.ok(taskService.findAll(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task) {

        if (task.getId() != null && task.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userWebclientBuilder.userExists(task.getUserId())) {
            return ResponseEntity.ok(taskService.add(task));
        }

        return new ResponseEntity("user id=" + task.getUserId() + " not found", HttpStatus.NOT_FOUND);

    }

    @PutMapping("/update")
    public ResponseEntity<Task> update(@RequestBody Task task) {

        if (task.getId() == null || task.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        taskService.update(task);

        return new ResponseEntity(HttpStatus.OK);

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {

        try {
            taskService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/id")
    public ResponseEntity<Task> findById(@RequestBody Long id) {
        Task task;
        try {
            task = taskService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(task);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchValues taskSearchValues) throws ParseException {
        String title = taskSearchValues.getTitle() != null ? taskSearchValues.getTitle() : null;

        Boolean completed = taskSearchValues.getCompleted() != null && taskSearchValues.getCompleted() == 1;

        Long priorityId = taskSearchValues.getPriorityId() != null ? taskSearchValues.getPriorityId() : null;
        Long categoryId = taskSearchValues.getCategoryId() != null ? taskSearchValues.getCategoryId() : null;

        String sortColumn = taskSearchValues.getSortColumn() != null ? taskSearchValues.getSortColumn() : null;
        String sortDirection = taskSearchValues.getSortDirection() != null ? taskSearchValues.getSortDirection() : null;

        Integer pageNumber = taskSearchValues.getPageNumber() != null ? taskSearchValues.getPageNumber() : null;
        Integer pageSize = taskSearchValues.getPageSize() != null ? taskSearchValues.getPageSize() : null;

        Long userId = taskSearchValues.getUserId() != null ? taskSearchValues.getUserId() : null;

        if (userId == null || userId == 0) {
            return new ResponseEntity("missed param: userId", HttpStatus.NOT_ACCEPTABLE);
        }

        // чтобы захватить в выборке все задачи по датам, независимо от времени - можно выставить время с 00:00 до 23:59

        Date dateFrom = null;
        Date dateTo = null;

        // выставить 00:01 для начальной даты (если она указана)
        if (taskSearchValues.getDateFrom() != null) {
            Calendar calendarFrom = Calendar.getInstance();
            calendarFrom.setTime(taskSearchValues.getDateFrom());
            calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
            calendarFrom.set(Calendar.MINUTE, 1);
            calendarFrom.set(Calendar.SECOND, 1);
            calendarFrom.set(Calendar.MILLISECOND, 1);

            dateFrom = calendarFrom.getTime(); // записываем начальную дату с 00:01

        }

        // выставить 23:59 для конечной даты (если она указана)
        if (taskSearchValues.getDateTo() != null) {

            Calendar calendarTo = Calendar.getInstance();
            calendarTo.setTime(taskSearchValues.getDateTo());
            calendarTo.set(Calendar.HOUR_OF_DAY, 23);
            calendarTo.set(Calendar.MINUTE, 59);
            calendarTo.set(Calendar.SECOND, 59);
            calendarTo.set(Calendar.MILLISECOND, 999);

            dateTo = calendarTo.getTime(); // записываем конечную дату с 23:59

        }


        // направление сортировки
        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        /* Вторым полем для сортировки добавляем id, чтобы всегда сохранялся строгий порядок.
            Например, если у 2-х задач одинаковое значение приоритета и мы сортируем по этому полю.
            Порядок следования этих 2-х записей после выполнения запроса может каждый раз меняться, т.к. не указано второе поле сортировки.
            Поэтому и используем ID - тогда все записи с одинаковым значением приоритета будут следовать в одном порядке по ID.
         */

        // объект сортировки, который содержит стобец и направление
        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Task> result = taskService.findByParams(title, completed, priorityId, categoryId, userId, dateFrom, dateTo, pageRequest);

        return ResponseEntity.ok(result);

    }

}
