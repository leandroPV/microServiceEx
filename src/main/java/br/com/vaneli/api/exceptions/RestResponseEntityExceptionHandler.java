package br.com.vaneli.api.exceptions;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import br.com.vaneli.api.exceptions.MessageError.ApiError;
import br.com.vaneli.api.interfaces.Messages;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.lang.annotation.ElementType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String DETAIL = "%s - Detail: %s";
  private MessageError messageError;

  public RestResponseEntityExceptionHandler(MessageError messageError) {
    this.messageError = messageError;
  }

  @ExceptionHandler(value
    = {IllegalArgumentException.class, IllegalStateException.class, DataAccessException.class,
    TransactionException.class})
  public ResponseEntity<Object> handleConflict(RuntimeException ex) {
    ApiError error = messageError.create(Messages.CONTACT_SYSTEM_ADMIN);
    log.error(format(DETAIL, error.toString(), ex.getMessage()), ex);
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(newArrayList(error));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<List<ApiError>> handleConstraintViolationException(
    ConstraintViolationException ex, HttpServletRequest request) {
    List<ApiError> errors = newArrayList();

    ex.getConstraintViolations().forEach(violation -> {
      ConstraintDescriptorImpl constraintDescriptor = (ConstraintDescriptorImpl) violation
        .getConstraintDescriptor();
      PathImpl propertyPath = (PathImpl) violation.getPropertyPath();
      String msg = violation.getMessage();
      String value =
        Objects.nonNull(violation.getInvalidValue()) ? violation.getInvalidValue().toString() : "";
      if (ElementType.PARAMETER.equals(constraintDescriptor.getElementType())) {
        errors.add(messageError
          .create(Messages.INVALID_PARAM, propertyPath.getLeafNode().getName(), value, msg));
      } else {
        errors
          .add(messageError.create(Messages.INVALID_FIELD, propertyPath.toString(), value, msg));
      }
    });
    log.error(format(DETAIL, errors.toString(), ex.getMessage()), ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(value = UnprocessableEntityException.class)
  protected ResponseEntity<List<ApiError>> handleUnprocessableEntityException(
    UnprocessableEntityException ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getErrors());
  }

  @ExceptionHandler(value = NotFoundException.class)
  protected ResponseEntity<List<ApiError>> handleNotFound(NotFoundException ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(newArrayList(ex.getError()));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
    HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<ApiError> errors = newArrayList();
    ex.getBindingResult().getFieldErrors().forEach(fe -> {
      errors.add(messageError.create(Messages.FIELD_VALIDATION,
        format("Field '%s' %s", fe.getField(), fe.getDefaultMessage())));
    });

    if (errors.isEmpty()) {
      errors.add(messageError.create(Messages.CONTACT_SYSTEM_ADMIN));
    }

    log.error(format(DETAIL, errors.toString(), ex.getMessage()), ex);

    return ResponseEntity.status(status).body(errors);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
    HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<ApiError> errors = newArrayList();

    if (Objects.isNull(ex.getCause())) {
      errors.add(messageError.create(Messages.REQUIRED_REQUEST_BODY));
    } else if (ex.getCause() instanceof InvalidFormatException) {
      InvalidFormatException ife = (InvalidFormatException) ex.getCause();
      if (ife.getTargetType() != null && ife.getTargetType().getEnumConstants() != null) {
        errors.add(messageError.create(Messages.FIELD_VALIDATION,
          format("Invalid type for field '%s' - Available values '%s'.",
            ife.getPath().stream().map(
              p -> Objects.nonNull(p.getFieldName()) ? p.getFieldName() : "[" + p.getIndex() + "]")
              .collect(joining(".")),
            Arrays.toString(ife.getTargetType().getEnumConstants()))));
      } else if (Objects.nonNull(ife.getTargetType())) {
        String type;
        if (ife.getTargetType().getSuperclass().equals(Object.class)) {
          type = ife.getTargetType().getSimpleName();
        } else {
          type = ife.getTargetType().getSuperclass().getSimpleName();
        }
        errors.add(messageError.create(Messages.FIELD_VALIDATION,
          format("Invalid type for field '%s' - Type '%s' expected.", ife.getPath().stream().map(
            p -> Objects.nonNull(p.getFieldName()) ? p.getFieldName() : "[" + p.getIndex() + "]")
            .collect(joining(".")), type)));
      } else {
        errors.add(messageError.create(Messages.CONTACT_SYSTEM_ADMIN));
      }
    } else if (ex.getRootCause() instanceof JsonParseException) {
      JsonParseException jpe = (JsonParseException) ex.getRootCause();
      errors.add(messageError.create(Messages.JSON_VALIDATION, jpe.getOriginalMessage()));
    } else if (ex.getRootCause() instanceof MismatchedInputException) {
      MismatchedInputException mtme = (MismatchedInputException) ex.getCause();

      errors.add(messageError.create(Messages.FIELD_VALIDATION,
        format("Invalid type for field '%s' - Type '%s' expected.",
          mtme.getPath().stream().map(
            p -> Objects.nonNull(p.getFieldName()) ? p.getFieldName() : "[" + p.getIndex() + "]")
            .collect(joining(".")),
          mtme.getTargetType().getSimpleName())));
    } else {
      errors.add(messageError.create(Messages.CONTACT_SYSTEM_ADMIN));
    }
    log.error(format(DETAIL, errors.toString(), ex.getMessage()), ex);
    return ResponseEntity.status(status).body(errors);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
    HttpStatus status, WebRequest request) {
    List<ApiError> errors = newArrayList();
    if (ex instanceof MethodArgumentTypeMismatchException) {
      MethodArgumentTypeMismatchException mtme = (MethodArgumentTypeMismatchException) ex;
      if (mtme.getRequiredType().getEnumConstants() != null) {
        /**
         * Validação dos enums no PathParam
         */
        errors.add(messageError.create(Messages.FIELD_VALIDATION,
          format("%s is not a valid '%s' - Available values '%s'.",
            mtme.getValue(),
            mtme.getName(),
            Arrays.toString(mtme.getRequiredType().getEnumConstants()))));
      } else {
        errors.add(messageError.create(Messages.FIELD_VALIDATION,
          format("Invalid type for field '%s' - Type '%s' expected.",
            mtme.getName(),
            mtme.getRequiredType().getSimpleName())));
      }
    } else {
      errors.add(messageError.create(Messages.CONTACT_SYSTEM_ADMIN));
    }
    log.error(format(DETAIL, errors.toString(), ex.getMessage()), ex);
    return ResponseEntity.status(status).body(errors);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
    MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status,
    WebRequest request) {
    List<ApiError> errors = newArrayList();
    errors.add(
      messageError.create(Messages.REQUIRED_PARAM, ex.getParameterName(), ex.getParameterType()));
    log.error(format(DETAIL, errors.toString(), ex.getMessage()), ex);
    return ResponseEntity.status(status).body(errors);
  }

  @Override
  protected ResponseEntity<Object> handleServletRequestBindingException(
    ServletRequestBindingException ex,
    HttpHeaders headers, HttpStatus status,
    WebRequest request) {
    List<ApiError> errors = newArrayList();
    if(ex instanceof MissingRequestHeaderException) {
      MissingRequestHeaderException mrhe = ((MissingRequestHeaderException) ex);
      errors.add(
        messageError.create(Messages.REQUIRED_HEADER, mrhe.getMessage()));
    } else {
      errors.add(messageError.create(Messages.CONTACT_SYSTEM_ADMIN));
    }
    log.error(format(DETAIL, errors.toString(), ex.getMessage()), ex);
    return ResponseEntity.status(status).body(errors);
  }

  @ExceptionHandler(value = HttpServerErrorException.class)
  protected ResponseEntity<List<ApiError>> handleHttpServerErrorException(
    HttpServerErrorException ex) {
    ApiError error = messageError.create(Messages.CONTACT_SYSTEM_ADMIN);
    log.error(format(DETAIL, error.toString(), ex.getMessage()), ex);
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(newArrayList(error));
  }
}
