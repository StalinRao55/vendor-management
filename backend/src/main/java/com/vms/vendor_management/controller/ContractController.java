package com.vms.vendor_management.controller;

import com.vms.vendor_management.model.Contract;
import com.vms.vendor_management.service.ContractService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("/vendors/{vendorId}/contracts")
    public ResponseEntity<Contract> createContract(@PathVariable Long vendorId, @Valid @RequestBody Contract contract) {
        return ResponseEntity.ok(contractService.createContract(vendorId, contract));
    }

    @GetMapping("/vendors/{vendorId}/contracts")
    public ResponseEntity<List<Contract>> getContractsByVendor(@PathVariable Long vendorId) {
        return ResponseEntity.ok(contractService.getContractsByVendor(vendorId));
    }

    @GetMapping("/contracts/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @PutMapping("/contracts/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable Long id, @Valid @RequestBody Contract contract) {
        return ResponseEntity.ok(contractService.updateContract(id, contract));
    }

    @DeleteMapping("/contracts/{id}")
    public ResponseEntity<String> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.ok("Contract deleted successfully");
    }
}
