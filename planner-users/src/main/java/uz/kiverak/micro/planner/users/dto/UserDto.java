package uz.kiverak.micro.planner.users.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Value;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Data
public class UserDto implements Serializable {

    @NotNull
    String id;
    String email;
    String username;
    String password;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return username;
    }
}