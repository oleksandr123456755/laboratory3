package org.fpm.di;

public class DummyEnvironment implements Environment {

    @Override
    public Container configure(Configuration configuration) {
        return new DummyContainer(configuration);
    }
}
