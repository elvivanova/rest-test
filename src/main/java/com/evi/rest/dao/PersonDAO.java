package com.evi.rest.dao;

import com.evi.rest.model.Person;
import com.evi.rest.model.Selected;

import java.util.List;

public interface PersonDAO {

	void addPerson(Person p);

	void updatePerson(Person p);

	List<Person> listPersons();

	Person getPersonById(int id);

	void removePerson(int id);

	List<String> processPersons(Selected selected);
}
