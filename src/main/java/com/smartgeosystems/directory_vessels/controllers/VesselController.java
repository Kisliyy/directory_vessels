package com.smartgeosystems.directory_vessels.controllers;

import com.smartgeosystems.directory_vessels.dto.VesselRequestDto;
import com.smartgeosystems.directory_vessels.dto.VesselResponseDto;
import com.smartgeosystems.directory_vessels.dto.VesselUpdateDto;
import com.smartgeosystems.directory_vessels.services.vessel.VesselService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class VesselController {

    private final VesselService vesselService;

    @GetMapping(value = "/api/vessel/{imo}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VesselResponseDto> findByImo(@PathVariable("imo") long imo) {
        var response = vesselService.findByImo(imo);
        return ResponseEntity
                .ok(response);
    }

    @PostMapping(value = "/api/vessel/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VesselResponseDto> saveVessel(@RequestBody @Valid VesselRequestDto vesselRequestDto) {
        var response = vesselService.processingVessel(vesselRequestDto);
        return ResponseEntity
                .ok(response);
    }

    @DeleteMapping(value = "/api/vessel/{imo}")
    public ResponseEntity<?> deleteByImo(@PathVariable("imo") long imo) {
        vesselService.deleteById(imo);
        return ResponseEntity
                .ok()
                .build();
    }

    @PutMapping(value = "/api/vessel/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateVessel(@RequestBody @Valid VesselUpdateDto vesselUpdateDto) {
        vesselService.updateVessel(vesselUpdateDto);
        return ResponseEntity
                .ok()
                .build();
    }
}
