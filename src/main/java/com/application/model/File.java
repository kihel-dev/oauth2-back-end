package com.application.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a file entity stored in MongoDB.
 * 
 * This entity contains information about the file, such as its name, type,
 * binary data, and the associated user who uploaded the file.
 */
@Document(collection = "files") // Indicates that this class is a MongoDB document and specifies the collection name
@Data
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates a constructor with all fields
public class File {

    @Id
    private String id; // Unique identifier for the file

    private String name; // The name of the file

    private String type; // The MIME type of the file (e.g., "image/png")

    private byte[] data; // The binary data of the file

    @DBRef // Indicates that this field references another document (User)
    private User user; // The user associated with this file

    /**
     * Constructor to create a new File instance.
     *
     * @param name the name of the file
     * @param type the MIME type of the file
     * @param data the binary data of the file
     * @param user the user who uploaded the file
     */
    public File(String name, String type, byte[] data, User user) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.user = user;
    }
    
    /**
     * Constructor to create a File instance with an id, name, and type.
     *
     * @param id the unique identifier for the file
     * @param name the name of the file
     * @param type the MIME type of the file
     */
    public File(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
