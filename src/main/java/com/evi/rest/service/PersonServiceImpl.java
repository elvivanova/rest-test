package com.evi.rest.service;

import com.evi.rest.dao.PersonDAO;
import com.evi.rest.model.Person;
import com.evi.rest.model.Selected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

public class PersonServiceImpl implements PersonService {
    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceImpl.class);

	private PersonDAO personDAO;

	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

    @Transactional
    @Override
	public void addPerson(Person p) {
		personDAO.addPerson(p);
	}

	@Transactional
    @Override
	public void updatePerson(Person p) {
		personDAO.updatePerson(p);
	}

	@Transactional
    @Override
	public List<Person> listPersons() {
		List<Person> persons = personDAO.listPersons();
		Collections.sort(persons, (o1, o2) -> o1.getId().compareTo(o2.getId()));
		return persons;
	}

	@Transactional
    @Override
	public Person getPersonById(int id) {
		return personDAO.getPersonById(id);
	}

	@Transactional
    @Override
	public void removePerson(int id) {
		personDAO.removePerson(id);
	}

	@Transactional
    @Override
	public List<String> processPersons(Selected selected) {
		selected.init();

        return personDAO.processPersons(selected);
    }

}
