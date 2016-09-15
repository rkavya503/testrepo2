package com.honeywell.dras.vtn.dras.service;

import javax.xml.ws.Endpoint;

public class VtnDrasSoapServicePublisher {

	public static void main(String[] args) {
		Endpoint.publish("http://localhost:9889/VtnDrasService", new VtnDrasSoapService());
	}
}
