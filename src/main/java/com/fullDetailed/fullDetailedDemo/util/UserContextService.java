package com.fullDetailed.fullDetailedDemo.util;

import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserContextService {
    private final UserRepo userRepository;

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Current user not found"));
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
