package br.com.ricardo.swplanetapi.common;

import br.com.ricardo.swplanetapi.domain.Planet;

public class PlanetConstants {
	public static final Planet PLANET = new Planet("name","climate","terrain");
	public static final Planet INVALID_PLANET = new Planet("name","climate","terrain");
	public static final Long ID_PLANET = 1L;
	public static final Long ID_INVALID_PLANET = 0L;
}
