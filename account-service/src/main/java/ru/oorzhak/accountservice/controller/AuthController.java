package ru.oorzhak.accountservice.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.oorzhak.accountservice.dto.AccountLoginDTO;
import ru.oorzhak.accountservice.dto.AccountRegisterDTO;
import ru.oorzhak.accountservice.model.Account;
import ru.oorzhak.accountservice.model.AccountResponseDTO;
import ru.oorzhak.accountservice.service.AccountService;
import ru.oorzhak.accountservice.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AccountService accountService, AuthenticationManager authenticationManager, ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponseDTO> registerAccount(@RequestBody @Valid AccountRegisterDTO accountDTO) {
        Account account = accountService.save(accountDTO);

        AccountResponseDTO response = modelMapper.map(account, AccountResponseDTO.class);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "login",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> loginAccount(@RequestBody @Valid AccountLoginDTO accountLoginDTO) {
        var authToken = new UsernamePasswordAuthenticationToken(
                accountLoginDTO.getUsername(),
                accountLoginDTO.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authToken);

        Map<String, String> body = new HashMap<>();
        body.put("refresh", jwtUtil.generateRefreshToken(authentication));
        body.put("access", jwtUtil.generateAccessToken(authentication));

        System.out.println(jwtUtil.getUsernameFromJwtToken(jwtUtil.generateRefreshToken(authentication)));

        return ResponseEntity.ok(body);
    }

    @GetMapping(value = "access", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> generateAccessToken () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(Map.of("access", jwtUtil.generateAccessToken(authentication)));
    }

}
