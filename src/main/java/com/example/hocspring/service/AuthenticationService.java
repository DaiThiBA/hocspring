package com.example.hocspring.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.hocspring.dto.request.AuthenticationRequest;
import com.example.hocspring.dto.request.IntrospectRequest;
import com.example.hocspring.dto.response.AuthenticationResponse;
import com.example.hocspring.dto.response.IntrospectResponse;
import com.example.hocspring.entity.User;
import com.example.hocspring.exception.AppException;
import com.example.hocspring.exception.ErrorCode;
import com.example.hocspring.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    //hàm kiểm tra có match password 

    protected static final String SIGNER_KEY = 
    "deTMzWBYDgDR7h3WUHqEXpkkKZOaVWnSE+gkxFkK2rq7iDOJJYb29eEgPeUdkQA5";

    // @Value("${jwt.signerKey}")
    // private String SIGNER_KEY;

    
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        
        //lấy user từ db, nếu không trả về lỗi không tìm thấy user
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

           //hash password: dùng phương thức encode của PasswordEncoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

        boolean isPasswordMatch = encoder.matches(authenticationRequest.getPassword(), user.getPassword());

        if (!isPasswordMatch) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        return AuthenticationResponse.builder()
                .authenticated(isPasswordMatch)
                .token(generateToken(user))
                .build();
    }

    public String generateToken(User user){

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                                    .subject(user.getUsername())
                                    .issuer("thiuit")
                                    .issueTime(new Date())
                                    .expirationTime(new Date(
                                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                                    ))
                                    .claim("scope", buildScope(user))
                                    .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new RuntimeException(e);
        }
        
    }

    private String buildScope (User user) {
        //các scope trong oauth2 được phân cách bởi dấu cách
        //nên dùng StringJoiner để nối các scope lại với nhau
        StringJoiner stringJoiner = new StringJoiner(" ");
        
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                log.info("Role: {}", role.getName());
                stringJoiner.add( "ROLE_" + role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        log.info("Permission: {}", permission.getName());
                        stringJoiner.add(permission.getName());
                    });
                }
            });
        } else {
            log.warn("User has no roles");
        }
    
        String scope = stringJoiner.toString();
        log.info("Generated scope: {}", scope);
        return scope;
    }

    public IntrospectResponse introspect (IntrospectRequest request) throws JOSEException, ParseException{
        String token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

       

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean isnotExpired = expirationTime.after(new Date());

        var verify =  signedJWT.verify(verifier);//true hoặc false

        return IntrospectResponse.builder()
                .valid(verify && isnotExpired )
                .build();
    }

}
