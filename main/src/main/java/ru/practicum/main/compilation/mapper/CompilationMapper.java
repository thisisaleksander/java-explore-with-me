package ru.practicum.main.compilation.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.main.compilation.Compilation;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.event.Event;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CompilationMapper {
    public static Compilation mapToCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setPinned(newCompilationDto.getPinned());
        if (newCompilationDto.getPinned() == null) {
            compilation.setPinned(false);
        }
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(compilation.getEvents());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public static List<CompilationDto> mapToMultipleCompilationDto(List<Compilation> listEntity) {
        List<CompilationDto> listDto = new ArrayList<>();
        for (Compilation compilation : listEntity) {
            listDto.add(mapToCompilationDto(compilation));
        }
        return listDto;
    }
}
