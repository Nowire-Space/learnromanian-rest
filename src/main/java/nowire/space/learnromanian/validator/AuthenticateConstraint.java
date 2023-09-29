package nowire.space.learnromanian.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.ANNOTATION_TYPE,ElementType.FIELD,ElementType.METHOD,ElementType.TYPE})
@Constraint(validatedBy = AuthenticateValidator.class)
public @interface AuthenticateConstraint {
    String message () default "User can't be authenticated !";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

}
