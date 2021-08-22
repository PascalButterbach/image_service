package de.chronies.image_service.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptEncryptor {

    //    encryptor.setPassword("passwort-to-encrypt");
    //    String encrypted = encryptor.encrypt("privatedata");
    @Bean
    public StandardPBEStringEncryptor standardPBEStringEncryptor(){
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        return encryptor;
    }

}
