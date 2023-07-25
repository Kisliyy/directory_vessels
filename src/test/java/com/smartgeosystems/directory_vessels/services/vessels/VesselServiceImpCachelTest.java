package com.smartgeosystems.directory_vessels.services.vessels;

import com.smartgeosystems.directory_vessels.config.CacheConfig;
import com.smartgeosystems.directory_vessels.mappers.vessels.VesselMapper;
import com.smartgeosystems.directory_vessels.models.Vessel;
import com.smartgeosystems.directory_vessels.repository.VesselRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        VesselServiceImpl.class,
        CacheConfig.class
})
class VesselServiceImpCachelTest {

    @Autowired
    private VesselService vesselService;

    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private VesselMapper vesselMapper;

    @MockBean
    private VesselRepository vesselRepository;

    @AfterEach
    void clear() {
        final Cache vesselsImoCache = cacheManager.getCache("vessels_imo");
        final Cache vesselsMmsiCache = cacheManager.getCache("vessels_mmsi");
        assertNotNull(vesselsImoCache);
        assertNotNull(vesselsMmsiCache);
        vesselsImoCache.clear();
        vesselsMmsiCache.clear();
    }

    @Test
    void findByImoVesselAddInCacheTest() {
        final var imo = 123455L;
        final Vessel vessel = Vessel.builder()
                .imo(imo)
                .build();

        when(vesselRepository.findByImo(imo))
                .thenReturn(Optional.of(vessel));

        Vessel byImo = vesselService.findByImo(imo);

        assertNotNull(byImo);
        assertEquals(vessel, byImo);

        final Cache cache = cacheManager.getCache("vessels_imo");
        assertNotNull(cache);

        Vessel cacheVessel = cache.get(imo, Vessel.class);
        assertNotNull(cacheVessel);
        assertEquals(vessel, cacheVessel);
    }

    @Test
    void findByImoWillBeCalledOnceRepositoryTest() {
        final var imo = 123455L;
        final Vessel vessel = Vessel.builder()
                .imo(imo)
                .build();

        final Optional<Vessel> optionalVessel = Optional.of(vessel);

        when(vesselRepository.findByImo(imo))
                .thenReturn(optionalVessel, any());

        vesselService.findByImo(imo);
        vesselService.findByImo(imo);

        verify(vesselRepository, times(1)).findByImo(imo);
    }

    @Test
    void getByImoVesselAddInCacheNotOptionalTest() {
        final var imo = 123455L;
        final Vessel vessel = Vessel.builder()
                .imo(imo)
                .build();

        Optional<Vessel> optionalVessel = Optional.of(vessel);

        when(vesselRepository.findByImo(imo))
                .thenReturn(optionalVessel);

        Optional<Vessel> byImo = vesselService.getByImo(imo);

        assertNotNull(byImo);
        assertEquals(optionalVessel, byImo);

        final Cache cache = cacheManager.getCache("vessels_imo");
        assertNotNull(cache);

        Vessel cacheVessel = cache.get(imo, Vessel.class);
        assertNotNull(cacheVessel);
        assertEquals(vessel, cacheVessel);
    }

    @Test
    void getByImoWillBeCalledOnceRepositoryTest() {
        final var imo = 123455L;
        final Vessel vessel = Vessel.builder()
                .imo(imo)
                .build();

        final Optional<Vessel> optionalVessel = Optional.of(vessel);

        when(vesselRepository.findByImo(imo))
                .thenReturn(optionalVessel, any());

        vesselService.getByImo(imo);
        vesselService.getByImo(imo);

        verify(vesselRepository, times(1)).findByImo(imo);
    }


    @Test
    void findByMmsiVesselAddInCacheTest() {
        final var mmsi = 123455L;
        final Vessel vessel = Vessel.builder()
                .mmsi(mmsi)
                .build();

        when(vesselRepository.findByMmsi(mmsi))
                .thenReturn(Optional.of(vessel));

        Vessel byImo = vesselService.findByMmsi(mmsi);

        assertNotNull(byImo);
        assertEquals(vessel, byImo);

        final Cache cache = cacheManager.getCache("vessels_mmsi");
        assertNotNull(cache);

        Vessel cacheVessel = cache.get(mmsi, Vessel.class);
        assertNotNull(cacheVessel);
        assertEquals(vessel, cacheVessel);
    }

    @Test
    void findByMmsiWillBeCalledOnceRepositoryTest() {
        final var mmsi = 123455L;
        final Vessel vessel = Vessel.builder()
                .mmsi(mmsi)
                .build();

        final Optional<Vessel> optionalVessel = Optional.of(vessel);

        when(vesselRepository.findByMmsi(mmsi))
                .thenReturn(optionalVessel, any());

        vesselService.findByMmsi(mmsi);
        vesselService.findByMmsi(mmsi);

        verify(vesselRepository, times(1)).findByMmsi(mmsi);
    }
}