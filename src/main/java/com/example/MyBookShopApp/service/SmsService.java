package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.SmsCode;
import com.example.MyBookShopApp.data.SmsCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SmsService {

//    @Value("${twilio.ACCOUNT_SID}")
//    private String ACCOUNT_SID;
//
//    @Value("${twilio.ACCOUNT_SID}")
//    private String AUTH_TOKEN;
//
//    @Value("${twilio.ACCOUNT_SID}")
//    private String TWILIO_NUMBER;

    private final SmsCodeRepository smsCodeRepository;

    @Autowired
    public SmsService(SmsCodeRepository smsCodeRepository) {
        this.smsCodeRepository = smsCodeRepository;
    }

    public String sendSecretCodeSms(String contact) {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        String formattedContact = contact.replaceAll("[()-]]", "");
        String generatedCode = generateCode();
//        Message.creator(
//                new PhoneNumber(formattedContact),
//                new PhoneNumber(TWILIO_NUMBER),
//                "Your secret code is: " + generatedCode
//        ).create();
        return generatedCode;
    }

    public String generateCode() {
        //nnn nnn - pattern
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 6) {
            sb.append(random.nextInt(9));
        }
        sb.insert(3, " ");
        return sb.toString();
    }

    public void saveNewCode(SmsCode smsCode) {
        if (smsCodeRepository.findByCode(smsCode.getCode()) == null) {
            smsCodeRepository.save(smsCode);
        }
    }

    public Boolean verifyCode(String code) {
        SmsCode smsCode = smsCodeRepository.findByCode(code);
        return (smsCode != null && !smsCode.isExpired());
    }
}
