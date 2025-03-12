package com.pragma.powerup.domain.spi;

public interface IJwtTokenProviderPort {

    boolean validateToken(String token);
    String getEmailFromToken(String token);
    String getRoleFromToken(String token);
    Long getUserIdFromToken(String token);

}
