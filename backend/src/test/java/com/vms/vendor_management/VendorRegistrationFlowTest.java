package com.vms.vendor_management;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VendorRegistrationFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registrationFailsWhenRequiredFieldsAreMissing() throws Exception {
        mockMvc.perform(multipart("/api/vendor-registration/register")
                        .param("name", "Missing Fields Vendor")
                        .param("email", "missing-fields@example.com")
                        .param("phone", "9999999999")
                        .param("companyName", "Missing Fields Pvt Ltd")
                        .param("gstNumber", "22AAAAA1111A1Z5")
                        .param("panNumber", "ABCDE1111F")
                        .param("serviceType", "Consulting")
                        .param("category", "CONSULTING"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.details.address").exists())
                .andExpect(jsonPath("$.details.payment").exists());
    }

    @Test
    void registrationSucceedsWhenAllRequiredFieldsAreProvided() throws Exception {
        mockMvc.perform(multipart("/api/vendor-registration/register")
                        .param("name", "Acme Vendor")
                        .param("contactPerson", "Jane Doe")
                        .param("email", "acme-vendor@example.com")
                        .param("phone", "8888888888")
                        .param("serviceType", "IT Services")
                        .param("category", "IT_SERVICES")
                        .param("companyName", "Acme Vendor Pvt Ltd")
                        .param("gstNumber", "22AAAAA2222A1Z5")
                        .param("panNumber", "ABCDE2222F")
                        .param("address", "42 Market Street")
                        .param("city", "Bengaluru")
                        .param("state", "Karnataka")
                        .param("country", "India")
                        .param("pincode", "560001")
                        .param("payment", "1500.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.vendor.companyName").value("Acme Vendor Pvt Ltd"))
                .andExpect(jsonPath("$.vendor.status").value("PENDING_APPROVAL"));
    }
}
