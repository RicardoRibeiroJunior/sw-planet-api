package br.com.ricardo.swplanetapi.service;

import static br.com.ricardo.swplanetapi.common.PlanetConstants.ID_INVALID_PLANET;
import static br.com.ricardo.swplanetapi.common.PlanetConstants.ID_PLANET;
import static br.com.ricardo.swplanetapi.common.PlanetConstants.INVALID_PLANET;
import static br.com.ricardo.swplanetapi.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import br.com.ricardo.swplanetapi.domain.Planet;
import br.com.ricardo.swplanetapi.domain.QueryBuilder;
import br.com.ricardo.swplanetapi.repository.PlanetRepository;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

	@InjectMocks
	private PlanetService planetService;

	@Mock
	private PlanetRepository planetRepository;

	@Test
	public void createPlanet_WithValidData_ReturnsPlanet() {

		when(planetRepository.save(PLANET)).thenReturn(PLANET);

		// sut (sistema sobre teste)
		Planet sut = planetService.create(PLANET);
		assertThat(sut).isEqualTo(PLANET);
	}

	@Test
	public void createPlanet_WithInvalidData_ThrowsException() {
		when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);
		assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getPlanet_ByExistingId_ReturnsPlanet() {

		when(planetRepository.findById(ID_PLANET)).thenReturn(Optional.of(PLANET));

		Optional<Planet> sut = planetService.getById(ID_PLANET);

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(PLANET);

	}

	@Test
	public void getPlanet_ByUnexistingId_ReturnsEmpty() {

		when(planetRepository.findById(ID_INVALID_PLANET)).thenReturn(Optional.empty());

		Optional<Planet> sut = planetService.getById(ID_INVALID_PLANET);

		assertThat(sut).isEmpty();

	}

	@Test
	public void getPlanet_ByExistingName_ReturnsPlanet() {

		when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

		Optional<Planet> sut = planetService.getByName(PLANET.getName());

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(PLANET);

	}

	@Test
	public void getPlanet_ByUnexistingName_ReturnsEmpty() {

		final String name = "Unexisting name";

		when(planetRepository.findByName(name)).thenReturn(Optional.empty());

		Optional<Planet> sut = planetService.getByName(name);

		assertThat(sut).isEmpty();

	}

	@Test
	public void listPlanets_ReturnsAllPlanets() {

		List<Planet> planets = new ArrayList<>() {
			{
				add(PLANET);
			}
		};

		Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));

		when(planetRepository.findAll(query)).thenReturn(planets);

		List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSize(1);
		assertThat(sut.get(0)).isEqualTo(PLANET);

	}

	@Test
	public void listPlanets_ReturnNoPlanets() {
		
		List<Planet> planets = new ArrayList<>() {
			{
				add(INVALID_PLANET);
			}
		};

		Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));

		when(planetRepository.findAll(query)).thenReturn(Collections.emptyList());
		List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());
		assertThat(sut).isEmpty();
	}
	
	@Test
	public void deletePlanet_WithExistingId_doesNotThrowAnyException() {
		assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
	}
	
	@Test
	public void removePlanet_WithUnexistingId_ThrowException() {
		doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);
		
		assertThatThrownBy(() -> planetService.remove(99L)).isInstanceOf(RuntimeException.class);
	}

}
