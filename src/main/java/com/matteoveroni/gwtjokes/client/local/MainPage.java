package com.matteoveroni.gwtjokes.client.local;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.matteoveroni.gwtjokes.client.shared.Joke;
import static elemental2.dom.DomGlobal.window;
import elemental2.dom.HTMLButtonElement;
import elemental2.dom.HTMLInputElement;
import elemental2.dom.MouseEvent;
import javax.inject.Inject;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.ForEvent;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import com.matteoveroni.gwtjokes.client.shared.RestJokesService;
import static elemental2.dom.DomGlobal.console;
import java.util.List;
import org.jboss.errai.enterprise.client.jaxrs.MarshallingWrapper;
import org.jboss.errai.ui.nav.client.local.PageShowing;

@Page(role = {DefaultPage.class})
@Templated
public class MainPage {

	private static final String REST_SERVER = "http://localhost:8090/";

	// Example:
	//	https://github.com/klenovic/spring-errai-jaxrs/blob/master/src/main/java/org/jboss/errai/samples/restdemo/client/local/App.java
	@Inject
	@DataField
	private HTMLButtonElement btn_saveNewJoke;

	@Inject
	@DataField
	private HTMLButtonElement btn_printJokes;

	@Inject
	@DataField
	private HTMLButtonElement btn_printRandomJoke;

	@Inject
	@DataField
	private HTMLInputElement txt_newJoke;

//	private final Caller<RestJokesService> restCustomerService;
//	@Inject
//	public MainPage(Caller<RestJokesService> restCustomerService) {
//		this.restCustomerService = restCustomerService;
//	}
	@PageShowing
	public void onPageShowing() {
		RestClient.setJacksonMarshallingActive(true);
	}

	@EventHandler("btn_saveNewJoke")
	public void onBottoneSalvaNuovoJokeCliccato(@ForEvent("click") MouseEvent e) {
		String newJokeText = txt_newJoke.value;
		if (newJokeText == null || newJokeText.trim().isEmpty()) {
			window.alert("You can\'t insert an empty joke!");
		} else {
			Joke newJoke = new Joke();
			newJoke.setText(newJokeText);

			RestClient.create(
				RestJokesService.class,
				REST_SERVER,
				(Response response) -> {
					try {
						int responseStatusCode = response.getStatusCode();
						if (responseStatusCode == Response.SC_OK) {
							Joke savedJoke = MarshallingWrapper.fromJSON(response.getText(), Joke.class);
							if (savedJoke != null) {
								txt_newJoke.value = "";
								window.alert("Joke " + savedJoke + " saved");
							}
						} else {
							window.alert("Response status code: " + responseStatusCode);
						}
					} catch (Exception ex) {
						window.alert("Unexpected error parsing the response: " + ex.getMessage());
					}
				},
				(Request message, Throwable throwable) -> {
					window.alert("Error: " + throwable.getMessage());
					return true;
				}
			).addJoke(newJoke);
		}
	}

	@EventHandler("btn_printJokes")
	public void onBottoneStampaJokesCliccato(@ForEvent("click") MouseEvent e) {
		RestClient.create(
			RestJokesService.class,
			REST_SERVER,
			(Response response) -> {
				try {
					int responseStatusCode = response.getStatusCode();
					if (responseStatusCode == Response.SC_OK) {
						List<Joke> jokes = (List<Joke>) MarshallingWrapper.fromJSON(response.getText());
						jokes.forEach(joke -> {
							console.log("\nJoke: " + joke.toString());
						});
					} else {
						window.alert("Response status code: " + responseStatusCode);
					}
				} catch (Exception ex) {
					window.alert("Unexpected error parsing the response: " + ex.getMessage());
				}
			},
			(Request message, Throwable throwable) -> {
				window.alert("Errore" + throwable.getMessage());
				return true;
			}
		).getAllJokes();
	}

	@EventHandler("btn_printRandomJoke")
	public void onBottoneStampaJokeRandomCliccato(@ForEvent("click") MouseEvent e) {
		RestClient.create(
			RestJokesService.class,
			REST_SERVER,
			(Response response) -> {
				try {
					int responseStatusCode = response.getStatusCode();
					if (responseStatusCode == Response.SC_OK) {
						Joke joke = MarshallingWrapper.fromJSON(response.getText(), Joke.class);
						window.alert(joke.getText());
					} else {
						window.alert("Response status code: " + responseStatusCode);
					}
				} catch (Exception ex) {
					window.alert("Unexpected error parsing the response: " + ex.getMessage());
				}
			},
			(Request message, Throwable throwable) -> {
				window.alert("Error: " + throwable.getMessage());
				return true;
			}
		).getRandomJoke();
	}
}
