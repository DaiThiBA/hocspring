package com.example.hocspring.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hocspring.dto.request.UserCreationRequest;
import com.example.hocspring.dto.request.UserUpdationRequest;
import com.example.hocspring.dto.response.ApiResponse;
import com.example.hocspring.entity.User;
import com.example.hocspring.exception.AppException;
import com.example.hocspring.exception.ErrorCode;
import com.example.hocspring.mapper.UserMapper;
import com.example.hocspring.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)

@Service
public class UserService {

    //đã dùng lombok để tự động khởi tạo biến final, giúp có thể tiêm vào constructor(DI) mà không cần @Autowired
    UserRepository userRepository;

    UserMapper userMapper;



    public ApiResponse<User> createUser(UserCreationRequest request) {
        User user = new User();

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        user = userMapper.toUser(request);

        //hash password: dùng phương thức encode của PasswordEncoder
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);

        user.setPassword(encoder.encode(request.getPassword()));

        //var result = userMapper.toUser(request);

        ApiResponse<User> apiResponse = new ApiResponse<>();
        
        //chú ý phần này để trả về đúng
        apiResponse = ApiResponse.<User>builder()
            .code(1000)
            .result(userRepository.save(user))
            .build();
        
        return apiResponse;
    }

    public ApiResponse<User> getUser(String userId){

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
}
