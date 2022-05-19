package project.diploma.agreement.service;

import project.diploma.agreement.domain.Module;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.dto.SaveModuleDto;
import project.diploma.agreement.dto.UpdateModuleDto;

public interface ModuleService {

    void save(Module module);
    MessageResponseDto toMoodleEntity(SaveModuleDto saveModuleDto) throws InterruptedException;
    MessageResponseDto updateModule (UpdateModuleDto updateModuleDto) throws InterruptedException;
    Module findById(Integer id);
}
