package nowire.space.learnromanian.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD,ElementType.PARAMETER})
@Constraint(validatedBy = ValidateAccountValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateAccountConstraint {
    String message() default "This account can't be validated !";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
