package main.java.repositories;

import main.java.models.StatusOfTask;
import main.java.models.Task;
import org.w3c.dom.Document;

import java.util.List;

public interface Repository {

    Document loadDocument();
    void saveDocument(Document document);
    Task create(Task task);
    List<Task> selectAll();
    List<Task> selectByStatus(List<Task> tasks, StatusOfTask statusOfTask);
    void edit(Task task);
    boolean delete(int id);
    boolean complete(int id);
    Task findTaskById(int id);
}
