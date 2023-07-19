package com.smartgeosystems.directory_vessels.controllers;

import com.smartgeosystems.directory_vessels.dto.PageAndSortDto;
import com.smartgeosystems.directory_vessels.dto.VesselRequestDto;
import com.smartgeosystems.directory_vessels.dto.VesselResponseDto;
import com.smartgeosystems.directory_vessels.dto.VesselUpdateDto;
import com.smartgeosystems.directory_vessels.mappers.vessels.VesselMapperResponse;
import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.services.vessels.VesselService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer authentication")
public class VesselController {

    private final VesselService vesselService;
    private final VesselMapperResponse vesselMapperResponse = Mappers.getMapper(VesselMapperResponse.class);

    @GetMapping(value = "/api/vessels/{imo}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VesselResponseDto> findByImo(@PathVariable("imo") long imo) {
        var vessel = vesselService.findByImo(imo);
        var responseDto = vesselMapperResponse.vesselToVesselResponseDto(vessel);
        return ResponseEntity
                .ok(responseDto);
    }

    @GetMapping(value = "/api/vessels/{mmsi}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VesselResponseDto> findByMmsi(@PathVariable("mmsi") long mmsi) {
        var vessel = vesselService.findByMmsi(mmsi);
        var responseDto = vesselMapperResponse.vesselToVesselResponseDto(vessel);
        return ResponseEntity
                .ok(responseDto);
    }

    @PostMapping(value = "/api/vessels/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VesselResponseDto> saveVessel(@RequestBody @Valid VesselRequestDto vesselRequestDto) {
        var vessel = vesselService.processingVessel(vesselRequestDto);
        var responseDto = vesselMapperResponse.vesselToVesselResponseDto(vessel);
        return ResponseEntity
                .ok(responseDto);
    }

    @DeleteMapping(value = "/api/vessels/{imo}")
    public ResponseEntity<?> deleteByImo(@PathVariable("imo") long imo) {
        vesselService.deleteById(imo);
        return ResponseEntity
                .ok()
                .build();
    }

    @PutMapping(value = "/api/vessels/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateVessel(@RequestBody @Valid VesselUpdateDto vesselUpdateDto) {
        vesselService.updateVessel(vesselUpdateDto);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping(value = "/api/vessels/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Vessel>> findAll(@RequestBody @Valid PageAndSortDto pageAndSortDto) {
        var page = PageRequest.of(pageAndSortDto.getPage(), pageAndSortDto.getSize());
        return ResponseEntity
                .ok(vesselService.findAll(page));
    }
}
