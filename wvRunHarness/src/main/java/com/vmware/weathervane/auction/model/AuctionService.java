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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.vmware.weathervane.auction.runtime.ServiceInstance;

@JsonTypeInfo(use = com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
		@Type(value = AuctionAppServer.class, name = "auctionAppServer"),
		@Type(value = AuctionBidServer.class, name = "auctionBidServer"),
		@Type(value = AuctionCoordinationServer.class, name = "auctionCoordinationServer"),
		@Type(value = AuctionDataManager.class, name = "auctionDataManager"),
		@Type(value = AuctionDbServer.class, name = "auctionDbServer"),
		@Type(value = AuctionMsgServer.class, name = "auctionMsgServer"),
		@Type(value = AuctionNosqlServer.class, name = "auctionNosqlServer"),
		@Type(value = AuctionWebServer.class, name = "auctionWebServer"),
})

public abstract class AuctionService extends Service {
	// RunConfiguration fields
	private List<String> computeResourceNames = new LinkedList<String>();

	// RunConfiguration getters and setters
	public abstract int getNumInstances();
	public abstract void setNumInstances(int numInstances);

	public List<String> getComputeResourceNames() {
		return computeResourceNames;
	}

	public void setComputeResourceNames(List<String> computeResourceNames) {
		this.computeResourceNames = computeResourceNames;
	}

	// RunTime
	protected Map<String, ComputeResource> computeResourcesMap;
	protected List<ServiceInstance> instances = new ArrayList<>();
	private String appInstanceComputeResourceName;
	protected String kubernetesNamespace;

	public void setComputeResourceMap(String appInstanceComputeResourceName, Map<String, ComputeResource> computeResourcesMap) {
		this.appInstanceComputeResourceName = appInstanceComputeResourceName;
		this.computeResourcesMap = computeResourcesMap;
	}

	public void setKubernetesNamespace(String ns) {
		this.kubernetesNamespace = ns;
	}

	public abstract String getTierType();
	public abstract String getServiceType();

	@Override
	public abstract String configure();


	@Override
	public void start() throws IOException
	{
		int nameIndex = 0;
		//create instances and assign computeResources
		for (int i = 0; i < getNumInstances(); i++)
		{
			ComputeResource computeResource = null;
			ComputeResource appInstanceComputeResource = computeResourcesMap.get(appInstanceComputeResourceName);
			if (appInstanceComputeResource instanceof KubernetesComputeResource && getComputeResourceNames().size() > 0) {
				System.out.println("ERROR AuctionService start appInstance ComputeResource instanceof Kubernetes with ComputeResourceNames specified in service");
				return;
			}
			if (getComputeResourceNames().size() > 0) {
				computeResource = computeResourcesMap.get(getComputeResourceNames().get(nameIndex));
				if (computeResource == null) {
					System.out.println("ERROR AuctionService start computeResourceName not matched in map");
					return;
				}
				if (computeResource instanceof KubernetesComputeResource) {
					System.out.println("ERROR AuctionService start instanceof KubernetesComputeResource with names specified");
					return;
				}
			} else if (appInstanceComputeResource == null) {
				System.out.println("ERROR AuctionService start null computeResource");
				return;
			} else {
				computeResource = appInstanceComputeResource;
			}
			ServiceInstance instance = new ServiceInstance();
			instances.add(instance);
			instance.setComputeResource(computeResource);

			nameIndex++;
			if (nameIndex >= getComputeResourceNames().size()) {
				nameIndex = 0;
			}
		} // NumInstances

		// Use the first instance of the service for starting the service instances
		// TODO need to loop through instances for docker and vm?
		//for (ServiceInstance instance: instances) {
		if (instances.size() > 0) {
			ServiceInstance instance = instances.get(0);
			if (instance.getComputeResource() instanceof KubernetesComputeResource) {
				//create yaml file
				//TODO actually create the yaml files instead of reusing existing perl created versions
				String fileName = configure();
				if (fileName == null) {
					System.out.println("ERROR AuctionService start fileName is null, skipping kubernetesApply for "+this+" "+instance);
					return;
				}

				try {
					KubernetesComputeResource kcr = (KubernetesComputeResource)(instance.getComputeResource());
					kcr.kubernetesApply(fileName, kubernetesNamespace);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			} else if (instance.getComputeResource() instanceof DockerComputeResource) {
				System.out.println("ERROR AuctionService start docker NYI");
				return;
			} else {
				System.out.println("ERROR AuctionService start unknown computeResource type");
				return;
			}
		}
	}


	@Override
	public boolean areRunning() throws IOException {
		//TODO need to loop through instances for docker and vm?
		//for (ServiceInstance instance: instances) {
		if (instances.size() > 0) {
			ServiceInstance instance = instances.get(0);
			ComputeResource computeResource = instance.getComputeResource();
			if (computeResource instanceof KubernetesComputeResource) {
				if (getServiceType() == null) {
					System.out.println("ERROR AuctionService areRunning getServiceType is null, skipping kubernetesAreAllPodRunning for "+this+" "+instance);
					return true; //TODO
				}

				try {
					KubernetesComputeResource kcr = (KubernetesComputeResource) computeResource;
					boolean result = kcr.kubernetesAreAllPodRunning("type="+getServiceType(), kubernetesNamespace );
					if (result == false) {
						return false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					return false;
				}
			} else if (computeResource instanceof DockerComputeResource) {
				System.out.println("ERROR AuctionService areRunning docker NYI");
				return false;
			} else {
				System.out.println("ERROR AuctionService areRunning unknown computeResource type");
				return false;
			}
		}
		return true;
	}


	@Override
	public abstract boolean areUp() throws IOException;


	@Override
	public void stop() throws IOException {
		//TODO need to loop through instances for docker and vm?
		//for (ServiceInstance instance: instances) {
		if (instances.size() > 0) {
			ServiceInstance instance = instances.get(0);
			ComputeResource computeResource = instance.getComputeResource();
			if (computeResource instanceof KubernetesComputeResource) {
				if (getServiceType() == null) {
					System.out.println("ERROR AuctionService stop getServiceType is null, skipping kubernetesDeleteAll... for "+this+" "+instance);
					return;
				}

				try {
					KubernetesComputeResource kcr = (KubernetesComputeResource) computeResource;
					kcr.kubernetesDeleteAllWithLabel("type="+getServiceType(), kubernetesNamespace);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (instance.getComputeResource() instanceof DockerComputeResource) {
				System.out.println("ERROR AuctionService stop docker NYI");
				return;
			} else {
				System.out.println("ERROR AuctionService stop unknown computeResource type");
				return;
			}
		}
	}

}
