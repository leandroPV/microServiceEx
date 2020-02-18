package br.com.vaneli.api.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Validator for E.164 format (ITU-T)
 * <p>
 * E.164 defines a general format for international telephone numbers.
 */
public class InternationlPhoneValidator implements ConstraintValidator<InternationalPhone, String> {


  public static final String REGEX_INTERNATIONAL_PHONE = "^\\+(?:[0-9]){6,14}[0-9]$";


  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.isEmpty(value) || value.matches(REGEX_INTERNATIONAL_PHONE);
  }
}
