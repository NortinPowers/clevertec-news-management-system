package by.clevertec.auth.controller;

import static by.clevertec.auth.utils.Constants.SECURITY_SWAGGER;
import static by.clevertec.auth.utils.Constants.USER;
import static by.clevertec.auth.utils.ResponseUtils.CHANGE_ROLE_MESSAGE;
import static by.clevertec.auth.utils.ResponseUtils.getSuccessResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.aspect.ControllerAspectLogger;
import by.clevertec.auth.UserDto;
import by.clevertec.auth.service.AdminService;
import by.clevertec.message.BaseResponse;
import by.clevertec.message.ExceptionResponse;
import by.clevertec.message.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "Admin", description = "Admin`s management API")
@SecurityRequirement(name = SECURITY_SWAGGER)
@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
//@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;

    //TODO
    @ControllerAspectLogger
    @PostMapping("/set/{id}")
//    @PatchMapping("/set/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Set the administrator role for the user",
            description = "Update the user role by specifying its id. The response is a message about the successful changed a role",
            //TODO
            tags = "path"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> setAdmin(@PathVariable("id") @Min(1) Long id) {
        adminService.setAdmin(id);
        return ResponseEntity.ok(getSuccessResponse(CHANGE_ROLE_MESSAGE, USER));
    }

    @ControllerAspectLogger
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Retrieve all users",
            description = "Collect all users. The answer is an array of users with identifier, username and role for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }
}
