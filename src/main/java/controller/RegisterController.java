package controller;

import jakarta.servlet.http.HttpServletRequest;
import model.dto.AllUsersDTO;
import model.dto.OrgDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import service.FileUpload.UploadService;
import service.Register.RegisterAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.SignUp.Register_OrgAccount;

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
            registerAccount.createAccount(allUsersDTO);
        }

        return ResponseEntity.ok(Map.of("message", response));
    }

}