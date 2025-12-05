package com.ds.DataStructure;

import com.ds.DataStructure.Mappers.BaseMapper;
import com.ds.Entities.BaseEntity;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager<T extends BaseEntity> {

    private File file;
    public List<T> entities;
    private BaseMapper<T> mapper;

    public FileManager(String filePath, BaseMapper<T> mapper) {
        file = new File(filePath);
        entities = new ArrayList<>();
        this.mapper = mapper;
    }

    public void readFile() {
        try {
            FileReader fileReader = new FileReader(file);

            Scanner scanner = new Scanner(fileReader);

            while (scanner.hasNextLine()) {
                String row =  scanner.nextLine();
                if(!row.equals("")) {
                    T entity = this.mapper.toEntity(row);
                    entities.add(entity);
                }
            }

            fileReader.close();

        } catch (Exception e) {
            System.out.println("Error in reading file");
            e.printStackTrace();
        }
    }

    public void saveFile() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            for (T entity : entities) {
                fileWriter.write(mapper.toString(entity) + "\n");
            }
            fileWriter.close();
        }
        catch (Exception e) {
            System.out.println("Error in saving file");
        }
    }
}
