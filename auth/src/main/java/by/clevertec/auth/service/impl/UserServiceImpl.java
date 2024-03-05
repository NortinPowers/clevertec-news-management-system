package by.clevertec.auth.service.impl;

import static by.clevertec.auth.utils.Constants.ENTITY_NOT_FOUND_EXCEPTION_MESSAGE;
import static by.clevertec.auth.utils.Constants.ROLE_ADMIN;
import static by.clevertec.auth.utils.Constants.USERNAME_NOT_FOUND_EXCEPTION_MESSAGE;
import static by.clevertec.auth.utils.ResponseUtils.DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE;

import by.clevertec.aspect.ServiceAspectLogger;
import by.clevertec.auth.UserDto;
import by.clevertec.auth.domain.Role;
import by.clevertec.auth.domain.User;
import by.clevertec.auth.dto.UserRegistrationDto;
import by.clevertec.auth.mapper.UserMapper;
import by.clevertec.auth.repository.RoleRepository;
import by.clevertec.auth.repository.UserRepository;
import by.clevertec.auth.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @ServiceAspectLogger
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    @Override
    @ServiceAspectLogger
    public void save(UserRegistrationDto userRegistrationDto) {
        userRegistrationDto.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        User user = userMapper.toDomain(userRegistrationDto);
//        Optional<Role> optionalRole = roleRepository.findByName(ROLE_USER);
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_SUBSCRIBER");
        if (optionalRole.isPresent()) {
            user.setRole(optionalRole.get());
            userRepository.save(user);
        } else {
            throw new DataSourceLookupFailureException(DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE);
        }
    }

    @Override
    @ServiceAspectLogger
    public boolean isUserExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    @ServiceAspectLogger
    public void setRoleAdmin(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            Optional<Role> optionalRole = roleRepository.findByName(ROLE_ADMIN);
//            Optional<Role> optionalRole = roleRepository.findByName("ADMIN");
            if (optionalRole.isPresent()) {
                User user = userOptional.get();
                Role role = optionalRole.get();
                user.setRole(role);
                userRepository.save(user);
            } else {
                throw new DataSourceLookupFailureException(DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE);
            }
        } else {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    @Override
    @ServiceAspectLogger
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }
}
