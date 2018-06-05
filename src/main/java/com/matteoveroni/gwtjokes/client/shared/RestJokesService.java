package com.matteoveroni.gwtjokes.client.shared;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("jokes")
public interface RestJokesService {

	/**
	 * Returns a random joke
	 * @return Response (Joke)
	 */
	@GET
	@Path("/random")
	@Produces("application/json")
	public Response getRandomJoke();

	/**
	 * Save a new joke
	 * @param joke The new joke to save
	 * @return Response (Joke) - The saved joke or null
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addJoke(Joke joke);

	/**
	 * Returns the complete List of all jokes
	 * @return Response (List[Joke])
	 */
	@GET
	@Produces("application/json")
	public Response getAllJokes();
}
