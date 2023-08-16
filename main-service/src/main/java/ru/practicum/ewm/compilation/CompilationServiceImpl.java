package ru.practicum.ewm.compilation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.model.*;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CompilationServiceImpl implements CompilationService {

    CompilationRepository compilationRepository;

    EventService eventService;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventService.findByIds(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toEntityFromNewComp(newCompilationDto, events);
        return CompilationMapper.toDtoFromEntity(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto patchCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId) {
        List<Event> events = eventService.findByIds(updateCompilationRequest.getEvents());
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found"));
        if (events != null) {
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        return CompilationMapper.toDtoFromEntity(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> getCompilations(Integer from, Integer size, boolean pinned) {
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        return CompilationMapper.toListDtoFromListEntity(compilationRepository.findByPinned(pinned, page).toList());
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        return CompilationMapper.toDtoFromEntity(compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found")));
    }
}
