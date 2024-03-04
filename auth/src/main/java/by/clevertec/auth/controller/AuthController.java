package by.clevertec.auth.controller;

import by.clevertec.aspect.ControllerAspectLogger;
import by.clevertec.auth.JwtRequest;
import by.clevertec.auth.JwtResponse;
import by.clevertec.auth.dto.UserRegistrationDto;
import by.clevertec.auth.service.AuthService;
import by.clevertec.auth.service.UserService;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static by.clevertec.auth.utils.Constants.USER;
import static by.clevertec.auth.utils.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.auth.utils.ResponseUtils.getSuccessResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Auth", description = "User`s management API")
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping
    @ControllerAspectLogger
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
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody @Valid JwtRequest request) throws BadCredentialsException {
        String token = authService.getToken(request);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    //    @PostMapping
//    public JwtResponse createAuthToken(@RequestBody @Valid JwtRequest request) throws BadCredentialsException {
//        String token = authService.getToken(request);
//        return new JwtResponse(token);
//    }
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
    public ResponseEntity<BaseResponse> createNewUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        userService.save(userRegistrationDto);
        return ResponseEntity.ok(getSuccessResponse(CREATION_MESSAGE, USER));
    }

//    @GetMapping("/{name}/role")
//    String getUserRoleByName(@PathVariable("name") String name){
//        return userService.getUserByUsername(name).getRole().toString();
//    }

//    @SuppressWarnings("checkstyle:EmptyLineSeparator")
//    @PostMapping("/auth/role")
//    String getUserRoleByName(@RequestParam String name,
//                             @RequestParam String password){
//        JwtRequest jwtRequest = new JwtRequest();
//
//
//    }
}
