package main.java.repositories;

import main.java.models.StatusOfTask;
import main.java.models.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class XMLRepository  implements Repository{
    File inputFile;

    public XMLRepository(File inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    public Document loadDocument() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(inputFile);
            document.getDocumentElement().normalize();
            return document;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке XML документа: " + e.getMessage(), e);
        }
    }

    @Override
    public void saveDocument(Document document) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(inputFile);
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке XML документа: " + e.getMessage(), e);
        }
    }

    @Override
    public Task create(Task task) {
        Document document = loadDocument();

        NodeList taskList = document.getElementsByTagName("Task");
        int maxId = 0;

        //получаем максимальное ID задачи для автоинкремента
        for (int i = 0; i < taskList.getLength(); i++) {
            Element taskElement = (Element) taskList.item(i);
            String idStr = taskElement.getAttribute("id");
            int id = Integer.parseInt(idStr);
            if (id > maxId) {
                maxId = id;
            }
        }

        ++maxId;
        task.setId(maxId);

        Element newTask = document.createElement("Task");
        newTask.setAttribute("id", String.valueOf(task.getId()));
        newTask.setAttribute("caption", task.getCaption());

        Element taskDescription = document.createElement("Description");
        taskDescription.appendChild(document.createTextNode(task.getDescription()));
        newTask.appendChild(taskDescription);

        Element taskPriority = document.createElement("Priority");
        taskPriority.appendChild(document.createTextNode(String.valueOf(task.getPriority())));
        newTask.appendChild(taskPriority);

        Element taskDeadline = document.createElement("Deadline");
        taskDeadline.appendChild(document.createTextNode(String.valueOf(task.getDeadline())));
        newTask.appendChild(taskDeadline);

        task.setStatusOfTask(StatusOfTask.NEW);
        Element taskStatus = document.createElement("Status");
        taskStatus.appendChild(document.createTextNode(String.valueOf(task.getStatusOfTask()).toLowerCase()));
        newTask.appendChild(taskStatus);

        document.getDocumentElement().appendChild(newTask);

        saveDocument(document);
        return task;
    }

    @Override
    public List<Task> selectAll() {
        sortXMLById();
        Document document = loadDocument();

        List<Task> tasks = new ArrayList<>();

        NodeList nodeList = document.getElementsByTagName("Task");

        for (int i = 1; i < nodeList.getLength() + 1; i++) {
            if (findTaskById(i) != null) {
                tasks.add(findTaskById(i));

            } else {
                for (int j = i + 1; j < nodeList.getLength()+10; j++) {
                    if (findTaskById(j) != null) {
                        tasks.add(findTaskById(j));
                    }
                    i = j;
                }
            }
        }



        return tasks;
    }

    @Override
    public List<Task> selectByStatus(List<Task> tasks, StatusOfTask statusOfTask) {
        return tasks.stream()
                .filter(task -> task.getStatusOfTask() == statusOfTask)
                .toList();
    }

    @Override
    public void edit(Task task) {
        Document document = loadDocument();

        NodeList taskList = document.getElementsByTagName("Task");

        for (int i = 0; i < taskList.getLength(); i++) {
            Element taskElement = (Element) taskList.item(i);
            Integer taskId = Integer.parseInt(taskElement.getAttribute("id"));

            if (taskId.equals(task.getId())) {
                if (!task.getCaption().isEmpty()) {
                    taskElement.setAttribute("caption", task.getCaption());
                }


                if (!task.getDescription().isEmpty()) {
                    taskElement.getElementsByTagName("Description").item(0).setTextContent(
                            task.getDescription());
                }


                if (task.getPriority() != 0) {
                    taskElement.getElementsByTagName("Priority").item(0).setTextContent(
                            String.valueOf(task.getPriority()));
                }


                if (task.getDeadline() != null) {
                    taskElement.getElementsByTagName("Deadline").item(0).setTextContent(
                            String.valueOf(task.getDeadline()));
                }


                if (!task.getStatusOfTask().toString().isEmpty()) {
                    taskElement.getElementsByTagName("Status").item(0).setTextContent(String.valueOf(
                            task.getStatusOfTask()).toLowerCase());
                }

                if (task.getCompletionDate() != null) {
                    taskElement.getElementsByTagName("Complete").item(0).setTextContent(
                            String.valueOf(task.getCompletionDate()));
                }
            }
        }

        saveDocument(document);
    }

    @Override
    public boolean delete(int id) {
        Document document = loadDocument();

        Element root = document.getDocumentElement();
        NodeList taskList = document.getElementsByTagName("Task");

        boolean isTaskFound = false;

        for (int i = 0; i < taskList.getLength(); i++) {
            Element task = (Element) taskList.item(i);
            int taskId = Integer.parseInt(task.getAttribute("id"));
            if (taskId == id) {
                root.removeChild(task);
                isTaskFound = true;
                break;
            }
        }

        saveDocument(document);
        return isTaskFound;
    }

    @Override
    public boolean complete(int id) {
        Document document = loadDocument();

        boolean taskFound = false;

        NodeList taskList = document.getElementsByTagName("Task");

        for (int i = 0; i < taskList.getLength(); i++) {
            Element taskElement = (Element) taskList.item(i);
            if (taskElement.getAttribute("id").equals(Integer.toString(id))) {
                NodeList completeList = taskElement.getElementsByTagName("Complete");
                if (completeList.getLength() <= 0) {

                    taskElement.getElementsByTagName("Status").item(0).setTextContent(
                            String.valueOf(StatusOfTask.DONE).toLowerCase());

                    Element taskComplete = document.createElement("Complete");
                    taskComplete.appendChild(document.createTextNode(LocalDate.now().toString()));
                    taskElement.appendChild(taskComplete);

                    document.getDocumentElement().appendChild(taskElement);

                    taskFound = true;
                }
                break;
            }
        }

        saveDocument(document);
        return taskFound;
    }

    private void sortXMLById(){
        Document document = loadDocument();

        NodeList nodeList = document.getElementsByTagName("Task");
        List<Element> taskList = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            taskList.add((Element) nodeList.item(i));
        }

        taskList.sort(Comparator.comparingInt(e -> Integer.parseInt(e.getAttribute("id"))));

        Element newRoot = document.createElement(document.getDocumentElement().getNodeName());

        for (Element task : taskList) {
            Node importedNode = document.importNode(task, true);
            newRoot.appendChild(importedNode);
        }

        Node oldRoot = document.getDocumentElement();
        document.replaceChild(newRoot, oldRoot);

        saveDocument(document);
    }

    @Override
    public Task findTaskById(int id) {
        sortXMLById();
        Document document = loadDocument();

        NodeList taskList = document.getElementsByTagName("Task");

        for (int i = 0; i < taskList.getLength(); i++) {
            Element taskElement = (Element) taskList.item(i);
            Integer taskId = Integer.parseInt(taskElement.getAttribute("id"));

            if (taskId.equals(id)) {
                Task task = new Task(
                        Integer.parseInt(taskElement.getAttribute("id")),
                        taskElement.getAttribute("caption"),
                        taskElement.getElementsByTagName("Description").item(0).getTextContent(),
                        Integer.parseInt(taskElement.getElementsByTagName("Priority").item(0).getTextContent()),
                        LocalDate.parse(taskElement.getElementsByTagName("Deadline").item(0).getTextContent()),
                        StatusOfTask.valueOf(taskElement.getElementsByTagName("Status").item(0).getTextContent().
                                toUpperCase())
                );

                try {
                    task.setCompletionDate(LocalDate.parse(taskElement.getElementsByTagName("Complete").item(0).getTextContent()));
                    return task;
                } catch (NullPointerException e){
                    return task;
                }

            }
        }

        return null;
    }
}
