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
public class AuctionDriver extends Driver {
	private int numInstances = 1;
	private List<String> computeResourceNames = new LinkedList<String>();
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

	public int getNumInstances() {
		return numInstances;
	}

	public void setNumInstances(int numInstances) {
		this.numInstances = numInstances;
	}

	public List<String> getComputeResourceNames() {
		return computeResourceNames;
	}

	public void setComputeResourceNames(List<String> computeResourceNames) {
		this.computeResourceNames = computeResourceNames;
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