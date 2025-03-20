package com.pragma.powerup.domain.spi;



public interface IUserFeignPort {
    String getUserRole(Long userId);
    String getUserPhone(Long userId);
    String getUserEmail(Long userId);


}
