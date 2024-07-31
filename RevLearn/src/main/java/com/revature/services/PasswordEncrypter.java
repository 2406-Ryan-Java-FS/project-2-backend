package com.revature.services;

import jakarta.xml.bind.DatatypeConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncrypter {

    public static String encryptPassword(String Password) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        md5.update(Password.getBytes());
        byte[] digested = md5.digest();

        return DatatypeConverter.printHexBinary(digested);


    }
}