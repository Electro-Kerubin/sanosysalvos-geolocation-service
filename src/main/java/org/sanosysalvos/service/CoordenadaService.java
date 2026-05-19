package org.sanosysalvos.service;

import lombok.RequiredArgsConstructor;
import org.sanosysalvos.dto.CoordenadaDTO;
import org.sanosysalvos.entity.Comuna;
import org.sanosysalvos.entity.Coordenada;
import org.sanosysalvos.exception.ResourceNotFoundException;
import org.sanosysalvos.repository.ComunaRepository;
import org.sanosysalvos.repository.CoordenadaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class CoordenadaService {

    private final CoordenadaRepository coordenadaRepository;
    private final ComunaRepository comunaRepository;

    public List<CoordenadaDTO> findAll() {
        return coordenadaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CoordenadaDTO findById(Long id) {
        return toDTO(coordenadaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coordenada no encontrada con id: " + id)));
    }

    public CoordenadaDTO findByReporte(Long idReporte) {
        return toDTO(coordenadaRepository.findByIdReporte(idReporte)
                .orElseThrow(() -> new ResourceNotFoundException("Coordenada no encontrada para reporte: " + idReporte)));
    }

    public List<CoordenadaDTO> findByComuna(Long idComuna) {
        return coordenadaRepository.findByComuna_IdComuna(idComuna).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public CoordenadaDTO save(CoordenadaDTO dto) {
        Comuna comuna = comunaRepository.findById(dto.getIdComuna())
                .orElseThrow(() -> new ResourceNotFoundException("Comuna no encontrada con id: " + dto.getIdComuna()));
        Coordenada coordenada = Coordenada.builder()
                .ubicacionLat(dto.getUbicacionLat())
                .ubicacionLon(dto.getUbicacionLon())
                .idReporte(dto.getIdReporte())
                .comuna(comuna)
                .direccion(dto.getDireccion())
                .build();
        return toDTO(coordenadaRepository.save(coordenada));
    }

    @Transactional
    public CoordenadaDTO update(Long id, CoordenadaDTO dto) {
        Coordenada coordenada = coordenadaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coordenada no encontrada con id: " + id));
        Comuna comuna = comunaRepository.findById(dto.getIdComuna())
                .orElseThrow(() -> new ResourceNotFoundException("Comuna no encontrada con id: " + dto.getIdComuna()));
        coordenada.setUbicacionLat(dto.getUbicacionLat());
        coordenada.setUbicacionLon(dto.getUbicacionLon());
        coordenada.setIdReporte(dto.getIdReporte());
        coordenada.setComuna(comuna);
        coordenada.setDireccion(dto.getDireccion());
        return toDTO(coordenadaRepository.save(coordenada));
    }

    @Transactional
    public void delete(Long id) {
        coordenadaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coordenada no encontrada con id: " + id));
        coordenadaRepository.deleteById(id);
    }

    private CoordenadaDTO toDTO(Coordenada c) {
        return CoordenadaDTO.builder()
                .idUbicacionCoordenadas(c.getIdUbicacionCoordenadas())
                .ubicacionLat(c.getUbicacionLat())
                .ubicacionLon(c.getUbicacionLon())
                .idReporte(c.getIdReporte())
                .idComuna(c.getComuna().getIdComuna())
                .direccion(c.getDireccion())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
