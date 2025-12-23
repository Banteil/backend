package com.example.board.post.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.board.post.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/check-password")
    public ResponseEntity<Boolean> checkPassword(@RequestBody Map<String, Object> data) {
        // JS에서 보낸 idKey인 'bno'로 값을 추출
        Long bno = Long.parseLong(data.get("bno").toString());
        String inputPw = data.get("password").toString();

        return boardRepository.findById(bno)
                .map(board -> {
                    boolean isMatch = passwordEncoder.matches(inputPw, board.getPassword());
                    return ResponseEntity.ok(isMatch);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(false));
    }
}