package ru.sfedu.pKino.model;

import ru.sfedu.pKino.repository.interfaces.CsvConvertable;
import ru.sfedu.pKino.repository.interfaces.XMLConvertable;

public class Employee_type implements CsvConvertable, XMLConvertable {

    private long id;
    private String description;

    public Employee_type(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String convertToCsv() {
        return null;
    }

    @Override
    public String convertToXML() {
        return null;
    }
}
