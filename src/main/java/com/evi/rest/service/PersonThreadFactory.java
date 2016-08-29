package com.evi.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class PersonThreadFactory implements ThreadFactory {
    private static final Logger LOG = LoggerFactory.getLogger(PersonThreadFactory.class);

    private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

    @Override
    public Thread newThread(Runnable r) {
        Thread t = defaultThreadFactory.newThread(r);
        t.setName("Person-" + t.getName());
        LOG.debug("Starting Person Processing thread = " + t);
        return t;
    }
}
