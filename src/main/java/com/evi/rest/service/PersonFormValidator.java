package com.evi.rest.service;

import com.evi.rest.model.Person;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component("personFormValidator")
public class PersonFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
//        return Person.class.isAssignableFrom(clazz);  // Interrupts app in case several different models posting
        return true;
    }

    @Override
    public void validate(Object o, Errors errors) {

        if (! (o instanceof Person)) {
           throw new AssertionError("Invalid target for Validator");
        }

        Person person = (Person) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty.person.firstName", "First Name is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty.person.lastName", "Last Name is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "middleName", "NotEmpty.person.middleName", "Middle Name is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthDate", "NotEmpty.person.birthDate", "Birth Date is required");

        validateBirthDate(errors);

    }

    private void validateBirthDate(Errors errors) {
        String birthDate = (String)errors.getFieldValue("birthDate");
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        formatter.setLenient(false);
        String[] bd = birthDate.split("\\.");

        if (birthDate.length() > 10 || bd.length != 3 || bd[2].matches("^0+\\d*")) {
            rejectDate(errors);
            return;
        }
        Date date;
        try {
            date = formatter.parse(birthDate);
        } catch (ParseException e) {
            rejectDate(errors);
            return;
        }
        if (date.after(Calendar.getInstance().getTime())) {
            rejectDate(errors);
        }
    }

    private void rejectDate(Errors errors) {
        errors.rejectValue("birthDate", "format.person.birthDate", "Please use dd.MM.yyyy date format");
    }

}
