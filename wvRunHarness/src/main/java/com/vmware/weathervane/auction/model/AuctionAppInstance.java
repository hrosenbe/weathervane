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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vmware.weathervane.auction.runtime.DataManager;

public class AuctionAppInstance extends AppInstance {
	// RunConfiguration fields
	private List<String> loadPath = new LinkedList<String>();
	private boolean repeatLoadPath = true;
	private String computeResourceName;
	private String imageStoreType = "mongodb";
	private List<AuctionService> services = Stream.of(
														new AuctionAppServer(),
														new AuctionBidServer(),
														new AuctionCoordinationServer(),
														new AuctionDataManager(),
														new AuctionDbServer(),
														new AuctionMsgServer(),
														new AuctionNosqlServer(),
														new AuctionWebServer()
													).collect(Collectors.toList());

	// RunConfiguration getters and setters
	public List<String> getLoadPath() {
		return loadPath;
	}

	public void setLoadPath(List<String> loadPath) {
		this.loadPath = loadPath;
	}

	public boolean isRepeatLoadPath() {
		return repeatLoadPath;
	}

	public void setRepeatLoadPath(boolean repeatLoadPath) {
		this.repeatLoadPath = repeatLoadPath;
	}

	public String getComputeResourceName() {
		return computeResourceName;
	}

	public void setComputeResourceName(String computeResourceName) {
		this.computeResourceName = computeResourceName;
	}

	public String getImageStoreType() {
		return imageStoreType;
	}

	public void setImageStoreType(String imageStoreType) {
		this.imageStoreType = imageStoreType;
	}

	public List<AuctionService> getServices() {
		return services;
	}

	public void setServices(List<AuctionService> services) {
		this.services = services;
	}

	// Runtime
	private DataManager dataManager;
	public DataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

}