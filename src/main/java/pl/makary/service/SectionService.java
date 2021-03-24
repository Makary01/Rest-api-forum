package pl.makary.service;


import pl.makary.entity.Section;

public interface SectionService {

    boolean existsByName(String name);

    Section findByName(String sectionName);
}
