package me.bruceli.cache.service;


import java.util.List;

public interface DemoService {

    public List<String> getAll();

    public String queryById(String id);

    public void add(String id);

    public void delete(String id);
}
