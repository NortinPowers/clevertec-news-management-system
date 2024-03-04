package by.clevertec.gateway.controller;

import static by.clevertec.gateway.utils.ControllerUtils.getUserDetails;
import static by.clevertec.utils.Constants.SECURITY_SWAGGER;
import static by.clevertec.utils.Constants.USER;
import static by.clevertec.utils.ResponseUtils.CHANGE_ROLE_MESSAGE;
import static by.clevertec.utils.ResponseUtils.getSuccessResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.aspect.ControllerAspectLogger;
import by.clevertec.auth.JwtRequest;
import by.clevertec.auth.JwtResponse;
import by.clevertec.auth.UserDto;
import by.clevertec.auth.UserRegistrationDto;
import by.clevertec.gateway.client.AdminServiceClient;
import by.clevertec.gateway.client.AuthServiceClient;
import by.clevertec.gateway.security.CustomUserDetails;
import by.clevertec.gateway.security.token.JwtTokenManager;
import by.clevertec.message.BaseResponse;
import by.clevertec.message.ErrorValidationResponse;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@SecurityRequirement(name = SECURITY_SWAGGER)
@Tag(name = "Admin", description = "Admin management API")
public class AdminController {

    private final AdminServiceClient adminServiceClient;
    private final JwtTokenManager jwtTokenManager;

    @ControllerAspectLogger
//    @PostMapping("/set/{id}")
    @PatchMapping("/set/{id}")
//    @RequestMapping(value = "/set/{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Set the administrator role for the user",
            description = "Update the user role by specifying its id. Access is restricted. The response is a message about the successful changed a role",
            tags = "post"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> setAdmin(@PathVariable("id") @Min(1) Long id) {
        CustomUserDetails userDetails = getUserDetails();
        String header = "Bearer " + jwtTokenManager.generateJwtToken(userDetails);
        return adminServiceClient.setAdmin(id, header);
    }

    @ControllerAspectLogger
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Retrieve all users",
            description = "Collect all users. Access is restricted. The answer is an array of users with identifier, username and role for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<List<UserDto>> getAllUsers() {
        CustomUserDetails userDetails = getUserDetails();
        String header = "Bearer " + jwtTokenManager.generateJwtToken(userDetails);
        return adminServiceClient.getAllUsers(header);
    }


}
