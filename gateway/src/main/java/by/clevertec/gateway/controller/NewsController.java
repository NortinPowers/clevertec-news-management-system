package by.clevertec.gateway.controller;

import static by.clevertec.gateway.utils.Constants.SECURITY_SWAGGER;
import static by.clevertec.gateway.utils.ControllerUtils.getNewsAndNamePathRequestDtoToUpdate;
import static by.clevertec.gateway.utils.ControllerUtils.getNewsAndNameRequestDtoToCreate;
import static by.clevertec.gateway.utils.ControllerUtils.getNewsAndNameRequestDtoToUpdate;
import static by.clevertec.gateway.utils.ControllerUtils.getNewsWithCommentsProjection;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.aspect.ControllerAspectLogger;
import by.clevertec.gateway.client.CommentServiceClient;
import by.clevertec.gateway.client.NewsServiceClient;
import by.clevertec.gateway.projection.NewsWithCommentsProjection;
import by.clevertec.gateway.projection.impl.NewsWithCommentsProjectionImpl;
import by.clevertec.message.BaseResponse;
import by.clevertec.model.ErrorValidationResponse;
import by.clevertec.model.ExceptionResponse;
import by.clevertec.request.NewsAndNamePathRequestDto;
import by.clevertec.request.NewsAndNameRequestDto;
import by.clevertec.request.NewsPathRequestDto;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/api/news")
@SecurityRequirement(name = SECURITY_SWAGGER)
@Tag(name = "News", description = "News management API")
public class NewsController {

    private final NewsServiceClient newsServiceClient;
    private final CommentServiceClient commentServiceClient;

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
        return newsServiceClient.getAll(pageable);
    }

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
        return newsServiceClient.getById(id);
    }

    @PostMapping
    @ControllerAspectLogger
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    @Operation(
            summary = "Create new news",
            description = "Create new news. Access is restricted. The response is a message about the successful creation of a news",
            tags = "post"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> save(@RequestBody NewsRequestDto news) {
        NewsAndNameRequestDto requestDto = getNewsAndNameRequestDtoToCreate(news);
        return newsServiceClient.save(requestDto);

    }

    @PutMapping("/{id}")
    @ControllerAspectLogger
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    @Operation(
            summary = "Update the news by id",
            description = "Update the news by specifying its id. Access is restricted. The response is a message about the successful update a news",
            tags = "put"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> update(@PathVariable Long id,
                                               @RequestBody NewsRequestDto news) {
        NewsAndNameRequestDto requestDto = getNewsAndNameRequestDtoToUpdate(news);
        return newsServiceClient.update(id, requestDto);

    }

    @PatchMapping("/{id}")
    @ControllerAspectLogger
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    @Operation(
            summary = "Update the news by id",
            description = "Update the news by specifying its id. Access is restricted. The response is a message about the successful update a news",
            tags = "patch"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> updatePath(@PathVariable Long id,
                                                   @RequestBody NewsPathRequestDto news) {
        NewsAndNamePathRequestDto requestDto = getNewsAndNamePathRequestDtoToUpdate(news);
        return newsServiceClient.updatePath(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ControllerAspectLogger
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    @Operation(
            summary = "Delete the news by id",
            description = "Delete the news by specifying its id. Access is restricted. The response is a message about the successful deletion a news",
            tags = "delete"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return newsServiceClient.delete(id, username);
    }

    @ControllerAspectLogger
    @GetMapping("/{id}/comments")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Retrieves the news and page of comments on it depending on the page",
            description = "Displays news and comments on it depending on the page. Access is restricted. The default comment page size is 15 elements. The answer is a news with id, time, title, text and author and an array of comments with id, time, text, username, newsId and author for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = NewsResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<NewsWithCommentsProjection> getCommentsByNewsId(@PathVariable Long id,
                                                                          @Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                                          @PageableDefault(size = 15)
                                                                          @ParameterObject Pageable pageable) {
        ResponseEntity<NewsResponseDto> responseWithNewsResponseDto = newsServiceClient.getById(id);
        ResponseEntity<Page<CommentResponseDto>> responseWithPageOfCommentResponseDtos = commentServiceClient.getAllByNewsId(id, pageable);
        NewsWithCommentsProjectionImpl newsWithCommentsProjection = getNewsWithCommentsProjection(
                Objects.requireNonNull(responseWithNewsResponseDto.getBody()),
                responseWithPageOfCommentResponseDtos.getBody()
        );
        return ResponseEntity.ok(newsWithCommentsProjection);
    }

    @ControllerAspectLogger
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search/{condition}")
    @Operation(
            summary = "Retrieves a page of news from the list of all news found by the search condition depending on the page",
            description = "Collect news from the list of all news according to the search condition. Access is restricted. The default page size is 15 elements. The answer is an array of news with id, time, title, text and author for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = NewsResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<Page<NewsResponseDto>> getPersonSearchResult(@PathVariable String condition,
                                                                       @Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                                       @PageableDefault(size = 15)
                                                                       @ParameterObject Pageable pageable) {
        return newsServiceClient.getPersonSearchResult(condition, pageable);
    }
}
