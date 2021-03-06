package ru.sfedu.pKino.repository.interfaces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataProviderXml extends IDataProvider {


    private volatile static DataProviderXml dataProviderXml;

    private static Logger log = LogManager.getLogger();

    private DataProviderXml() {
        super();
    }

    public static DataProviderXml getInstance() {

        if (dataProviderXml == null) {
            dataProviderXml = new DataProviderXml();
            FILE_EXTENSION = ".xml";
        }

        return dataProviderXml;
    }

    @Override
    public <T extends Entity> boolean updateRecord(T object, Repository repository) {

        if (object != null) {

            return writeToFile(findAll(repository).stream().map(t -> {
                if (t.compareById(object)) {
                    t.updateFromStrings(object.toStringsArray());
                }
                return t;
            }).collect(Collectors.toList()), getFilePath(repository));

        }

        return false;
    }

    @Override
    public <T extends Entity> List<T> findAll(Repository repository) {

        List<T> list = readFromFile(getFilePath(repository));

        if (list == null) {
            return new ArrayList<>();
        }

        return list;

    }


    public <T extends Entity> boolean saveRecord(T entity, Repository repository) {

        List<T> list = findAll(repository);
        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(entity);

        return writeToFile(list, getFilePath(repository));

    }


    public <T extends Entity> boolean deleteRecord(T object, Repository repository) {


        List<? extends Entity> list = findAll(repository);

        list.remove(object);

        return writeToFile(list, getFilePath(repository));

    }

    private <T extends Entity> boolean writeToFile(List<T> list, String filePath) {

        Serializer serializer = new Persister();

        XmlWrapper xmlWrap = new XmlWrapper(list);

        try (FileWriter fileWriter = new FileWriter(filePath)) {

            serializer.write(xmlWrap, fileWriter);

            return true;

        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }

        return false;

    }

    public <T extends Entity> List<T> readFromFile(String filePath) {


        Serializer serializer = new Persister();

        XmlWrapper wrapper = new XmlWrapper();

        try {
            if (!Files.exists(Paths.get(filePath))) {
                Files.createFile(Paths.get(filePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (FileReader fileReader = new FileReader(filePath)) {

            serializer.read(wrapper, fileReader);

        } catch (Exception e) {

            log.error(e);

            return new ArrayList<>();
        }

        return wrapper.getList();

    }


}
