package by.clevertec.news.controller;

import static by.clevertec.util.CheckerUtil.checkIllegalArgument;
import static by.clevertec.utils.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.DELETION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.UPDATE_MESSAGE;
import static by.clevertec.utils.ResponseUtils.getSuccessResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.aspect.ControllerAspectLogger;
import by.clevertec.message.BaseResponse;
import by.clevertec.model.ErrorValidationResponse;
import by.clevertec.model.ExceptionResponse;
import by.clevertec.news.domain.News;
import by.clevertec.news.service.NewsService;
import by.clevertec.request.NewsAndNamePathRequestDto;
import by.clevertec.request.NewsAndNameRequestDto;
import by.clevertec.request.NewsPathRequestDto;
import by.clevertec.request.NewsRequestDto;
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
@RequestMapping("/news")
@Tag(name = "News", description = "News management API")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    @ControllerAspectLogger
    @Operation(
            summary = "Retrieves a page of news from the list of all news depending on the page",
            description = "Collect news from the list of all news. Default page size - 15 elements. The answer is an array of news with id, time, title, text and author for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = NewsResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<Page<NewsResponseDto>> getAll(@Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                        @PageableDefault(size = 15)
                                                        @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(newsService.getAll(pageable));
    }


    /**
     * Получает информацию о новости по ее уникальному идентификатору.
     *
     * @param id Уникальный идентификатор новости.
     * @return {@link ResponseEntity} с объектом {@link NewsResponseDto}.
     */
    @GetMapping("/{id}")
    @ControllerAspectLogger
    @Operation(
            summary = "Retrieve the news by id",
            description = "Get news by specifying its id. The response is a news with id, time, title and text ",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = NewsResponseDto.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<NewsResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getById(id));
    }

    /**
     * Сохраняет новую новость на основе предоставленных данных.
     *
     * @param requestDto Объект {@link NewsAndNameRequestDto}, содержащий данные новой новости и имя пользователя.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @PostMapping
    @ControllerAspectLogger
    @Operation(
            summary = "Create new news",
            description = "Create new news. The response is a message about the successful creation of a news",
            tags = "post"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> save(@RequestBody NewsAndNameRequestDto requestDto) {
//    public ResponseEntity<BaseResponse> save(@RequestBody NewsRequestDto news) {
        newsService.save(requestDto);
//        newsService.save(news);
        return ResponseEntity.ok(getSuccessResponse(CREATION_MESSAGE, News.class));
    }

    /**
     * Обновляет данные новости по её уникальному идентификатору.
     *
     * @param id   Уникальный идентификатор новости, которую требуется обновить.
     * @param requestDto Объект {@link NewsAndNameRequestDto}, содержащий обновленные данные новости и имя пользователя.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @PutMapping("/{id}")
    @ControllerAspectLogger
    @Operation(
            summary = "Update the news by id",
            description = "Update the news by specifying its id. The response is a message about the successful update a news",
            tags = "put"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody NewsAndNameRequestDto requestDto) {
//                                               @Valid @RequestBody NewsRequestDto news) {
        newsService.update(id, requestDto);
//        newsService.update(id, news);
        return ResponseEntity.ok(getSuccessResponse(UPDATE_MESSAGE, News.class));
    }

    /**
     * Частично обновляет некоторые данные новости по её уникальному идентификатору.
     *
     * @param id   Уникальный идентификатор новости, которую требуется частично обновить.
     * @param requestDto Объект {@link NewsAndNamePathRequestDto}, содержащий обновленные данные новости и имя пользователя.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     * @throws IllegalArgumentException Если переданный объект {@code null}.
     */
    @ControllerAspectLogger
    @PostMapping("/{id}")
//    @PatchMapping("/{id}")
    @Operation(
            summary = "Update the news by id",
            description = "Update the news by specifying its id. The response is a message about the successful update a news",
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
                                                  @RequestBody NewsAndNamePathRequestDto requestDto) {
//        checkIllegalArgument(news, "Incorrect news data");
        checkIllegalArgument(requestDto.getRequestDto(), "Incorrect news data");
        newsService.updatePath(id, requestDto);
        return ResponseEntity.ok(getSuccessResponse(UPDATE_MESSAGE, News.class));
    }

    /**
     * Удаляет новость по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор новости, который требуется удалить.
     * @param username Имя пользователя запрашивающего метод.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @ControllerAspectLogger
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete the news by id",
            description = "Delete the news by specifying its id. The response is a message about the successful deletion a news",
            tags = "delete"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id,
                                               @RequestBody String username) {
        newsService.delete(id, username);
        return ResponseEntity.ok(getSuccessResponse(DELETION_MESSAGE, News.class));
    }

    @ControllerAspectLogger
    @GetMapping("/search/{condition}")
    @Operation(
            summary = "Retrieves a page of news from the list of all news found by the search condition depending on the page",
            description = "Collect news from the list of all news according to the search condition. The default page size is 15 elements. The answer is an array of news with id, time, title, text and author for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = NewsResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<Page<NewsResponseDto>> getPersonSearchResult(@PathVariable String condition,
                                                                         @Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                                         @PageableDefault(size = 15)
                                                                         @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(newsService.findNewsSearchResult(condition, pageable));
    }
}
