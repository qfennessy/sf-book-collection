package com.sfcollection.controller;

import com.sfcollection.dto.ResponseDTO;
import com.sfcollection.dto.auth.JwtResponse;
import com.sfcollection.dto.auth.LoginRequest;
import com.sfcollection.dto.auth.MessageResponse;
import com.sfcollection.dto.auth.SignupRequest;
import com.sfcollection.model.Role;
import com.sfcollection.model.User;
import com.sfcollection.repository.RoleRepository;
import com.sfcollection.repository.UserRepository;
import com.sfcollection.security.JwtUtils;
import com.sfcollection.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication API for user management")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and generate JWT token")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<ResponseDTO<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        JwtResponse jwtResponse = JwtResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
        
        return ResponseEntity.ok(ResponseDTO.of(jwtResponse));
    }

    @PostMapping("/signup")
    @Operation(summary = "Register", description = "Register a new user")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Bad request - invalid input or username/email already taken")
    public ResponseEntity<ResponseDTO<MessageResponse>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Validate username and email availability
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseDTO.of(new MessageResponse("Error: Username is already taken!")));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseDTO.of(new MessageResponse("Error: Email is already in use!")));
        }

        // Create new user's account
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            try {
                Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: User Role not found in database."));
                roles.add(userRole);
            } catch (Exception e) {
                // Create default roles if they don't exist
                try {
                    Role userRole = new Role();
                    userRole.setName(Role.RoleName.ROLE_USER);
                    roleRepository.save(userRole);
                    roles.add(userRole);
                } catch (Exception ex) {
                    throw new RuntimeException("Error: Failed to create default role: " + ex.getMessage());
                }
            }
        } else {
            strRoles.forEach(role -> {
                try {
                    switch (role.toLowerCase()) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(Role.RoleName.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Admin Role not found in database."));
                            roles.add(adminRole);
                            break;
                        case "user":
                            Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: User Role not found in database."));
                            roles.add(userRole);
                            break;
                        default:
                            throw new RuntimeException("Error: Invalid role '" + role + "'. Use 'user' or 'admin'.");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error assigning role '" + role + "': " + e.getMessage());
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(ResponseDTO.of(new MessageResponse("User registered successfully!")));
    }
}