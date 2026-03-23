package com.vms.vendor_management.service;

import com.vms.vendor_management.exception.ResourceNotFoundException;
import com.vms.vendor_management.model.Contract;
import com.vms.vendor_management.model.Vendor;
import com.vms.vendor_management.repository.ContractRepository;
import com.vms.vendor_management.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final VendorRepository vendorRepository;

    public ContractService(ContractRepository contractRepository, VendorRepository vendorRepository) {
        this.contractRepository = contractRepository;
        this.vendorRepository = vendorRepository;
    }

    public Contract createContract(Long vendorId, Contract contract) {
        Vendor vendor = getVendor(vendorId);
        contract.setVendor(vendor);
        return contractRepository.save(contract);
    }

    public List<Contract> getContractsByVendor(Long vendorId) {
        getVendor(vendorId);
        return contractRepository.findByVendorIdOrderByStartDateDesc(vendorId);
    }

    public Contract getContractById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + id));
    }

    public Contract updateContract(Long id, Contract request) {
        Contract existing = getContractById(id);
        existing.setTitle(request.getTitle());
        existing.setStartDate(request.getStartDate());
        existing.setEndDate(request.getEndDate());
        existing.setValue(request.getValue());
        existing.setTerms(request.getTerms());
        return contractRepository.save(existing);
    }

    public void deleteContract(Long id) {
        Contract existing = getContractById(id);
        contractRepository.delete(existing);
    }

    private Vendor getVendor(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));
    }
}
