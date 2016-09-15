package com.akuacom.pss2.usecasetest.cases;

import org.junit.Test;
import com.akuacom.pss2.client.ClientManager;

public class DeleteClientUsecase extends AbstractUseCase {

	private String clientName = null;

	public DeleteClientUsecase() {
		this(null);
	}

	public DeleteClientUsecase(String clientName) {
		this.clientName = clientName;
	}

	@Override
	@Test
	public Object runCase() throws Exception {

		ClientManager cm = getClientMgr();
		boolean isInEvent = false;
		// Don't drop a client in event
		if (cm.getClientEventNames(clientName).size() > 0) {
			isInEvent = true;
		}

		if (!isInEvent) {
			// Delete the client object with given name
			cm.removeClient(clientName);
		}

		return null;

	}

}
