package main.java.factories;

import main.java.repositories.Repository;
import main.java.repositories.XMLRepository;

import java.io.File;

public class XMLRepositoryFactory implements RepositoryFactory {
    File inputFile;

    public XMLRepositoryFactory(File inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    public Repository createRepository() {
        return new XMLRepository(inputFile);
    }
}
