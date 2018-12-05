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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WorkloadAuction extends Workload {
	private Driver driver = new Driver();
	private List<AuctionAppInstance> appInstances = new LinkedList<AuctionAppInstance>();

    public WorkloadAuction() {
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

	// getters and setters
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public List<AuctionAppInstance> getAppInstances() {
		return appInstances;
	}

	public void setAppInstances(List<AuctionAppInstance> appInstances) {
		this.appInstances = appInstances;
	}

}

class Driver {
	private int numInstances = 1;
	private List<ComputeResource> computeResources = new LinkedList<ComputeResource>();
	private int port = 7500;
	private int portStep = 1;
	private String workloadProfile = "auction";
	private String jvmOpts = "-Xmx2g -Xms2g";
	private int maxConnPerUser = 4;
	private int threads = 0; // default to numCpus
	private int httpThreads = 0; // default to 4*numCpus
	private String cpuRequest = "1500m";
	private String cpuLimit = "2";
	private String memRequest = "2Gi";
	private String memLimit = "7Gi";
    
	// getters and setters
	public List<ComputeResource> getComputeResources() {
		return computeResources;
	}

	public void setComputeResources(List<ComputeResource> computeResources) {
		this.computeResources = computeResources;
	}

	public int getNumInstances() {
		return numInstances;
	}

	public void setNumInstances(int numInstances) {
		this.numInstances = numInstances;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPortStep() {
		return portStep;
	}

	public void setPortStep(int portStep) {
		this.portStep = portStep;
	}

	public String getWorkloadProfile() {
		return workloadProfile;
	}

	public void setWorkloadProfile(String workloadProfile) {
		this.workloadProfile = workloadProfile;
	}

	public String getJvmOpts() {
		return jvmOpts;
	}

	public void setJvmOpts(String jvmOpts) {
		this.jvmOpts = jvmOpts;
	}

	public int getMaxConnPerUser() {
		return maxConnPerUser;
	}

	public void setMaxConnPerUser(int maxConnPerUser) {
		this.maxConnPerUser = maxConnPerUser;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public int getHttpThreads() {
		return httpThreads;
	}

	public void setHttpThreads(int httpThreads) {
		this.httpThreads = httpThreads;
	}

	public String getCpuRequest() {
		return cpuRequest;
	}

	public void setCpuRequest(String cpuRequest) {
		this.cpuRequest = cpuRequest;
	}

	public String getCpuLimit() {
		return cpuLimit;
	}

	public void setCpuLimit(String cpuLimit) {
		this.cpuLimit = cpuLimit;
	}

	public String getMemRequest() {
		return memRequest;
	}

	public void setMemRequest(String memRequest) {
		this.memRequest = memRequest;
	}

	public String getMemLimit() {
		return memLimit;
	}

	public void setMemLimit(String memLimit) {
		this.memLimit = memLimit;
	}

}

class AuctionAppInstance {
	private List<String> loadPath = new LinkedList<String>();
	private boolean repeatLoadPath = true;
	private ComputeResource computeResource;
	private String imageStoreType = "mongodb";
	private List<AuctionService> services = new LinkedList<AuctionService>();
	
	// getters and setters
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

	public ComputeResource getComputeResource() {
		return computeResource;
	}

	public void setComputeResource(ComputeResource computeResource) {
		this.computeResource = computeResource;
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
	
}
