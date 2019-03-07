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

public class AuctionCoordinationServer extends AuctionService {
	// RunConfiguration fields
	private int numInstances = 3;
	private int zkClientPort = 2181;
	private int zkPeerPort = 2888;
	private int zkElectionPort = 3888;
	private int portStep = 1;

	// RunConfiguration getters and setters
	public int getNumInstances() {
		return numInstances;
	}

	public void setNumInstances(int numInstances) {
		this.numInstances = numInstances;
	}

	public int getZkClientPort() {
		return zkClientPort;
	}

	public void setZkClientPort(int zkClientPort) {
		this.zkClientPort = zkClientPort;
	}

	public int getZkPeerPort() {
		return zkPeerPort;
	}

	public void setZkPeerPort(int zkPeerPort) {
		this.zkPeerPort = zkPeerPort;
	}

	public int getZkElectionPort() {
		return zkElectionPort;
	}

	public void setZkElectionPort(int zkElectionPort) {
		this.zkElectionPort = zkElectionPort;
	}

	public int getPortStep() {
		return portStep;
	}

	public void setPortStep(int portStep) {
		this.portStep = portStep;
	}

	// RunTime
	private final String tierType = WeathervaneTypes.tierData;
	private final String serviceType = WeathervaneTypes.typeCoordinationServer;
	private final String serviceImpl = "zookeeper"; //TODO

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
					String result = kcr.kubernetesExecOne(serviceImpl, Arrays.asList(new String[] {"/bin/sh", "-c", "[ \"imok\" = \"$(echo ruok | nc -w 1 127.0.0.1 2181)\" ]"}), kubernetesNamespace);
					if (result == null) {
						return false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					return false;
				}
			} else if (computeResource instanceof DockerComputeResource) {
				System.out.println("ERROR AuctionCoordServer areUp docker NYI");
				return false;
			} else {
				System.out.println("ERROR AuctionCoordServer areUp unknown computeResource type");
				return false;
			}
		}
		return true;
	}

}
