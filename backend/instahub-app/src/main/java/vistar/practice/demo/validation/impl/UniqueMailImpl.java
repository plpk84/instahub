package vistar.practice.demo.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vistar.practice.demo.dtos.authentication.RegisterDto;
import vistar.practice.demo.repositories.UserRepository;

import vistar.practice.demo.validation.UniqueMail;

@Component
@RequiredArgsConstructor
public class UniqueMailImpl implements ConstraintValidator<UniqueMail, RegisterDto> {
    private final UserRepository userRepository;
    @Override
    public boolean isValid(RegisterDto value, ConstraintValidatorContext context) {
        return !userRepository.existsByEmail(value.getEmail());
    }
}
