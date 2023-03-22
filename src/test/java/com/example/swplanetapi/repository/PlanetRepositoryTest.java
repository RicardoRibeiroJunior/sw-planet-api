package com.example.swplanetapi.repository;

import static com.example.swplanetapi.domain.commons.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.swplanetapi.domain.Planet;

@DataJpaTest
public class PlanetRepositoryTest {
	
	@Autowired
	private PlanetRepository planetRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void createPlanet_WithValidData_ReturnsPlanet() {
		
		Planet planet = planetRepository.save(PLANET);
		Planet sut = testEntityManager.find(Planet.class, PLANET.getId());
		
		assertThat(sut).isNotNull();
		assertThat(sut.getName()).isEqualTo(planet.getName());
		assertThat(sut.getClimate()).isEqualTo(planet.getClimate());
		assertThat(sut.getTerrain()).isEqualTo(planet.getTerrain());
		
	}
	
	@Test
	public void createPlanet_WithInvalidData_ThrowsException() {
		
		Planet emptyPlanet = new Planet();
		Planet invalidPlanet = new Planet("","","");
		
		assertThatThrownBy(() -> planetRepository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> planetRepository.save(invalidPlanet)).isInstanceOf(RuntimeException.class);
		
	}
	
	@Test
	public void createPlanet_WithExistingName_ThrowsException() {
		
		Planet newPlanet = new Planet("Terra", "Clima", "Terreno");
		Planet planet = testEntityManager.persistFlushFind(newPlanet);
		testEntityManager.detach(planet);
		planet.setId(null);
		assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
		
	}
	
}
