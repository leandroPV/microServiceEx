package br.com.vaneli.api.interfaces;

public interface Messages {

  // 400
  String FIELD_VALIDATION = "400.001";
  String JSON_VALIDATION = "400.002";
  String REQUIRED_PARAM = "400.003";
  String INVALID_PARAM = "400.004";
  String REQUIRED_REQUEST_BODY = "400.005";
  String INVALID_FIELD = "400.006";
  String REQUIRED_HEADER = "400.007";

  // 404
  String USER_NOT_FOUND = "404.001";
  String CONTACT_NOT_FOUND = "404.002";

  // 422
  String CONTACT_SYSTEM_ADMIN = "422.001";
  String AES_ENCRYPT_ERROR = "422.002";
  String AES_DECRYPT_ERROR = "422.003";

}
