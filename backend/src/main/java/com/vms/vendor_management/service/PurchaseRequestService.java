package com.vms.vendor_management.service;

import com.vms.vendor_management.exception.ResourceNotFoundException;
import com.vms.vendor_management.model.PurchaseRequest;
import com.vms.vendor_management.model.enums.RequestStatus;
import com.vms.vendor_management.repository.PurchaseRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseRequestService {

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    public List<PurchaseRequest> getAllRequests() {
        return purchaseRequestRepository.findAll();
    }

    public PurchaseRequest getRequestById(Long id) {
        return purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseRequest not found with id: " + id));
    }

    public PurchaseRequest createRequest(PurchaseRequest request) {
        if(request.getStatus() == null) request.setStatus(RequestStatus.PENDING);
        return purchaseRequestRepository.save(request);
    }

    public PurchaseRequest updateStatus(Long id, RequestStatus status) {
        PurchaseRequest request = getRequestById(id);
        request.setStatus(status);
        return purchaseRequestRepository.save(request);
    }
}
