package main.java.factories;

import main.java.repositories.Repository;

public interface RepositoryFactory {
    Repository createRepository();
}
