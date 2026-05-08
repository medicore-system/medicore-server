package com.medicore.api.services;

import com.medicore.api.dtos.HospitalRequest;
import com.medicore.api.dtos.HospitalResponse;

public interface IHospitalService {

    HospitalResponse createHospital(HospitalRequest request);
}
