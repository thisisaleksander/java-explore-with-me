package ru.practicum.ewm.compilation.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.UpdateCompilationRequest;

import java.util.List;

@Transactional(readOnly = true)
public interface CompilationService {
    @Transactional
    CompilationDto saveCompilation(NewCompilationDto newCompilationDto);

    @Transactional
    void deleteCompilation(Long compId);

    @Transactional
    CompilationDto patchCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId);

    List<CompilationDto> getCompilations(Integer from, Integer size, boolean pinned);

    CompilationDto getCompilation(Long compId);
}
