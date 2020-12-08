package com.example.demo;

import javax.persistence.*;
import java.util.List;

@Entity
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @OrderBy("position ASC")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Task> tasks;

    private int position;

    public Stage(String name, int position, List<Task> tasks) {
        this.name = name;
        this.position = position;
        this.tasks = tasks;
    }

    public Stage() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public Stage(int id, String name, List<Task> tasks, int position) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Stage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tasks=" + tasks +
                ", position=" + position +
                '}';
    }
}
