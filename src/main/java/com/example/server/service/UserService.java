package com.example.server.service;

import com.example.server.dto.request.EnterpriseWithAdminRegistrationDTO;
import com.example.server.dto.request.UserRegistrationDTO;
import com.example.server.dto.response.UserAccountResponseDTO;
import com.example.server.model.Enterprise;
import com.example.server.model.User;
import com.example.server.model.enums.ActionType;
import com.example.server.repository.EnterpriseRepository;
import com.example.server.repository.RoleRepository;
import com.example.server.repository.UserRepository;
import com.example.server.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;

    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       EnterpriseRepository enterpriseRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       LogService logService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
        this.jwtUtil = jwtUtil;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User registerHR(UserRegistrationDTO dto, User currentUser) {
        Enterprise enterprise = enterpriseRepository.findById(dto.getEnterpriseId())
                .orElseThrow(() -> new RuntimeException("Enterprise not found"));

        User user = createUser(dto, "HR", enterprise);
        User savedUser = userRepository.save(user);

        logService.log(currentUser, ActionType.CREATE_HR,
                "Создан HR: " + savedUser.getEmail() + ", Enterprise: " + enterprise.getName());

        return savedUser;
    }

    public User createEnterpriseAdmin(EnterpriseWithAdminRegistrationDTO dto, Enterprise enterprise) {
        User admin = new User();
        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());
        admin.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        admin.setEnterprise(enterprise);
        admin.setRole(roleRepository.findByName("ENT_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ENT_ADMIN not found")));

        User savedAdmin = userRepository.save(admin);

        logService.log(savedAdmin, ActionType.CREATE_ENTERPRISE,
                "Создана компания: " + enterprise.getName());

        logService.log(savedAdmin, ActionType.CREATE_ADMIN,
                "Создан администратор компании: " + savedAdmin.getEmail() +
                        " (компания: " + enterprise.getName() + ")");
        return savedAdmin;
    }

    private User createUser(UserRegistrationDTO dto, String roleName, Enterprise enterprise) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setEnterprise(enterprise);
        user.setRole(roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found")));
        return user;
    }

    public User authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Enterprise enterprise = enterpriseRepository.findById(
                user.getEnterprise().getId()
        ).orElseThrow(() -> new RuntimeException("Enterprise not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        logService.log(user, ActionType.LOGIN,
                "Выполнен вход: " + user.getEmail() +
                        " (компания: " + enterprise.getName() + ")");

        return user;
    }

    public UserAccountResponseDTO createEmployeeForEnterprise(
            UserRegistrationDTO dto,
            User hrUser
    ) {
        Enterprise enterprise = hrUser.getEnterprise();
        if (enterprise == null) {
            throw new RuntimeException("HR не привязан к предприятию");
        }

        User employee = new User();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        employee.setEnterprise(enterprise);
        employee.setRole(
                roleRepository.findByName("EMPLOYEE")
                        .orElseThrow(() -> new RuntimeException("Role EMPLOYEE not found"))
        );

        User saved = userRepository.save(employee);

        logService.log(hrUser, ActionType.CREATE_EMPLOYEE,
                "Создан сотрудник: " + saved.getEmail() +
                        " (компания: " + enterprise.getName() + ")");

        return toDto(saved);
    }

    public UserAccountResponseDTO updateEmployeeForEnterpriseByEmail(String email, UserRegistrationDTO dto, User hrUser) {
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        validateSameEnterprise(hrUser, employee);

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPhone(dto.getPhone());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            employee.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(employee);

        logService.log(hrUser, ActionType.UPDATE_EMPLOYEE,
                "Обновлен сотрудник: " + employee.getEmail());

        return toDto(employee);
    }

    public void deleteEmployeeForEnterpriseByEmail(String email, User hrUser) {
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        validateSameEnterprise(hrUser, employee);

        userRepository.delete(employee);

        logService.log(hrUser, ActionType.DELETE_EMPLOYEE,
                "Удалён сотрудник: " + employee.getEmail());
    }


    private void validateSameEnterprise(User hrUser, User employee) {
        if (hrUser.getEnterprise() == null ||
                employee.getEnterprise() == null ||
                !hrUser.getEnterprise().getId().equals(employee.getEnterprise().getId())) {
            throw new RuntimeException("Нет доступа к этому сотруднику");
        }
    }


    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new RuntimeException("User not authenticated");
        }

        if (auth.getPrincipal() instanceof User user) {
            return user;
        }

        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        throw new RuntimeException("Invalid authentication principal");
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HR not found"));
    }

    public UserAccountResponseDTO getUser() {
        User user = getCurrentUser();
        Enterprise enterprise = user.getEnterprise();

        UserAccountResponseDTO dto = new UserAccountResponseDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        if (enterprise != null) {
            dto.setEnterpriseId(enterprise.getId());
            dto.setEnterpriseName(enterprise.getName());
            dto.setEnterpriseAddress(enterprise.getAddress());
            dto.setEnterpriseContactEmail(enterprise.getContactEmail());
            dto.setEnterpriseContactPhone(enterprise.getContactPhone());
        }

        return dto;
    }

    public Map<String, Object> updateUser(UserRegistrationDTO request) {
        User user = getCurrentUser();

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        userRepository.save(user);

        logService.log(user, ActionType.UPDATE_HR, "Обновлен профиль HR: " + user.getEmail());

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getName());

        return Map.of(
                "profile", getUser(),
                "token", token
        );
    }

    public List<UserAccountResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        logService.log(getCurrentUser(),
                ActionType.VIEW_USERS,
                "Получен список всех пользователей");

        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<UserAccountResponseDTO> getUsersByEnterprise(Long enterpriseId) {
        List<User> users = userRepository.findByEnterpriseId(enterpriseId);

        logService.log(getCurrentUser(),
                ActionType.VIEW_ENT_USERS,
                "Получен список пользователей предприятия id=" + enterpriseId);

        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserAccountResponseDTO toDto(User user) {
        UserAccountResponseDTO dto = new UserAccountResponseDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        if (user.getEnterprise() != null) {
            dto.setEnterpriseId(user.getEnterprise().getId());
            dto.setEnterpriseName(user.getEnterprise().getName());
            dto.setEnterpriseAddress(user.getEnterprise().getAddress());
            dto.setEnterpriseContactEmail(user.getEnterprise().getContactEmail());
            dto.setEnterpriseContactPhone(user.getEnterprise().getContactPhone());
        }

        return dto;
    }

}