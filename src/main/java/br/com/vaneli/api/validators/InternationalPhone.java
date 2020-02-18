package br.com.vaneli.api.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Constraint(validatedBy = InternationlPhoneValidator.class)
public @interface InternationalPhone {

  String message() default "format must be a valid international phone";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
