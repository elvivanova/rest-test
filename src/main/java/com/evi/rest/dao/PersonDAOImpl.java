package com.evi.rest.dao;

import com.evi.rest.model.Person;
import com.evi.rest.model.Selected;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class PersonDAOImpl implements com.evi.rest.dao.PersonDAO {
	private static final Logger LOG = LoggerFactory.getLogger(PersonDAOImpl.class);

	private int THREAD_MAX_TIME = 1000;
	private String threadMaxTime;

	private SessionFactory sessionFactory;
	private ExecutorService personThreadPoolExecutor;

	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	public void setPersonThreadPoolExecutor(ExecutorService personThreadPoolExecutor) {
		this.personThreadPoolExecutor = personThreadPoolExecutor;
	}

	public void setThreadMaxTime(String threadMaxTime) {
		this.threadMaxTime = threadMaxTime;
	}
	@Override
	public void addPerson(Person p) {
		Assert.notNull(p);
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(p);
		LOG.info("Person saved successfully, Person Details="+p);
	}

	@Override
	public void updatePerson(Person p) {
		Assert.notNull(p);
		Session session = sessionFactory.getCurrentSession();
		Person persisted = getPersonById(p.getId());
		persisted.setFirstName(p.getFirstName());
		persisted.setMiddleName(p.getMiddleName());
		persisted.setLastName(p.getLastName());
		persisted.setBirthDate(p.getBirthDate());
		session.update(persisted);
		LOG.info("Person updated successfully, Person Details="+p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Person> listPersons() {
		Session session = sessionFactory.getCurrentSession();
		List<Person> personsList = session.createQuery("from Person").list();
		for(Person p : personsList){
			LOG.info("Person List:"+ p);
		}
		return personsList;
	}

	@Override
	public Person getPersonById(int id) {
		Assert.notNull(id);
		Session session = sessionFactory.getCurrentSession();
		Person p = (Person) session.load(Person.class, id);
		LOG.info("Person loaded successfully, Person details="+p);
		return p;
	}

	@Override
	public void removePerson(int id) {
		Assert.notNull(id);
		Session session = sessionFactory.getCurrentSession();
		Person p = (Person) session.load(Person.class, id);
		if(null != p){
			session.delete(p);
		}
		LOG.info("Person deleted successfully, person details="+p);
	}

	public List<String> processPersons(Selected selected) {
		Assert.notNull(selected);

		for (Integer id : selected.getSelIds()) {
			try {
				Future callResult = personThreadPoolExecutor.submit(new Runnable() {
					@Override
					public void run() {
						Session session;
						Transaction tx = null;
						try {
							session = sessionFactory.openSession();
							tx = session.beginTransaction();

							Timer timer = new Timer("ThreadInterrupterTimer", false);
							TimerTask task = new TimerTaskInterrupter(Thread.currentThread());

							timer.schedule(task, getThreadMaxTime());

       						handlePersonId(id, session);

							tx.commit();
							session.close();

							selected.addMessage(String.format("Handling of Person id [%s] completed successfully", id));

							timer.cancel();

						} catch (InterruptedException ie) {
							rollbackTransaction(id, tx);
							LOG.debug(formErrorMessage(id, "thread timed out"));
							selected.addMessage(formErrorMessage(id, "thread timed out"));

						} catch (HibernateException he) {
							rollbackTransaction(id, tx);
							LOG.debug(formErrorMessage(id, he.getMessage()), he.getMessage());
							selected.addMessage(formErrorMessage(id, he.getMessage()));
						}
					}
				});

			} catch (RejectedExecutionException re) {
				LOG.debug(formErrorMessage(id, re.getMessage()));
				selected.addMessage(formErrorMessage(id, re.getMessage()));

			}
		}

		selected.waitForCount();

		return selected.getResultMessages();
	}

	private void handlePersonId(Integer id, Session session) throws HibernateException, InterruptedException {
		Person person = (Person) session.load(Person.class, id);

		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date current = Calendar.getInstance().getTime();
		person.setComment(String.format("Обработано [%s]", dateFormat.format(current)));
		person.setUpdateDate(current);

		session.update(person);

/*		if (id == 210) {
			Thread.sleep(2500);
		}*/
	}

	private void rollbackTransaction(Integer id, Transaction tx) {
		if (tx != null) {
			try {
				tx.rollback();
			} catch (HibernateException he) {
				LOG.info(formErrorMessage(id, ""), he.getMessage());
			}
		}
	}

	private String formErrorMessage(Integer id, String message) {
		return String.format("Handling of person id [%s] completed with error: %s", id, message);
	}

	private int getThreadMaxTime() {
		int i = THREAD_MAX_TIME;
		try {
			i = Integer.parseInt(threadMaxTime);
		} catch (NumberFormatException e) {
			LOG.info("ThreadMaxTime property was incorrect");
		}
		LOG.info("ThreadMaxTime is " + i);
		return i;
	}


	private class TimerTaskInterrupter extends TimerTask {

		Thread thread;

		TimerTaskInterrupter(Thread t) {
			this.thread = t;
		}

		public void run() {
			LOG.debug(String.format("_____Thread %s interrupted ", thread));
			thread.interrupt();
		}
	}

}
