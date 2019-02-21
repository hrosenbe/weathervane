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

import java.io.IOException;

import com.vmware.weathervane.auction.runtime.WeathervaneTypes;

public class AuctionWebServer extends AuctionService {
	// RunConfiguration fields
	private String impl = "nginx"; // leave the possibility of different service implementations
	private String cpuRequest = "1500m";
	private String cpuLimit = "2";
	private String memRequest = "2Gi";
	private String memLimit = "7Gi";
	private String dockerCpuSetCpus = "1-4";
	private String  dockerNet = "bridge";
	private int httpPort = 80;
	private int httpsPort = 443;
	private int portOffset = 9000;
	private int portStep = 1;

	// Service-specific fields
	private boolean keepalive = true;
	private long keepaliveTimeout = 120;
	private int nginxWorkerConnections = 0;

	// These are only relevant when running on a docker host
	private boolean useNamedVolumes = false;
	private String cacheVolumeName = "nginxCache";
	private String cacheVolumeSize = "10Gi";

	public AuctionWebServer() {
		super();
		numInstances = 2;
	}

	// RunConfiguration getters and setters
	public String getImpl() {
		return impl;
	}

	public void setImpl(String impl) {
		this.impl = impl;
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

	public String getDockerCpuSetCpus() {
		return dockerCpuSetCpus;
	}

	public void setDockerCpuSetCpus(String dockerCpuSetCpus) {
		this.dockerCpuSetCpus = dockerCpuSetCpus;
	}

	public String getDockerNet() {
		return dockerNet;
	}

	public void setDockerNet(String dockerNet) {
		this.dockerNet = dockerNet;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public int getHttpsPort() {
		return httpsPort;
	}

	public void setHttpsPort(int httpsPort) {
		this.httpsPort = httpsPort;
	}

	public int getPortOffset() {
		return portOffset;
	}

	public void setPortOffset(int portOffset) {
		this.portOffset = portOffset;
	}

	public int getPortStep() {
		return portStep;
	}

	public void setPortStep(int portStep) {
		this.portStep = portStep;
	}

	public boolean isKeepalive() {
		return keepalive;
	}

	public void setKeepalive(boolean keepalive) {
		this.keepalive = keepalive;
	}

	public long getKeepaliveTimeout() {
		return keepaliveTimeout;
	}

	public void setKeepaliveTimeout(long keepaliveTimeout) {
		this.keepaliveTimeout = keepaliveTimeout;
	}

	public int getNginxWorkerConnections() {
		return nginxWorkerConnections;
	}

	public void setNginxWorkerConnections(int nginxWorkerConnections) {
		this.nginxWorkerConnections = nginxWorkerConnections;
	}

	public boolean isUseNamedVolumes() {
		return useNamedVolumes;
	}

	public void setUseNamedVolumes(boolean useNamedVolumes) {
		this.useNamedVolumes = useNamedVolumes;
	}

	public String getCacheVolumeName() {
		return cacheVolumeName;
	}

	public void setCacheVolumeName(String cacheVolumeName) {
		this.cacheVolumeName = cacheVolumeName;
	}

	public String getCacheVolumeSize() {
		return cacheVolumeSize;
	}

	public void setCacheVolumeSize(String cacheVolumeSize) {
		this.cacheVolumeSize = cacheVolumeSize;
	}

	// RunTime
	private final String tierType = WeathervaneTypes.tierFrontEnd;
	private final String serviceType = null; //TODO WeathervaneTypes.webServer;
	private final String serviceImpl = "nginx"; //TODO

	@Override
	public String getTierType() {
		return tierType;
	}

	@Override
	public String getServiceType() {
		return serviceType;
	}

	@Override
	public String configure() {
		return null;
		//TODO return "/tmp/"+serviceImpl+"-" + kubernetesNamespace + ".yaml";
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean areUp() throws IOException {
		// TODO Auto-generated method stub
		return true;
	}

}
