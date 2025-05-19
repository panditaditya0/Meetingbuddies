package com.meetingBuddy.meetingBuddy.service;

import com.github.javafaker.Faker;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class NamingService {

    public ArrayList<String> getNameByLocale(String locale, long count){
        Faker faker = new Faker(new Locale(locale));
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            names.add(faker.name().fullName());
        }
        return names;
    }
}
