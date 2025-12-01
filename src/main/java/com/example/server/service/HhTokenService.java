package com.example.server.service;

import com.example.server.dto.response.HhAuthTokenResponse;
import com.example.server.model.HhToken;
import com.example.server.model.User;
import com.example.server.repository.HhTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class HhTokenService {

    private final HhTokenRepository repo;
    private final UserService userService;


    public void saveTokenForHr(Long hrId, HhAuthTokenResponse token) {
        User hr = userService.getById(hrId);

        HhToken entity = repo.findByHr(hr)
                .orElse(HhToken.builder().hr(hr).build());

        entity.setAccessToken(token.getAccess_token());
        entity.setRefreshToken(token.getRefresh_token());
        entity.setExpiresAt(Instant.now().plusSeconds(token.getExpires_in()));

        repo.save(entity);
    }


    public HhToken getTokenByHr(User hr) {
        return repo.findByHr(hr)
                .orElseThrow(() -> new RuntimeException("HR не авторизовался"));
    }

    public boolean hasTokenForHr(Long hrId) {
        return repo.findByHrId(hrId).isPresent();
    }

    public HhToken getTokenByHrId(Long hrId) {
        User hr = userService.getById(hrId);
        return repo.findByHr(hr)
                .orElseThrow(() -> new RuntimeException("HR не авторизовался"));
    }

}

