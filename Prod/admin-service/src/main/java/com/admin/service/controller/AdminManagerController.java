package com.admin.service.controller;

import com.admin.service.client.auth.RoleClient;
import com.admin.service.client.notify.EmailClient;
import com.admin.service.dto.EmailDto;
import com.admin.service.dto.ManagerDto;
import com.admin.service.dto.PageableResponse;
import com.admin.service.service.AdminManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/manager")
@Slf4j
public class AdminManagerController {

        @Autowired
        RoleClient roleClient;
        @Autowired
        AdminManagerService adminManagerService;

        @Autowired
        EmailClient emailClient;

        @GetMapping("/")
        public ResponseEntity<PageableResponse<ManagerDto>> getAllManagers(
                        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
                PageableResponse<ManagerDto> managers = adminManagerService.getAllManagers(pageNumber, pageSize);
                return new ResponseEntity<>(managers, HttpStatus.OK);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ManagerDto> getManagerById(@PathVariable("id") Long managerId) {

                ManagerDto manager = adminManagerService.getManagerById(managerId);
                return new ResponseEntity<>(manager, HttpStatus.OK);

        }

        @GetMapping("/pending-request")
        public ResponseEntity<PageableResponse<ManagerDto>> getPendingRequests(
                        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

                PageableResponse<ManagerDto> pendingRequests = adminManagerService.getAllPendingRequests(pageNumber,
                                pageSize);
                return new ResponseEntity<>(pendingRequests, HttpStatus.OK);

        }

        @PutMapping("/reject/{id}")
        public ResponseEntity<String> rejectPendingRequestById(@PathVariable("id") Long managerId) {

                String response = adminManagerService.rejectPendingRequestById(managerId);
                List<EmailDto> emailDtoList = new ArrayList<>();
                EmailDto emailDto = new EmailDto();
                emailDto.setToEmail("ankit.satyam@publicissapient.com");
                emailDto.setUserId(managerId);
                emailDto.setEmailSubject("Request Rejected");
                emailDto.setEmailBody("<!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<head>\n" +
                                "<title></title>\n" +
                                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                                "<link href=\"https://fonts.googleapis.com/css2?family=Poppins:wght@800&display=swap\" rel=\"stylesheet\">\n"
                                +
                                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                                "<style type=\"text/css\">\n" +
                                "    \n" +
                                "\n" +
                                "body, table, td, a { -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }\n" +
                                "table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }\n" +
                                "img { -ms-interpolation-mode: bicubic; }\n" +
                                "\n" +
                                "img { border: 0; height: auto; line-height: 100%; outline: none; text-decoration: none; }\n"
                                +
                                "table { border-collapse: collapse !important; }\n" +
                                "body { height: 100% !important; margin: 0 !important; padding: 0 !important; width: 100% !important; }\n"
                                +
                                "\n" +
                                "\n" +
                                "a[x-apple-data-detectors] {\n" +
                                "    color: inherit !important;\n" +
                                "    text-decoration: none !important;\n" +
                                "    font-size: inherit !important;\n" +
                                "    font-family: inherit !important;\n" +
                                "    font-weight: inherit !important;\n" +
                                "    line-height: inherit !important;\n" +
                                "}\n" +
                                "\n" +
                                "@media screen and (max-width: 480px) {\n" +
                                "    .mobile-hide {\n" +
                                "        display: none !important;\n" +
                                "    }\n" +
                                "    .mobile-center {\n" +
                                "        text-align: center !important;\n" +
                                "    }\n" +
                                "}\n" +
                                "div[style*=\"margin: 16px 0;\"] { margin: 0 !important; }\n" +
                                "</style>\n" +
                                "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\n"
                                +
                                "\n" +
                                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                                "    <tr>\n" +
                                "        <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\n"
                                +
                                "        \n" +
                                "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:500px;\">\n"
                                +
                                "            <tr>\n" +
                                "                <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#dae5f7\">\n"
                                +
                                "               \n" +
                                "                    <div style=\"display:inline-block;  vertical-align:top; width:100%;\">\n"
                                +
                                "                        <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\n"
                                +
                                "                            <tr>\n" +
                                "                                <a href=\"https://d1j9yqk9s11go2.cloudfront.net/\"><img src=\"./img/logo-removebg-preview.png\" alt=\"\"  width=\"200\" height=\"250\" ></a>\n"
                                +
                                "                            </tr>\n" +
                                "                        </table>\n" +
                                "                    </div>\n" +
                                "                </td>\n" +
                                "            </tr>\n" +
                                "            <tr>\n" +
                                "                <td align=\"center\" style=\"padding: 5px 35px 5px 35px; background-color: #ffffff; bgcolor:#ffffff\" >\n"
                                +
                                "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px; background: #ffffff;\">\n"
                                +
                                "                    <tr>\n" +
                                "                        <td align=\"center\" style=\"font-family: 'Poppins', sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding-top: 25px;\">\n"
                                +
                                "                            <img src=\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcROdDLqHc5-BVEiJalAvikD7j93IM4cMAcdwA&usqp=CAU\" width=\"200\" height=\"250\" style=\"display: block; border: 0px;\" /><br>\n"
                                +
                                "                            \n" +
                                "                        </td>\n" +
                                "                    </tr>\n" +
                                "                    <tr>\n" +
                                "                        <td align=\"center\" >\n" +
                                "                            <p style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 200; line-height: 24px; color: #524e4e; \">\n"
                                +
                                "                               We would like to inform you that your request for store manager has been rejected due to following reasion:\n"
                                +
                                "                            </p>\n" +
                                "                            <h4 style=\"font-family: 'Poppins', sans-serif;\">Team PillZone</h4>\n"
                                +
                                "                        </td>\n" +
                                "                    </tr>\n" +
                                "                </table>\n" +
                                "                \n" +
                                "                </td>\n" +
                                "            </tr>\n" +
                                "             \n" +
                                "        </table>\n" +
                                "    </tr>\n" +
                                "</table>\n" +
                                "    \n" +
                                "</body>\n" +
                                "</html>");
                emailDtoList.add(emailDto);
                emailClient.sendEmail(emailDtoList);

                return new ResponseEntity<>(response, HttpStatus.OK);

        }

        @PutMapping("/approve/{id}")
        public ResponseEntity<String> approvePendingRequestById(@PathVariable("id") Long managerId) {

                String response = adminManagerService.approvePendingRequestById(managerId);
                roleClient.approveManager(managerId);
                List<EmailDto> emailDtoList = new ArrayList<>();
                EmailDto emailDto = new EmailDto();
                emailDto.setUserId(managerId);
                emailDto.setToEmail("ankit.satyam@publicissapient.com");
                emailDto.setEmailSubject("Request Approved");
                emailDto.setEmailBody("<!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<head>\n" +
                                "<title></title>\n" +
                                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                                "<link href=\"https://fonts.googleapis.com/css2?family=Poppins:wght@800&display=swap\" rel=\"stylesheet\">\n"
                                +
                                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                                "<style type=\"text/css\">\n" +
                                "    \n" +
                                "\n" +
                                "body, table, td, a { -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }\n" +
                                "table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }\n" +
                                "img { -ms-interpolation-mode: bicubic; }\n" +
                                "\n" +
                                "img { border: 0; height: auto; line-height: 100%; outline: none; text-decoration: none; }\n"
                                +
                                "table { border-collapse: collapse !important; }\n" +
                                "body { height: 100% !important; margin: 0 !important; padding: 0 !important; width: 100% !important; }\n"
                                +
                                "\n" +
                                "\n" +
                                "a[x-apple-data-detectors] {\n" +
                                "    color: inherit !important;\n" +
                                "    text-decoration: none !important;\n" +
                                "    font-size: inherit !important;\n" +
                                "    font-family: inherit !important;\n" +
                                "    font-weight: inherit !important;\n" +
                                "    line-height: inherit !important;\n" +
                                "}\n" +
                                "\n" +
                                "@media screen and (max-width: 480px) {\n" +
                                "    .mobile-hide {\n" +
                                "        display: none !important;\n" +
                                "    }\n" +
                                "    .mobile-center {\n" +
                                "        text-align: center !important;\n" +
                                "    }\n" +
                                "}\n" +
                                "div[style*=\"margin: 16px 0;\"] { margin: 0 !important; }\n" +
                                "</style>\n" +
                                "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\n"
                                +
                                "\n" +
                                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                                "    <tr>\n" +
                                "        <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\n"
                                +
                                "        \n" +
                                "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:500px;\">\n"
                                +
                                "            <tr>\n" +
                                "                <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#dae5f7\">\n"
                                +
                                "               \n" +
                                "                    <div style=\"display:inline-block;  vertical-align:top; width:100%;\">\n"
                                +
                                "                        <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\n"
                                +
                                "                            <tr>\n" +
                                "                                <a href=\"https://d1j9yqk9s11go2.cloudfront.net/\"><img src=\"./img/logo-removebg-preview.png\" alt=\"\"  width=\"200\" height=\"250\" ></a>\n"
                                +
                                "                            </tr>\n" +
                                "                        </table>\n" +
                                "                    </div>\n" +
                                "                </td>\n" +
                                "            </tr>\n" +
                                "            <tr>\n" +
                                "                <td align=\"center\" style=\"padding: 5px 35px 5px 35px; background-color: #ffffff; bgcolor:#ffffff\" >\n"
                                +
                                "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px; background: #ffffff;\">\n"
                                +
                                "                    <tr>\n" +
                                "                        <td align=\"center\" style=\"font-family: 'Poppins', sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding-top: 25px;\">\n"
                                +
                                "                            <img src=\"https://bit.ly/3KZfikP\" width=\"200\" height=\"250\" style=\"display: block; border: 0px;\" /><br>\n"
                                +
                                "                            <h2 style=\"font-size: 30px; font-weight: 800; line-height: 36px; color: rgb(17, 217, 67); margin: 0;\">\n"
                                +
                                "                                Congratulations!\n" +
                                "                            </h2>\n" +
                                "                        </td>\n" +
                                "                    </tr>\n" +
                                "                    <tr>\n" +
                                "                        <td align=\"center\" >\n" +
                                "                            <p style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 200; line-height: 24px; color: #524e4e; \">\n"
                                +
                                "                               We would like to inform you that you are now eligible to <br>open and manage your store on PillZone.\n"
                                +
                                "                            </p>\n" +
                                "                            <h4 style=\"font-family: 'Poppins', sans-serif;\">Team PillZone</h4>\n"
                                +
                                "                        </td>\n" +
                                "                    </tr>\n" +
                                "                </table>\n" +
                                "                \n" +
                                "                </td>\n" +
                                "            </tr>\n" +
                                "             \n" +
                                "        </table>\n" +
                                "    </tr>\n" +
                                "</table>\n" +
                                "    \n" +
                                "</body>\n" +
                                "</html>");

                emailDtoList.add(emailDto);
                emailClient.sendEmail(emailDtoList);
                return new ResponseEntity<>(response, HttpStatus.CREATED);

        }

        @GetMapping("/get-manager-with-filter")
        public ResponseEntity<PageableResponse<ManagerDto>> getManagersByFiltering(
                        @RequestParam(required = false) String status,
                        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                        @RequestParam(defaultValue = "managerId") String sortBy,
                        @RequestParam(required = false,defaultValue = "") String name) {

                PageableResponse<ManagerDto> pendingRequests = adminManagerService.getManagersWithFilter(status,
                                pageNumber, pageSize, sortBy,name);
                return new ResponseEntity<>(pendingRequests, HttpStatus.OK);

        }

}
