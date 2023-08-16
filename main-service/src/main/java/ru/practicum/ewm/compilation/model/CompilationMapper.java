package ru.practicum.ewm.compilation.model;

import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.model.Event;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CompilationMapper {
    public static Compilation toEntityFromNewComp(NewCompilationDto newCompilationDto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setPinned(newCompilationDto.getPinned());
        if (newCompilationDto.getPinned() == null) {
            compilation.setPinned(false);
        }
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public static CompilationDto toDtoFromEntity(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(compilation.getEvents());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public static List<CompilationDto> toListDtoFromListEntity(List<Compilation> listEntity) {
        List<CompilationDto> listDto = new ArrayList<>();
        for (Compilation compilation:listEntity
        ) {
            listDto.add(toDtoFromEntity(compilation));
        }
        return listDto;
    }
}
