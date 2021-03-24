package pl.makary.service.impl;

import org.springframework.stereotype.Service;
import pl.makary.entity.Section;
import pl.makary.repository.SectionRepository;
import pl.makary.service.SectionService;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;

    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean existsByName(String name) {
        return sectionRepository.existsByName(name);
    }

    @Override
    public Section findByName(String sectionName) {
        return sectionRepository.findByName(sectionName);
    }
}
