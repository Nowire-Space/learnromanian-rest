package nowire.space.learnromanian.validator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = CreateAccountValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateAccountConstraint {
String message() default "An error occurred.";
Class<?>[] groups() default { };
Class<? extends Payload>[] payload() default { };


}
