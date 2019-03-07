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
import java.util.Arrays;

import com.vmware.weathervane.auction.runtime.ServiceInstance;
import com.vmware.weathervane.auction.runtime.WeathervaneTypes;

public class AuctionDbServer extends AuctionService {
	// RunConfiguration fields
	private int numInstances = 1;
	private int postgresqlPort = 5432;
	private int portStep = 1;
	private int postgresqlSharedBuffers = 0;
	private double postgresqlSharedBuffersPct = 0.25;
	private int postgresqlEffectiveCacheSize = 0;
	private double postgresqlEffectiveCacheSizePct = 0.65;
	private int postgresqlMaxConnections = 0;

	// Used for Docker
	private boolean useNamedVolumes = false;
	private String dataVolumeName = "postgresqlData";
	private String dataVolumeSize = "20Gi";
	private String logVolumeName = "postgresqlLogs";
	private String logVolumeSize = "20Gi";

	// Used for Kubernetes
	private String dataStorageClass = "fast";
	private String logStorageClass = "fast";

	// RunConfiguration getters and setters
	public int getNumInstances() {
		return numInstances;
	}

	public void setNumInstances(int numInstances) {
		this.numInstances = numInstances;
	}

	public int getPostgresqlPort() {
		return postgresqlPort;
	}

	public void setPostgresqlPort(int postgresqlPort) {
		this.postgresqlPort = postgresqlPort;
	}

	public int getPortStep() {
		return portStep;
	}

	public void setPortStep(int portStep) {
		this.portStep = portStep;
	}

	public int getPostgresqlSharedBuffers() {
		return postgresqlSharedBuffers;
	}

	public void setPostgresqlSharedBuffers(int postgresqlSharedBuffers) {
		this.postgresqlSharedBuffers = postgresqlSharedBuffers;
	}

	public double getPostgresqlSharedBuffersPct() {
		return postgresqlSharedBuffersPct;
	}

	public void setPostgresqlSharedBuffersPct(double postgresqlSharedBuffersPct) {
		this.postgresqlSharedBuffersPct = postgresqlSharedBuffersPct;
	}

	public int getPostgresqlEffectiveCacheSize() {
		return postgresqlEffectiveCacheSize;
	}

	public void setPostgresqlEffectiveCacheSize(int postgresqlEffectiveCacheSize) {
		this.postgresqlEffectiveCacheSize = postgresqlEffectiveCacheSize;
	}

	public double getPostgresqlEffectiveCacheSizePct() {
		return postgresqlEffectiveCacheSizePct;
	}

	public void setPostgresqlEffectiveCacheSizePct(double postgresqlEffectiveCacheSizePct) {
		this.postgresqlEffectiveCacheSizePct = postgresqlEffectiveCacheSizePct;
	}

	public int getPostgresqlMaxConnections() {
		return postgresqlMaxConnections;
	}

	public void setPostgresqlMaxConnections(int postgresqlMaxConnections) {
		this.postgresqlMaxConnections = postgresqlMaxConnections;
	}

	public boolean isUseNamedVolumes() {
		return useNamedVolumes;
	}

	public void setUseNamedVolumes(boolean useNamedVolumes) {
		this.useNamedVolumes = useNamedVolumes;
	}

	public String getDataVolumeName() {
		return dataVolumeName;
	}

	public void setDataVolumeName(String dataVolumeName) {
		this.dataVolumeName = dataVolumeName;
	}

	public String getDataVolumeSize() {
		return dataVolumeSize;
	}

	public void setDataVolumeSize(String dataVolumeSize) {
		this.dataVolumeSize = dataVolumeSize;
	}

	public String getLogVolumeName() {
		return logVolumeName;
	}

	public void setLogVolumeName(String logVolumeName) {
		this.logVolumeName = logVolumeName;
	}

	public String getLogVolumeSize() {
		return logVolumeSize;
	}

	public void setLogVolumeSize(String logVolumeSize) {
		this.logVolumeSize = logVolumeSize;
	}

	public String getDataStorageClass() {
		return dataStorageClass;
	}

	public void setDataStorageClass(String dataStorageClass) {
		this.dataStorageClass = dataStorageClass;
	}

	public String getLogStorageClass() {
		return logStorageClass;
	}

	public void setLogStorageClass(String logStorageClass) {
		this.logStorageClass = logStorageClass;
	}


	// RunTime
	private final String tierType = WeathervaneTypes.tierData;
	private final String serviceType = WeathervaneTypes.typeDbServer;
	private final String serviceImpl = "postgresql"; //TODO

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
		return "/tmp/"+serviceImpl+"-" + kubernetesNamespace + ".yaml";
	}

	@Override
	public void start() throws IOException {
		super.start();
	}

	@Override
	public boolean areUp() throws IOException {
		//TODO need to loop through instances for docker and vm?
		//for (ServiceInstance instance: instances) {
		if (instances.size() > 0) {
			ServiceInstance instance = instances.get(0);
			ComputeResource computeResource = instance.getComputeResource();
			if (computeResource instanceof KubernetesComputeResource) {
				try {
					KubernetesComputeResource kcr = (KubernetesComputeResource) computeResource;
					String result = kcr.kubernetesExecOne(serviceImpl, Arrays.asList("/usr/pgsql-9.3/bin/pg_isready -h 127.0.0.1 -p 5432".split("\\s+")), kubernetesNamespace);
					if (result == null) {
						return false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					return false;
				}
			} else if (computeResource instanceof DockerComputeResource) {
				System.out.println("ERROR AuctionDbServer areUp docker NYI");
				return false;
			} else {
				System.out.println("ERROR AuctionDbServer areUp unknown computeResource type");
				return false;
			}
		}
		return true;
	}

}
