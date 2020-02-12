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
  String TRANSACTION_NOT_FOUND = "404.001";

  // 422
  String CONTACT_SYSTEM_ADMIN = "422.001";
  String EMAILAGE_REQUEST_HAS_FAILED = "422.002";
  String EMAILAGE_SCORE_NOT_FOUND = "422.003";
}
