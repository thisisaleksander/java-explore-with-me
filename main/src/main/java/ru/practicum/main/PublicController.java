package ru.practicum.main;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.service.CategoryService;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.compilation.service.CompilationService;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.event.service.EventService;
import ru.practicum.main.event.dto.EventDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PublicController {
    CompilationService compilationService;
    CategoryService categoryService;
    EventService eventService;

    /* testing response
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from main";
    }

     */

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                @RequestParam(value = "pinned", defaultValue = "false") boolean pinned) {
        log.info("GET compilations request");
        return compilationService.getCompilations(from, size, pinned);
    }

    @GetMapping("/compilations/{compilationId}")
    public CompilationDto getCompilation(@PathVariable Long compilationId) {
        log.info("GET compilation by id = {} request", compilationId);
        return compilationService.getCompilation(compilationId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                           @RequestParam(value = "pinned", defaultValue = "false") boolean unique) {
        log.info("GET categories request");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{categoryId}")
    public CategoryDto getCategory(@PathVariable Long categoryId) {
        log.info("GET category by id = {} request", categoryId);
        return categoryService.getCategory(categoryId);
    }

    @GetMapping("/events")
    public List<EventDto> getEvents(@RequestParam(name = "text", defaultValue = " ") String text,
                                    @RequestParam(value = "categories", defaultValue = "") List<Long> categories,
                                    @RequestParam(value = "paid", required = false) String paid,
                                    @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                    @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                    @RequestParam(value = "onlyAvailable", required = false) String onlyAvailable,
                                    @RequestParam(value = "sort",  defaultValue = "EVENT_DATE") String sort,
                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                    HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        log.info("GET events by parameters request");
        log.info("IP: {}", ip);
        log.info("path: {}", uri);
        return eventService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, uri, ip);
    }

    @GetMapping("/events/{eventId}")
    public EventDto getEvent(@PathVariable Long eventId, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        log.info("GET event by id = {} request", eventId);
        log.info("IP: {}", ip);
        log.info("path: {}", uri);
        return eventService.getEventPublic(eventId, uri, ip);
    }
}
