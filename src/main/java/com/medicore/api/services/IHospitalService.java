package com.medicore.api.services;

import com.medicore.api.dtos.HospitalRequest;
import com.medicore.api.dtos.HospitalResponse;
import com.medicore.api.dtos.HospitalUpdateRequest;

public interface IHospitalService {

    HospitalResponse createHospital(HospitalRequest request);

    HospitalResponse updateHospital(HospitalUpdateRequest request, String id);
}
