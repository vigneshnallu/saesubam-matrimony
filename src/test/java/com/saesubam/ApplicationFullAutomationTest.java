package com.saesubam;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.saesubam.model.Users;
import com.saesubam.service.UserService;

@SpringBootTest(classes = SpringBootApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class ApplicationFullAutomationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    @DisplayName("1. Verify all public static pages load with HTTP 200 OK")
    public void testPublicPages() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/login")).andExpect(status().isOk());
        mockMvc.perform(get("/register")).andExpect(status().isOk());
        mockMvc.perform(get("/terms")).andExpect(status().isOk());
        mockMvc.perform(get("/privacy-policy")).andExpect(status().isOk());
        mockMvc.perform(get("/grievance")).andExpect(status().isOk());
        mockMvc.perform(get("/forgot-password")).andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("2. Verify full candidate registration, mandatory photo upload & OTP verification flow")
    public void testRegistrationAndOtpFlow() throws Exception {
        MockMultipartFile photoFile = new MockMultipartFile(
            "photoFile",
            "profile_test.jpg",
            "image/jpeg",
            "fake-image-bytes-content".getBytes()
        );

        String uniqueEmail = "autotest." + System.currentTimeMillis() + "@saesubam.com";

        // Step A: Submit registration form with mandatory age & photoFile
        MvcResult regResult = mockMvc.perform(multipart("/userregister")
                .file(photoFile)
                .param("name", "Automation Test Candidate")
                .param("email", uniqueEmail)
                .param("mobile", "9876543210")
                .param("gender", "Female")
                .param("age", "25")
                .param("profileFor", "Self")
                .param("caste", "Devar")
                .param("city", "Madurai")
                .param("password", "Pass123456")
                .param("confirmPassword", "Pass123456"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/verify-otp"))
            .andReturn();

        MockHttpSession session = (MockHttpSession) regResult.getRequest().getSession();

        // Step B: Load verify-otp page
        mockMvc.perform(get("/verify-otp").session(session))
            .andExpect(status().isOk());

        // Step C: Process OTP verification with master code 623701
        mockMvc.perform(post("/api/auth/verify-otp")
                .session(session)
                .param("otpCode", "623701"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/dashboard?verified=true"));
    }

    @Test
    @Order(3)
    @DisplayName("3. Verify member authentication & dashboard pages access")
    public void testMemberPagesWithSession() throws Exception {
        Users candidate = userService.findByEmail("priya@gmail.com");
        if (candidate == null) {
            candidate = userService.getAllUsers().isEmpty() ? null : userService.getAllUsers().get(0);
        }

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loggedInUser", candidate);

        mockMvc.perform(get("/dashboard").session(session)).andExpect(status().isOk());
        mockMvc.perform(get("/my-profile").session(session)).andExpect(status().isOk());
        mockMvc.perform(get("/my-profile/edit").session(session)).andExpect(status().isOk());
        mockMvc.perform(get("/profiles").session(session)).andExpect(status().isOk());
        mockMvc.perform(get("/interests").session(session)).andExpect(status().isOk());
        mockMvc.perform(get("/shortlist").session(session)).andExpect(status().isOk());
        mockMvc.perform(get("/payment").session(session)).andExpect(status().isOk());
        mockMvc.perform(get("/contact").session(session)).andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("4. Verify Admin Dashboard & Management pages")
    public void testAdminDashboard() throws Exception {
        Users admin = userService.findByEmail("admin@saesubam.com");
        if (admin == null) {
            admin = userService.findByEmail("autotest.candidate@saesubam.com");
        }
        if (admin != null) {
            admin.setRole("ADMIN");
        }

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loggedInUser", admin);
        session.setAttribute("adminLoggedIn", true);

        mockMvc.perform(get("/admin/dashboard").session(session)).andExpect(status().isOk());
        mockMvc.perform(get("/admin/data").session(session)).andExpect(status().isOk());
    }
}
