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

public class AuctionAppServer extends AuctionService {
	// RunConfiguration fields
	private int numInstances = 1;
	private int threads = 49;
	private int jdbcConnections = 50;
	private String jvmOpts = "-Xms2g -Xmx2g";
	private int httpPort = 80;
	private int httpsPort = 443;
	private int portOffset = 8000;
	private int portStep = 1;

	// These are tuning knobs for the internals of the auctionApp
	private boolean randomizeImages = true;
	private boolean useImageWriterThreads = true;
	private int numImageWriterThreads = 1;
	private int numClientUpdateThreads = 2;
	private int numAuctioneerThreads = 2;
	private int highBidQueueConcurrency = 1;
	private int newBidQueueConcurrency = 1;
	private boolean prewarmAppServers = true;
	private int thumbnailImageCacheSizeMultiplier = 25;
	private int previewImageCacheSizeMultiplier = 25;
	private int fullImageCacheSizeMultiplier = 25;

	// RunConfiguration getters and setters
	public int getNumInstances() {
		return numInstances;
	}

	public void setNumInstances(int numInstances) {
		this.numInstances = numInstances;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public int getJdbcConnections() {
		return jdbcConnections;
	}

	public void setJdbcConnections(int jdbcConnections) {
		this.jdbcConnections = jdbcConnections;
	}

	public String getJvmOpts() {
		return jvmOpts;
	}

	public void setJvmOpts(String jvmOpts) {
		this.jvmOpts = jvmOpts;
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

	public boolean isRandomizeImages() {
		return randomizeImages;
	}

	public void setRandomizeImages(boolean randomizeImages) {
		this.randomizeImages = randomizeImages;
	}

	public boolean isUseImageWriterThreads() {
		return useImageWriterThreads;
	}

	public void setUseImageWriterThreads(boolean useImageWriterThreads) {
		this.useImageWriterThreads = useImageWriterThreads;
	}

	public int getNumImageWriterThreads() {
		return numImageWriterThreads;
	}

	public void setNumImageWriterThreads(int numImageWriterThreads) {
		this.numImageWriterThreads = numImageWriterThreads;
	}

	public int getNumClientUpdateThreads() {
		return numClientUpdateThreads;
	}

	public void setNumClientUpdateThreads(int numClientUpdateThreads) {
		this.numClientUpdateThreads = numClientUpdateThreads;
	}

	public int getNumAuctioneerThreads() {
		return numAuctioneerThreads;
	}

	public void setNumAuctioneerThreads(int numAuctioneerThreads) {
		this.numAuctioneerThreads = numAuctioneerThreads;
	}

	public int getHighBidQueueConcurrency() {
		return highBidQueueConcurrency;
	}

	public void setHighBidQueueConcurrency(int highBidQueueConcurrency) {
		this.highBidQueueConcurrency = highBidQueueConcurrency;
	}

	public int getNewBidQueueConcurrency() {
		return newBidQueueConcurrency;
	}

	public void setNewBidQueueConcurrency(int newBidQueueConcurrency) {
		this.newBidQueueConcurrency = newBidQueueConcurrency;
	}

	public boolean isPrewarmAppServers() {
		return prewarmAppServers;
	}

	public void setPrewarmAppServers(boolean prewarmAppServers) {
		this.prewarmAppServers = prewarmAppServers;
	}

	public int getThumbnailImageCacheSizeMultiplier() {
		return thumbnailImageCacheSizeMultiplier;
	}

	public void setThumbnailImageCacheSizeMultiplier(int thumbnailImageCacheSizeMultiplier) {
		this.thumbnailImageCacheSizeMultiplier = thumbnailImageCacheSizeMultiplier;
	}

	public int getPreviewImageCacheSizeMultiplier() {
		return previewImageCacheSizeMultiplier;
	}

	public void setPreviewImageCacheSizeMultiplier(int previewImageCacheSizeMultiplier) {
		this.previewImageCacheSizeMultiplier = previewImageCacheSizeMultiplier;
	}

	public int getFullImageCacheSizeMultiplier() {
		return fullImageCacheSizeMultiplier;
	}

	public void setFullImageCacheSizeMultiplier(int fullImageCacheSizeMultiplier) {
		this.fullImageCacheSizeMultiplier = fullImageCacheSizeMultiplier;
	}

	// RunTime
	private final String tierType = WeathervaneTypes.tierBackEnd;
	private final String serviceType = null; //TODO WeathervaneTypes.appServer;
	private final String serviceImpl = "tomcat"; //TODO

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
