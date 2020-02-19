package br.com.vaneli.api.services;

public interface CryptoService {

  String encryptAES256(String strToEncrypt);

  String decryptAES256(String strToDecrypt);

}
