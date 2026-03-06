package org.example.flux_mono.exercice14;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("rooms")
public class Room {

    @Id
    private Long id;
    private String name;
    private Boolean available;

    public Room() {
    }

    public Room(Long id, String name, Boolean available) {
        this.id = id;
        this.name = name;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
