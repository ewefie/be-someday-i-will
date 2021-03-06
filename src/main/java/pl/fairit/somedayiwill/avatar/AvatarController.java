package pl.fairit.somedayiwill.avatar;

import io.swagger.annotations.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.fairit.somedayiwill.security.user.CurrentUser;
import pl.fairit.somedayiwill.security.user.UserPrincipal;
import pl.fairit.somedayiwill.user.AppUserService;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;

@RestController
@RequestMapping("/users/me/avatar")
@Api(value = "Avatar resource")
public class AvatarController {

    private final AvatarService avatarService;
    private final AppUserService appUserService;

    public AvatarController(final AvatarService avatarService, final AppUserService appUserService) {
        this.avatarService = avatarService;
        this.appUserService = appUserService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get your avatar", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved an image"),
            @ApiResponse(code = 401, message = "You are not authorized to access the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<ByteArrayResource> getAvatar(@ApiIgnore @CurrentUser final UserPrincipal userPrincipal) {
        var userAvatar = avatarService.getUsersAvatar(userPrincipal.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(userAvatar.getFileType()))
                .body(new ByteArrayResource(userAvatar.getData()));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Upload an avatar")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Image successfully uploaded"),
            @ApiResponse(code = 401, message = "You are not authorized to access the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<ByteArrayResource> uploadAvatar(@ApiParam(value = "File to save or update", required = true) @RequestParam("file") final MultipartFile file,
                                                          @ApiIgnore @CurrentUser final UserPrincipal userPrincipal) {
        var existingUser = appUserService.getExistingUser(userPrincipal.getId());
        var userAvatar = avatarService.saveAvatar(file, existingUser);
        return ResponseEntity.created(URI.create(""))
                .contentType(MediaType.parseMediaType(userAvatar.getFileType()))
                .body(new ByteArrayResource(userAvatar.getData()));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete your avatar")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Image successfully deleted"),
            @ApiResponse(code = 401, message = "You are not authorized to access the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public void deleteAvatar(@ApiIgnore @CurrentUser final UserPrincipal userPrincipal) {
        avatarService.deleteUsersAvatar(userPrincipal.getId());
    }
}
