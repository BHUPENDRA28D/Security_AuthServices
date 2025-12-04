package UberAuthService.auth.Services;


import UberAuthService.auth.Model.RefreshToken;
import UberAuthService.auth.Repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expiry}")
    private int refreshExpiry;

    public RefreshToken createRefreshToken(Long userId){
        //Delete old refresh tokens for user (Optional but good)
        refreshTokenRepository.deleteByUserId(userId);

        RefreshToken refresh = RefreshToken.builder()
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiryDate(new Date(System.currentTimeMillis()+refreshExpiry*1000L))
                .build();

        return refreshTokenRepository.save(refresh);
    }


    public Optional<RefreshToken> validateRefreshToken(String token){

        return refreshTokenRepository.findByToken(token).filter(refreshToken -> refreshToken.getExpiryDate().after(new Date()));
    }
}
