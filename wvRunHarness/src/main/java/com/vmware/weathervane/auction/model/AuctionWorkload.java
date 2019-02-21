/*
Copyright (c) 2018 VMware, Inc. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.vmware.weathervane.auction.model;

import java.util.LinkedList;
import java.util.List;

public class AuctionWorkload extends Workload {
	// RunConfiguration fields
	private AuctionDriver driver = new AuctionDriver();
	private List<AuctionAppInstance> appInstances = new LinkedList<AuctionAppInstance>();

	public AuctionWorkload() {
		super();

		// create a default list of services in a default app instance
		List<AuctionService> serviceList = new LinkedList<AuctionService>();

		AuctionAppServer appServer = new AuctionAppServer();
		serviceList.add(appServer);
		AuctionBidServer bidServer = new AuctionBidServer();
		serviceList.add(bidServer);
		AuctionCoordinationServer coordServer = new AuctionCoordinationServer();
		serviceList.add(coordServer);
		AuctionDataManager dmServer = new AuctionDataManager();
		serviceList.add(dmServer);
		AuctionDbServer dbServer = new AuctionDbServer();
		serviceList.add(dbServer);
		AuctionMsgServer msgServer = new AuctionMsgServer();
		serviceList.add(msgServer);
		AuctionNosqlServer nosqlServer = new AuctionNosqlServer();
		serviceList.add(nosqlServer);
		AuctionWebServer webServer = new AuctionWebServer();
		serviceList.add(webServer);

		AuctionAppInstance appInstance = new AuctionAppInstance();
		appInstance.setServices(serviceList);
		appInstances.add(appInstance);
	}

	// RunConfiguration getters and setters
	public AuctionDriver getDriver() {
		return driver;
	}

	public void setDriver(AuctionDriver driver) {
		this.driver = driver;
	}

	public List<AuctionAppInstance> getAppInstances() {
		return appInstances;
	}

	public void setAppInstances(List<AuctionAppInstance> appInstances) {
		this.appInstances = appInstances;
	}

}