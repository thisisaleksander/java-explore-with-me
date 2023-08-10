package ru.practicum.main.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.main.compilation.Compilation;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationRequest;
import ru.practicum.main.compilation.mapper.CompilationMapper;
import ru.practicum.main.event.service.EventService;
import ru.practicum.main.event.Event;
import ru.practicum.main.exception.model.CompilationNotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CompilationServiceImpl implements CompilationService {
    CompilationRepository compilationRepository;
    EventService eventService;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventService.findByIds(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.mapToCompilation(newCompilationDto, events);
        return CompilationMapper.mapToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        compilationRepository.deleteById(compilationId);
    }

    @Override
    public CompilationDto patchCompilation(UpdateCompilationRequest updateCompilationRequest,
                                           Long compilationId) {
        List<Event> events = eventService.findByIds(updateCompilationRequest.getEvents());
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
        if (events != null) {
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        return CompilationMapper.mapToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> getCompilations(Integer from, Integer size, boolean pinned) {
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        return CompilationMapper.mapToMultipleCompilationDto(
                compilationRepository.findByPinned(pinned, page).toList());
    }

    @Override
    public CompilationDto getCompilation(Long compilationId) {
        return CompilationMapper.mapToCompilationDto(
                compilationRepository.findById(compilationId)
                        .orElseThrow(() -> new CompilationNotFoundException(compilationId)));
    }
}
