package controller;

import jakarta.servlet.http.HttpServletRequest;
import model.dto.AllUsersDTO;
import model.dto.ModeratorDto;
import model.dto.OrgDTO;
import model.dto.UsersDto;
import model.entity.AllUsers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import service.FileUpload.UploadService;
import service.Register.RegisterAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.SignUp.Register_ModeratorAccount;
import service.SignUp.Register_OrgAccount;
import service.SignUp.Register_UserAccount;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:9999"})
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private RegisterAccount registerAccount;

    @Autowired
    private Register_OrgAccount register_OrgAccount;

    @Autowired
    private Register_UserAccount registerUserAccount;

    @Autowired
    private Register_ModeratorAccount registerModeratorAccount;


    @Autowired
    private UploadService uploadService;

    @PostMapping("/register")
    public String addAccount(@RequestBody AllUsersDTO allUsersDTO) {
        String response = registerAccount.createAccount(allUsersDTO);
        return response;
    }


    @PostMapping("/registerOrg")
    public ResponseEntity<Map<String, String>> registerOrgWithFile(
            @RequestPart("registrationCopyFile") MultipartFile orgFile,
            @RequestPart("orgData") OrgDTO orgDTO) throws IOException {

        // Save the file
        String fileName = uploadService.getFileName(orgFile);
        String fileType = uploadService.getFileType(orgFile);

        // Attach file info to DTO
        orgDTO.setImageFileName(fileName);
        orgDTO.setFileType(fileType);

        // Save org logic
        String response = register_OrgAccount.createOrg(orgDTO);
        if ("success".equals(response)) {

            //saving the file to Back-End
            uploadService.upload(orgFile,fileName);

            AllUsersDTO allUsersDTO = new AllUsersDTO();
            allUsersDTO.setEmail(orgDTO.getEmail());
            allUsersDTO.setPassword(orgDTO.getPassword());
            allUsersDTO.setRole("organization");
            allUsersDTO.setName(orgDTO.getFname() + " " + orgDTO.getLname());
            allUsersDTO.setStatus(AllUsers.Status.active);
            response =registerAccount.createAccount(allUsersDTO);
        }

        return ResponseEntity.ok(Map.of("message", response));
    }

    @PostMapping("/registerUser")
    public ResponseEntity<Map<String, String>> registerUser(
            @RequestPart("idFront") MultipartFile idFront,
            @RequestPart("idBack") MultipartFile idBack,
            @RequestPart("billImage") MultipartFile billImage,
            @RequestPart("userData") UsersDto usersDto) throws IOException {

        // Save the file
        String idFrontName = uploadService.getFileName(idFront);
        String idBackName = uploadService.getFileName(idBack);
        String billImageName = uploadService.getFileName(billImage);

        // Attach file info to DTO
        usersDto.setIdFront(idFrontName);
        usersDto.setIdBack(idBackName);
        usersDto.setBillImage(billImageName);


        // Save org logic
        String response = registerUserAccount.createUser(usersDto);
        if ("success".equals(response)) {

            //saving the file to Back-End
            uploadService.upload(idFront,idFrontName);
            uploadService.upload(idBack,idBackName);
            uploadService.upload(billImage,billImageName);

            AllUsersDTO allUsersDTO = new AllUsersDTO();
            allUsersDTO.setEmail(usersDto.getEmail());
            allUsersDTO.setPassword(usersDto.getPassword());
            allUsersDTO.setRole("user");
            allUsersDTO.setName(usersDto.getFname() + " " + usersDto.getLname());
            allUsersDTO.setStatus(AllUsers.Status.pending);
            response = registerAccount.createAccount(allUsersDTO);
        }

        return ResponseEntity.ok(Map.of("message", response));
    }

    @PostMapping("/registerModerator")
    public ResponseEntity<Map<String, String>> registerModerator(@RequestBody ModeratorDto moderatorDto) {


        // Save org logic
        String response = registerModeratorAccount.createModerator(moderatorDto);
        if ("success".equals(response)) {

            AllUsersDTO allUsersDTO = new AllUsersDTO();
            allUsersDTO.setEmail(moderatorDto.getEmail());
            allUsersDTO.setPassword(moderatorDto.getPassword());
            allUsersDTO.setRole("moderator");
            allUsersDTO.setName(moderatorDto.getName());
            registerAccount.createAccount(allUsersDTO);
        }

        return ResponseEntity.ok(Map.of("message", response));
    }

}