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

public class AuctionDataManager extends AuctionService {
	// RunConfiguration fields
	private int threads = 6;
	private String jvmOpts = "-Xmx4g -Xms4G";

	// RunConfiguration getters and setters
	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public String getJvmOpts() {
		return jvmOpts;
	}

	public void setJvmOpts(String jvmOpts) {
		this.jvmOpts = jvmOpts;
	}

	// RunTime
	private final String tierType = WeathervaneTypes.tierUndefined;
	private final String serviceType = null; //TODO WeathervaneTypes.undefined;
	private final String serviceImpl = "auctiondatamanager"; //TODO

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
