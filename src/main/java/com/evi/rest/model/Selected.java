package com.evi.rest.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Selected {
    private static final Logger LOG = LoggerFactory.getLogger(Selected.class);

    private String personMaxNumber;
    private int PERSON_MAX_NUMBER=100;

    private int count;

    private Integer[] selectedIds = new Integer[getMaximumPersonNumber()];

    private Integer[] selIds;

    private List<String> resultMessages;

    private Lock lock;
    private Condition completed;

    public Integer[] getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Integer[] selectedIds) {
        this.selectedIds = selectedIds;
    }

    public Integer[] getSelIds() {
        return selIds;
    }

    public List<String> getResultMessages() {
        return resultMessages;
    }

    public void setPersonMaxNumber(String personMaxNumber) {
        this.personMaxNumber = personMaxNumber;
    }

    public void init() {
        int i = 0;                                  // count selected ids
        for (Integer id : selectedIds) {
            if (id != null) {
                i++;
            }
        }

        count = i;                                   // init count
        selIds = new Integer[i];                     // init selIds
        i = 0;
        for (Integer id : selectedIds) {
            if (id != null) {
                selIds[i++] = id;
            }
        }

        resultMessages = new ArrayList<>(count);    // init resultMessages Array

        lock = new ReentrantLock();                 // init lock
        completed  = lock.newCondition();
    }


    public void addMessage(String message) {
        LOG.debug("SetMessage: acquire lock");
        lock.lock();
        LOG.debug("SetMessage: lock acquired,  Count = " + count);

        resultMessages.add(message);

        if (count > 0 && --count == 0) {

            LOG.debug("Count = 0, completed.signal() is invoked");
            completed.signal();
        }

        lock.unlock();
        LOG.debug("SetMessage: lock released");
    }


    public void waitForCount() {
        LOG.debug("WaitForCount called");
        lock.lock();
        LOG.debug("Lock acquired");

        while (count > 0 ) {
            try {
                LOG.debug("Waiting for completed signal");
                completed.await();
                LOG.debug("Completed signalled, count = " + count);

            } catch (InterruptedException e) {
                LOG.info("Awaiting interrupted " + e.getMessage());
            }
        }

        lock.unlock();
        LOG.debug("___COMPLETED___");
    }


    private int getMaximumPersonNumber() {
        int i = PERSON_MAX_NUMBER;
        try {
            i = Integer.parseInt(personMaxNumber);
        } catch (NumberFormatException e) {
            LOG.info("Max person id property was incorrect");
        }
        LOG.info("Maximum person id is " + i);
        return i;
    }
}

