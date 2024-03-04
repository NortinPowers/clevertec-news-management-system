package by.clevertec.comment.controller;

import static by.clevertec.util.CheckerUtil.checkIllegalArgument;
import static by.clevertec.utils.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.DELETION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.UPDATE_MESSAGE;
import static by.clevertec.utils.ResponseUtils.getSuccessResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.aspect.ControllerAspectLogger;
import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.domain.News;
import by.clevertec.comment.service.CommentService;
import by.clevertec.message.BaseResponse;
import by.clevertec.model.ErrorValidationResponse;
import by.clevertec.model.ExceptionResponse;
import by.clevertec.request.CommentAndNamePathRequestDto;
import by.clevertec.request.CommentAndNameRequestDto;
import by.clevertec.request.CommentPathRequestDto;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.CommentResponseDto;
import by.clevertec.response.NewsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Tag(name = "Comment", description = "Comments management API")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @ControllerAspectLogger
    @Operation(
            summary = "Retrieves a page of comments from the list of all comments depending on the page",
            description = "Collect comments from the list of all comments. Default page size - 15 elements. The answer is an array of comments with id, time, text, username, newsId and author for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<Page<CommentResponseDto>> getAll(@Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                           @PageableDefault(size = 15)
                                                           @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(commentService.getAll(pageable));
    }

    /**
     * Получает информацию о комментарии по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор комментария.
     * @return {@link ResponseEntity} с объектом {@link CommentResponseDto}.
     */
    @GetMapping("/{id}")
    @ControllerAspectLogger
    @Operation(
            summary = "Retrieve the comment by id",
            description = "Get comment by specifying its id. The response is a comment with id, time, text and username ",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CommentResponseDto.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<CommentResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getById(id));
    }

    /**
     * Сохраняет новый комментарий на основе предоставленных данных.
     *
     * @param requestDto Объект {@link CommentAndNameRequestDto}, содержащий обновленные данные комментария и имя пользователя.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @PostMapping
    @ControllerAspectLogger
    @Operation(
            summary = "Create new comment",
            description = "Create new comment. The response is a message about the successful creation of a comment",
            tags = "post"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> save(@RequestBody CommentAndNameRequestDto requestDto) {
        commentService.save(requestDto);
        return ResponseEntity.ok(getSuccessResponse(CREATION_MESSAGE, Comment.class));

    }

    /**
     * Обновляет данные комментарии по её уникальному идентификатору.
     *
     * @param id      Уникальный идентификатор комментария, которую требуется обновить.
     * @param requestDto Объект {@link CommentAndNameRequestDto}, содержащий обновленные данные новости.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @PutMapping("/{id}")
    @ControllerAspectLogger
    @Operation(
            summary = "Update the comment by id",
            description = "Update the comment by specifying its id. The response is a message about the successful update a comment",
            tags = "put"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody CommentAndNameRequestDto requestDto) {
        commentService.update(id, requestDto);
        return ResponseEntity.ok(getSuccessResponse(UPDATE_MESSAGE, Comment.class));
    }

    /**
     * Частично обновляет некоторые данные комментария по её уникальному идентификатору.
     *
     * @param id      Уникальный идентификатор комментария, которую требуется частично обновить.
     * @param requestDto Объект {@link CommentAndNamePathRequestDto}, содержащий частично обновленные данные комментария.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     * @throws IllegalArgumentException Если переданный объект {@code null}.
     */
    @PostMapping("/{id}")
//    @PatchMapping("/{id}")
    @ControllerAspectLogger
    @Operation(
            summary = "Update the comment by id",
            description = "Update the comment by specifying its id. The response is a message about the successful update a comment",
            tags = "post"
//            tags = "patch"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> updatePath(@PathVariable Long id,
                                                   @RequestBody CommentAndNamePathRequestDto requestDto) {
        checkIllegalArgument(requestDto, "Incorrect comment data");
        commentService.updatePath(id, requestDto);
        return ResponseEntity.ok(getSuccessResponse(UPDATE_MESSAGE, Comment.class));
    }

    /**
     * Удаляет комментарий по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор комментарийа, который требуется удалить.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @ControllerAspectLogger
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete the comment by id",
            description = "Delete the comment by specifying its id. The response is a message about the successful deletion a comment",
            tags = "delete"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id,
                                               @RequestBody String username) {
        commentService.delete(id, username);
        return ResponseEntity.ok(getSuccessResponse(DELETION_MESSAGE, Comment.class));
    }

    @ControllerAspectLogger
    @GetMapping("/news/{newsId}")
    public ResponseEntity<Page<CommentResponseDto>> getAllByNewsId(@PathVariable Long newsId,
                                                                   @Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                                   @PageableDefault(size = 15)
                                                                   @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(commentService.getAllByNewsId(newsId, pageable));
    }

    @ControllerAspectLogger
    @GetMapping("/search/{condition}")
    @Operation(
            summary = "Retrieves a page of comments from the list of all comments found by the search condition depending on the page",
            description = "Collect comments from the list of all comments according to the search condition. The default page size is 15 elements. The answer is an array of comments with id, time, text, username, newsId and author for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CommentRequestDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<Page<CommentResponseDto>> getPersonSearchResult(@PathVariable String condition,
                                                                       @Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                                       @PageableDefault(size = 15)
                                                                       @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(commentService.findCommentsSearchResult(condition, pageable));
    }
}
