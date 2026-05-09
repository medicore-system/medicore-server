package com.medicore.api.controllers;

import com.medicore.api.dtos.HospitalRequest;
import com.medicore.api.dtos.HospitalResponse;
import com.medicore.api.dtos.HospitalUpdateRequest;
import com.medicore.api.services.IHospitalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final IHospitalService hospitalService;

    @PostMapping
    public ResponseEntity<HospitalResponse> createHospital(
            @Valid @RequestBody HospitalRequest request) {
        HospitalResponse response = hospitalService.createHospital(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalResponse> updateHospital(
            @Valid @RequestBody HospitalUpdateRequest request,
            @PathVariable String id){
        HospitalResponse response = hospitalService.updateHospital(request, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
