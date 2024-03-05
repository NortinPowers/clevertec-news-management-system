package by.clevertec.gateway.controller;

import static by.clevertec.gateway.utils.Constants.SECURITY_SWAGGER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.aspect.ControllerAspectLogger;
import by.clevertec.gateway.client.CommentServiceClient;
import by.clevertec.message.BaseResponse;
import by.clevertec.model.ErrorValidationResponse;
import by.clevertec.model.ExceptionResponse;
import by.clevertec.request.CommentPathRequestDto;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.response.CommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/comments")
@SecurityRequirement(name = SECURITY_SWAGGER)
@Tag(name = "Comments", description = "Comment management API")
public class CommentController {

    private final CommentServiceClient commentServiceClient;

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
    ResponseEntity<Page<CommentResponseDto>> getAll(@Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                    @PageableDefault(size = 15)
                                                    @ParameterObject Pageable pageable) {
        return commentServiceClient.getAll(pageable);
    }

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
        return commentServiceClient.getById(id);
    }

    @PostMapping
    @ControllerAspectLogger
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    @Operation(
            summary = "Create new comment",
            description = "Create new comment. Access is restricted. The response is a message about the successful creation of a comment",
            tags = "post"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> save(@RequestBody CommentRequestDto comment) {
        return commentServiceClient.save(comment);

    }

    @PutMapping("/{id}")
    @ControllerAspectLogger
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    @Operation(
            summary = "Update the comment by id",
            description = "Update the comment by specifying its id. Access is restricted. The response is a message about the successful update a comment",
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
                                               @RequestBody CommentRequestDto comment) {
        return commentServiceClient.update(id, comment);

    }

    @ControllerAspectLogger
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    @Operation(
            summary = "Update the comment by id",
            description = "Update the comment by specifying its id. Access is restricted. The response is a message about the successful update a comment",
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
                                                   @RequestBody CommentPathRequestDto comment) {
        return commentServiceClient.updatePath(id, comment);
    }

    @ControllerAspectLogger
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    @Operation(
            summary = "Delete the comment by id",
            description = "Delete the comment by specifying its id. Access is restricted. The response is a message about the successful deletion a comment",
            tags = "delete"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        return commentServiceClient.delete(id);
    }

    @ControllerAspectLogger
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search/{condition}")
    @Operation(
            summary = "Retrieves a page of comments from the list of all comments found by the search condition depending on the page",
            description = "Collect comments from the list of all comments according to the search condition. Access is restricted. The default page size is 15 elements. The answer is an array of comments with id, time, text, username, newsId and author for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CommentRequestDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<Page<CommentResponseDto>> getPersonSearchResult(@PathVariable String condition,
                                                                          @Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                                          @PageableDefault(size = 15)
                                                                          @ParameterObject Pageable pageable) {
        return commentServiceClient.getCommentsSearchResult(condition, pageable);
    }
}
