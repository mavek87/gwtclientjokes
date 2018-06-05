package com.matteoveroni.gwtjokes.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Joke {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Joke{" + "text=" + text + '}';
	}
	
}
