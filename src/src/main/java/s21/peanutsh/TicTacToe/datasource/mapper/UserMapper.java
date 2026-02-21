package s21.peanutsh.TicTacToe.datasource.mapper;


import s21.peanutsh.TicTacToe.datasource.model.UserEntity;
import s21.peanutsh.TicTacToe.domain.model.Role;
import s21.peanutsh.TicTacToe.domain.model.User;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserEntity userToEntity(User user){
        return UserEntity.builder()
                .login(user.getLogin())
                .password(user.getPassword())
                .roles(convertToDatabaseColumn(user.getRoles()))
                .build();
    }

    public static User entityToUser(UserEntity userEntity){
        return User.builder()
                .login(userEntity.getLogin())
                .password(userEntity.getPassword())
                .roles(convertToEntityAttribute(userEntity.getRoles()))
                .uuid(userEntity.getUuid())
                .build();
    }

    private static String convertToDatabaseColumn(Set<Role> roles) {
        return roles.stream().map(Role::getAuthority).collect(Collectors.joining(","));
    }

    private static Set<Role> convertToEntityAttribute(String s) {
        return  Arrays.stream(s.split(",")).map(Role::valueOf).collect(Collectors.toSet());
    }
}
