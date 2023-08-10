package ru.practicum.main;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.service.CategoryService;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.compilation.service.CompilationService;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationRequest;
import ru.practicum.main.event.service.EventService;
import ru.practicum.main.event.dto.EventDto;
import ru.practicum.main.event.dto.UpdateEventAdminRequest;
import ru.practicum.main.user.service.UserService;
import ru.practicum.main.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminController {
    CategoryService categoryService;
    EventService eventService;
    UserService userService;
    CompilationService compilationService;

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("POST category request");
        return new ResponseEntity<>(categoryService.saveCategory(categoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long categoryId) {
        log.info("DELETE category with id = {} request", categoryId);
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/categories/{categoryId}")
    public CategoryDto patchCategory(@RequestBody @Valid CategoryDto categoryDto,
            @PathVariable Long categoryId) {
        log.info("PATCH category with id = {} request", categoryId);
        return categoryService.patchCategory(categoryDto, categoryId);
    }

    @GetMapping("/events")
    public List<EventDto> getEvents(@RequestParam(name = "users", defaultValue = "") List<Long> users,
                                    @RequestParam(name = "states", defaultValue = "") List<String> states,
                                    @RequestParam(value = "categories", defaultValue = "") List<Long> categories,
                                    @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                    @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("GET events by parameters request");
        return eventService.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventDto patchEvent(@RequestBody @Valid  UpdateEventAdminRequest updateEventAdminRequest,
                               @PathVariable Long eventId) {
        log.info("PATCH event with id = {} request", eventId);
        return eventService.patchEventAdmin(updateEventAdminRequest, eventId);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(name = "ids", defaultValue = "") List<Long> users,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("GET users request");
        return userService.getUsers(users, from, size);
    }


    @PostMapping("/users")
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserDto userDto) {
        log.info("POST user request");
        return new ResponseEntity<>(userService.saveUser(userDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long userId) {
        log.info("DELETE user with id = {} request", userId);
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/compilations")
    public ResponseEntity<CompilationDto> saveCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("POST compilation request");
        return new ResponseEntity<>(compilationService.saveCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/compilations/{compilationId}")
    public ResponseEntity<CompilationDto> deleteCompilation(@PathVariable Long compilationId) {
        log.info("DELETE compilation with id = {} request", compilationId);
        compilationService.deleteCompilation(compilationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/compilations/{compilationId}")
    public CompilationDto patchCompilation(@RequestBody @Valid UpdateCompilationRequest updateCompilationRequest,
                                  @PathVariable Long compilationId) {
        log.info("PATCH compilation with id = {} request", compilationId);
        return compilationService.patchCompilation(updateCompilationRequest, compilationId);
    }

}
