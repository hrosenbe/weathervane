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

public class AuctionNosqlServer extends AuctionService {
	// RunConfiguration fields
	private boolean replicated = false;
	private boolean sharded = false;
	private int replicasPerShard = 3;
	private int portStep = 100;

	// Used for Docker
	private boolean useNamedVolumes = false;
	private String dataVolumeName = "mongodbData";
	private String dataVolumeSize = "200Gi";
	private String c1DataVolumeName = "mongodbC1Data";
	private String c1DataVolumeSize = "10Gi";
	private String c2DataVolumeName = "mongodbC2Data";
	private String c2DataVolumeSize = "10Gi";
	private String c3DataVolumeName = "mongodbC3Data";
	private String c3DataVolumeSize = "10Gi";

	// Used for Kubernetes
	private String dataStorageClass = "fast";

	private boolean mongodbTouch = true;
	private boolean mongodbTouchFull = false;
	private boolean mongodbTouchPreview = false;

	// RunConfiguration getters and setters
	public boolean isReplicated() {
		return replicated;
	}

	public void setReplicated(boolean replicated) {
		this.replicated = replicated;
	}

	public boolean isSharded() {
		return sharded;
	}

	public void setSharded(boolean sharded) {
		this.sharded = sharded;
	}

	public int getReplicasPerShard() {
		return replicasPerShard;
	}

	public void setReplicasPerShard(int replicasPerShard) {
		this.replicasPerShard = replicasPerShard;
	}

	public int getPortStep() {
		return portStep;
	}

	public void setPortStep(int portStep) {
		this.portStep = portStep;
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

	public String getC1DataVolumeName() {
		return c1DataVolumeName;
	}

	public void setC1DataVolumeName(String c1DataVolumeName) {
		this.c1DataVolumeName = c1DataVolumeName;
	}

	public String getC1DataVolumeSize() {
		return c1DataVolumeSize;
	}

	public void setC1DataVolumeSize(String c1DataVolumeSize) {
		this.c1DataVolumeSize = c1DataVolumeSize;
	}

	public String getC2DataVolumeName() {
		return c2DataVolumeName;
	}

	public void setC2DataVolumeName(String c2DataVolumeName) {
		this.c2DataVolumeName = c2DataVolumeName;
	}

	public String getC2DataVolumeSize() {
		return c2DataVolumeSize;
	}

	public void setC2DataVolumeSize(String c2DataVolumeSize) {
		this.c2DataVolumeSize = c2DataVolumeSize;
	}

	public String getC3DataVolumeName() {
		return c3DataVolumeName;
	}

	public void setC3DataVolumeName(String c3DataVolumeName) {
		this.c3DataVolumeName = c3DataVolumeName;
	}

	public String getC3DataVolumeSize() {
		return c3DataVolumeSize;
	}

	public void setC3DataVolumeSize(String c3DataVolumeSize) {
		this.c3DataVolumeSize = c3DataVolumeSize;
	}

	public String getDataStorageClass() {
		return dataStorageClass;
	}

	public void setDataStorageClass(String dataStorageClass) {
		this.dataStorageClass = dataStorageClass;
	}

	public boolean isMongodbTouch() {
		return mongodbTouch;
	}

	public void setMongodbTouch(boolean mongodbTouch) {
		this.mongodbTouch = mongodbTouch;
	}

	public boolean isMongodbTouchFull() {
		return mongodbTouchFull;
	}

	public void setMongodbTouchFull(boolean mongodbTouchFull) {
		this.mongodbTouchFull = mongodbTouchFull;
	}

	public boolean isMongodbTouchPreview() {
		return mongodbTouchPreview;
	}

	public void setMongodbTouchPreview(boolean mongodbTouchPreview) {
		this.mongodbTouchPreview = mongodbTouchPreview;
	}

	// RunTime
	private final String tierType = WeathervaneTypes.tierData;
	private final String serviceType = null; //TODO WeathervaneTypes.nosqlServer;
	private final String serviceImpl = "mongodb"; //TODO

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
	public void start() throws IOException {
		super.start();
	}

	@Override
	public boolean areUp() throws IOException {
		// TODO Auto-generated method stub
		return true;
	}

}
