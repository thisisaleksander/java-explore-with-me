package ru.practicum.ewm.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.comment.service.CommentService;
import ru.practicum.ewm.comment.dto.ParticipationCommentDto;
import ru.practicum.ewm.comment.model.StatusUpdateComment;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.UpdateCompilationRequest;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.UpdateEventAdminRequest;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(path = "/admin")
@Validated
public class AdminController {
    CategoryService categoryService;

    EventService eventService;

    UserService userService;

    CompilationService compilationService;

    CommentService commentService;

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody @Valid CategoryDto categoryDto) {

        return new ResponseEntity<CategoryDto>(categoryService.saveCategory(categoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto patchCategory(@RequestBody @Valid CategoryDto categoryDto,
                                     @PathVariable Long catId) {
        return categoryService.patchCategory(categoryDto, catId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(name = "users", defaultValue = "") List<Long> users,
                                        @RequestParam(name = "states", defaultValue = "") List<String> states,
                                        @RequestParam(value = "categories", defaultValue = "") List<Long> categories,
                                        @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                        @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get events from={}, size={}, users={}, states={}, categories={}, rangeStart={}, rangeEnd={}", from, size, users, states, categories, rangeStart, rangeEnd);
        return eventService.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto patchEvent(@RequestBody @Valid  UpdateEventAdminRequest updateEventAdminRequest,
                                   @PathVariable Long eventId) {
        return eventService.patchEventAdmin(updateEventAdminRequest, eventId);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(name = "ids", defaultValue = "") List<Long> users,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get users from={}, size={}", from, size);
        return userService.getUsers(users, from, size);
    }


    @PostMapping("/users")
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<UserDto>(userService.saveUser(userDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/compilations")
    public ResponseEntity<CompilationDto> saveCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return new ResponseEntity<CompilationDto>(compilationService.saveCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto patchCompilation(@RequestBody @Valid UpdateCompilationRequest updateCompilationRequest,
                                           @PathVariable Long compId) {
        return compilationService.patchCompilation(updateCompilationRequest, compId);
    }

    @GetMapping("comments")
    public List<ParticipationCommentDto> getCommentsAdmin(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                          @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get comments for admin");
        return commentService.getCommentsAdmin(from, size);
    }


    @PatchMapping("/comments/{commentId}")
    public ParticipationCommentDto patchCommentAdmin(@RequestBody @Valid StatusUpdateComment statusUpdateComment,
                                                     @PathVariable Long commentId) {
        log.info("Publish or reject comment with ID={}", commentId);
        return commentService.patchCommentsAdmin(statusUpdateComment, commentId);
    }
}
