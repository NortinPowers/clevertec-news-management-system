package by.clevertec.gateway.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.aspect.ControllerAspectLogger;
import by.clevertec.auth.JwtRequest;
import by.clevertec.auth.JwtResponse;
import by.clevertec.auth.UserRegistrationDto;
import by.clevertec.gateway.client.AuthServiceClient;
import by.clevertec.message.BaseResponse;
import by.clevertec.message.ExceptionResponse;
import by.clevertec.message.ErrorValidationResponse;
import by.clevertec.message.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Auth management API")
public class AuthController {

    private final AuthServiceClient authServiceClient;

    @ControllerAspectLogger
    @PostMapping("/token")
//    @PreAuthorize("isAnonymous()")
    @Operation(
            summary = "Create user token",
            description = "Get a jwt-token by credentials. The response is jwt-token",
            tags = "post"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = JwtRequest.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody @Valid JwtRequest request){
        return authServiceClient.createAuthToken(request);
    }

    @ControllerAspectLogger
    @PostMapping("/registration")
    @Operation(
            summary = "Create new user",
            description = "Create new user. The response is a message about the successful creation of a user",
            tags = "post"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SuccessResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> createNewUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto){
        return authServiceClient.createNewUser(userRegistrationDto);
    }
}
