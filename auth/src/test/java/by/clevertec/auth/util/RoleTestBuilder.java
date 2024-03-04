package by.clevertec.auth.util;

import by.clevertec.auth.domain.Role;
import by.clevertec.auth.domain.User;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static by.clevertec.auth.util.TestConstant.ROLE_ID;
import static by.clevertec.auth.util.TestConstant.ROLE_NAME;

@Data
@Builder(setterPrefix = "with")
public class RoleTestBuilder {

    @Builder.Default
    private Long id = ROLE_ID;

    @Builder.Default
    private String name = ROLE_NAME;

    @Builder.Default
    private List<User> users = new ArrayList<>();

    public Role buildRole() {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setUsers(users);
        return role;
    }
}
