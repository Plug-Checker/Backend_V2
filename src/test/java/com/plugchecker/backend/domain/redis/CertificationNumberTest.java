package com.plugchecker.backend.domain.redis;

import com.plugchecker.backend.IntegrationTest;
import com.plugchecker.backend.domain.auth.domain.CertificationNumber;
import com.plugchecker.backend.domain.auth.domain.CertificationNumberRepository;
import com.plugchecker.backend.global.error.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CertificationNumberTest extends IntegrationTest {

    @Autowired
    private CertificationNumberRepository certificationNumberRepository;

    @BeforeEach
    void clear() {
        certificationNumberRepository.deleteAll();
    }

    @Test
    void makeCertificationNumber_Test() {
        // when
        CertificationNumber certificationNumber1 = new CertificationNumber();
        CertificationNumber certificationNumber2 = new CertificationNumber(null, "xxxxxxx@gmail.com", "9999", "testUserId", "testUserPassword", 3000L);
        certificationNumberRepository.save(certificationNumber2);
        CertificationNumber certificationNumber3 = CertificationNumber.builder()
                .email("yyyyyyy@gmail.com")
                .number("8888")
                .user_id("testUserId2")
                .user_password("testUserPassword2")
                .exp(30000L)
                .build();
        certificationNumberRepository.save(certificationNumber3);

        // then
        CertificationNumber findCertificationNumber1 = certificationNumberRepository.findByEmail("xxxxxxx@gmail.com")
                .orElseThrow(()-> new NotFoundException("xxxxxxx@gmail.com"));
        CertificationNumber findCertificationNumber2 = certificationNumberRepository.findByEmail("yyyyyyy@gmail.com")
                .orElseThrow(()-> new NotFoundException("yyyyyyy@gmail.com"));

        Assertions.assertNotNull(certificationNumber1);

        Assertions.assertNotNull(findCertificationNumber1.getId());
        Assertions.assertEquals(certificationNumber2.getEmail(),findCertificationNumber1.getEmail());
        Assertions.assertEquals(certificationNumber2.getNumber(),findCertificationNumber1.getNumber());
        Assertions.assertEquals(certificationNumber2.getUser_id(),findCertificationNumber1.getUser_id());
        Assertions.assertEquals(certificationNumber2.getUser_password(),findCertificationNumber1.getUser_password());
        Assertions.assertEquals(certificationNumber2.getExp(),findCertificationNumber1.getExp());

        Assertions.assertNotNull(findCertificationNumber2.getId());
        Assertions.assertEquals(certificationNumber3.getEmail(),findCertificationNumber2.getEmail());
        Assertions.assertEquals(certificationNumber3.getNumber(),findCertificationNumber2.getNumber());
        Assertions.assertEquals(certificationNumber3.getUser_id(),findCertificationNumber2.getUser_id());
        Assertions.assertEquals(certificationNumber3.getUser_password(),findCertificationNumber2.getUser_password());
        Assertions.assertEquals(certificationNumber3.getExp(),findCertificationNumber2.getExp());
    }

    @Test
    void updateExp_Test(){
        // given
        CertificationNumber certificationNumber = CertificationNumber.builder()
                .email("xxxxxxx@gmail.com")
                .number("9999")
                .user_id("testUserId")
                .user_password("testUserPassword")
                .exp(30000L)
                .build();

        // when
        certificationNumber.updateExp(40000L);
        certificationNumberRepository.save(certificationNumber);

        // then
        CertificationNumber findCertificationNumber = certificationNumberRepository.findByEmail("xxxxxxx@gmail.com")
                .orElseThrow(()-> new NotFoundException("xxxxxxx@gmail.com"));

        Assertions.assertEquals(certificationNumber.getExp(), findCertificationNumber.getExp());
    }

    @Test
    void timeExtinction_Test() throws InterruptedException {
        // given
        CertificationNumber certificationNumber = CertificationNumber.builder()
                .email("xxxxxxx@gmail.com")
                .number("9999")
                .user_id("testUserId")
                .user_password("testUserPassword")
                .exp(1L)
                .build();
        certificationNumberRepository.save(certificationNumber);

        // when
        TimeUnit.SECONDS.sleep(certificationNumber.getExp()+10L);

        // then
        Optional<CertificationNumber> findCertificationNumber = certificationNumberRepository.findByEmail("xxxxxxx@gmail.com") ;

        Assertions.assertTrue(findCertificationNumber.isEmpty());

    }

    @Test
    void saveCertificationNumber_Test() {
        // given
        String email = "xxxxxxx@gmail.com";
        String number = "9999";
        String userId = "testUserId";
        String userPassword = "testUserPassword";

        // when
        certificationNumberRepository.saveCertificationNumber(email, number, userId, userPassword);

        // then
        CertificationNumber findCertificationNumber = certificationNumberRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException(email));

        Assertions.assertNotNull(findCertificationNumber.getId());
        Assertions.assertEquals(email,findCertificationNumber.getEmail());
        Assertions.assertEquals(number,findCertificationNumber.getNumber());
        Assertions.assertEquals(userId,findCertificationNumber.getUser_id());
        Assertions.assertEquals(userPassword,findCertificationNumber.getUser_password());
    }
}
