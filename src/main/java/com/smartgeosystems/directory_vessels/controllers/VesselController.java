package com.smartgeosystems.directory_vessels.controllers;

import com.smartgeosystems.directory_vessels.dto.exceptions.ExceptionMessageResponse;
import com.smartgeosystems.directory_vessels.dto.exceptions.ExceptionValidationResponse;
import com.smartgeosystems.directory_vessels.dto.vessels.VesselRequestDto;
import com.smartgeosystems.directory_vessels.dto.vessels.VesselResponseDto;
import com.smartgeosystems.directory_vessels.dto.vessels.VesselUpdateDto;
import com.smartgeosystems.directory_vessels.mappers.vessels.VesselMapperResponse;
import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.services.vessels.VesselService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequiredArgsConstructor
@Tag(name = "Vessel", description = "CRUD operation on the vessel")
public class VesselController {

    private final VesselService vesselService;
    private final VesselMapperResponse vesselMapperResponse;

    @Operation(summary = "Find vessel by imo")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Find vessel",
                    content = @Content(schema = @Schema(implementation = VesselResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ExceptionMessageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403", description = "The user is not authenticated or not authorized"
            )
    })
    @GetMapping(value = "/api/vessels/imo/{imo}")
    public ResponseEntity<VesselResponseDto> findByImo(@PathVariable("imo") long imo) {
        var vessel = vesselService.findByImo(imo);
        var responseDto = vesselMapperResponse.vesselToVesselResponseDto(vessel);
        return ResponseEntity
                .ok(responseDto);
    }

    @Operation(summary = "Find vessel by mmsi")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Find vessel",
                    content = @Content(schema = @Schema(implementation = VesselResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ExceptionMessageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403", description = "The user is not authenticated or not authorized"
            )
    })
    @GetMapping(value = "/api/vessels/mmsi/{mmsi}")
    public ResponseEntity<VesselResponseDto> findByMmsi(@PathVariable("mmsi") long mmsi) {
        var vessel = vesselService.findByMmsi(mmsi);
        var responseDto = vesselMapperResponse.vesselToVesselResponseDto(vessel);
        return ResponseEntity
                .ok(responseDto);
    }

    @Operation(summary = "Add new vessel")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Added vessel",
                    content = @Content(schema = @Schema(implementation = VesselResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ExceptionMessageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Value validation error",
                    content = @Content(schema = @Schema(implementation = ExceptionValidationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403", description = "The user is not authenticated or not authorized"
            )
    })
    @PostMapping(value = "/api/vessels/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VesselResponseDto> saveVessel(@RequestBody @Valid VesselRequestDto vesselRequestDto) {
        var vessel = vesselService.processingVessel(vesselRequestDto);
        var responseDto = vesselMapperResponse.vesselToVesselResponseDto(vessel);
        return ResponseEntity
                .ok(responseDto);
    }


    @Operation(summary = "Delete vessel by imo")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "The vessel has been removed"
            ),
            @ApiResponse(
                    responseCode = "403", description = "The user is not authenticated or not authorized"
            )
    })
    @DeleteMapping(value = "/api/vessels/{imo}")
    public ResponseEntity<?> deleteByImo(@PathVariable("imo") long imo) {
        vesselService.deleteById(imo);
        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(summary = "Update vessel")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "The vessel has been removed"
            ),
            @ApiResponse(
                    responseCode = "404", description = "Value validation error",
                    content = @Content(schema = @Schema(implementation = ExceptionValidationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403", description = "The user is not authenticated or not authorized"
            )
    })
    @PutMapping(value = "/api/vessels/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VesselResponseDto> updateVessel(@RequestBody @Valid VesselUpdateDto vesselUpdateDto) {
        Vessel vessel = vesselService.updateVessel(vesselUpdateDto);
        var vesselResponseDto = vesselMapperResponse.vesselToVesselResponseDto(vessel);
        return ResponseEntity
                .ok(vesselResponseDto);
    }

    @Operation(summary = "Find all vessel")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Find vessels"
            ),
            @ApiResponse(
                    responseCode = "403", description = "The user is not authenticated or not authorized"
            )
    })
    @GetMapping(value = "/api/vessels/")
    public Page<VesselResponseDto> findAll(@RequestParam(name = "page") @Min(value = 0) int page,
                                           @RequestParam(name = "size") @Min(value = 1) int size) {
        var pageRequest = PageRequest.of(page, size);
        return vesselService
                .findAll(pageRequest)
                .map(vesselMapperResponse::vesselToVesselResponseDto);
    }
}
