package com.example.hocspring.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.apache.bcel.classfile.annotation.RuntimeTypeAnnos;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.hocspring.dto.request.UserCreationRequest;
import com.example.hocspring.dto.request.UserUpdationRequest;
import com.example.hocspring.dto.response.ApiResponse;
import com.example.hocspring.dto.response.UserResponse;
import com.example.hocspring.entity.User;
import com.example.hocspring.enums.Role;
import com.example.hocspring.exception.AppException;
import com.example.hocspring.exception.ErrorCode;
import com.example.hocspring.mapper.UserMapper;
import com.example.hocspring.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)

@Slf4j
@Service
public class UserService {

    //đã dùng lombok để tự động khởi tạo biến final, giúp có thể tiêm vào constructor(DI) mà không cần @Autowired
    UserRepository userRepository;

    UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponse>> getAllUsers() {

        log.info("In method getAllUsers()");
        List<UserResponse> users = userRepository.findAll().stream()
        .map(userMapper::toUserResponse).toList(); // Lấy danh sách User từ DB

        return ApiResponse.<List<UserResponse>>builder()
            .code(1000)
            .result(users)
            .build();
    }

    public ApiResponse<User> createUser(UserCreationRequest request) {
        User user = new User();

        if (userRepository.existsByUsername(request.getUsername())) {
            // throw new AppException(ErrorCode.USER_EXISTED);
            throw new RuntimeException("User existed");
        }

        user = userMapper.toUser(request);

        //hash password: dùng phương thức encode của PasswordEncoder
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);

        user.setPassword(encoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

        //var result = userMapper.toUser(request);

        ApiResponse<User> apiResponse = new ApiResponse<>();
        
        //chú ý phần này để trả về đúng
        apiResponse = ApiResponse.<User>builder()
            .code(1000)
            .result(userRepository.save(user))
            .build();
        
        return apiResponse;
    }

    @PostAuthorize("returnObject.username == authetication.name")
    public ApiResponse<User> getUser(String userId){

        log.info("In method get user");
        ApiResponse<User> apiResponse = new ApiResponse<>(); 

        apiResponse.setResult(userRepository.findById(userId).
        orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));

        apiResponse.setCode(1000);

        return apiResponse;

    }

    public User updatUser (UserUpdationRequest request){

        User user = new User();

        userMapper.updateUser(user, request);
        return userRepository.save(user);
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();

        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND) );
    
        return userMapper.toUserResponse(user);
    }

}
