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
public class RunConfiguration {

	// required input fields
	private String dockerNamespace;
	private List<ComputeResource> computeResources = new LinkedList<ComputeResource>();
	
	// optional fields with default values
	private String description = "default description";
	private String configurationSize = "custom";
	
	private long maxDuration = 7200;
	private int maxUsers = 1000;

	private int loglevel = 1; 
	private boolean truncateLogs = true;
	private int maxLogLines = 4000;

	private String resultsFileDir = "";
	private String resultsFileName = "weathervaneResults.csv";
	private boolean showPeriodicOutput = false;

	private boolean redeploy = false;
	private boolean reloadDb = false;
	private boolean stopServices = true;
	private boolean stopOnFailure = true;

	private double responseTimePassingPercentile = 0.99;
	
	private String runProcedure = "full";
	private boolean interactive = false;

	private RunStrategy runStrategy;
	private LinkedList<Workload> workloads = new LinkedList<Workload>();
	private VirtualInfrastructure virtualInfrastructure;

	private String startStatsScript;
	private String stopStatsScript;

	public RunConfiguration() {
		super();
		
		//create a default runStrategy and workload
		runStrategy = new FixedRunStrategy();
		Workload defaultWorkload = new AuctionWorkload();
		workloads.add(defaultWorkload);
	}
	
	// getters and setters
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getConfigurationSize() {
		return configurationSize;
	}

	public void setConfigurationSize(String configurationSize) {
		this.configurationSize = configurationSize;
	}

	public long getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(long maxDuration) {
		this.maxDuration = maxDuration;
	}

	public int getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}

	public String getDockerNamespace() {
		return dockerNamespace;
	}

	public void setDockerNamespace(String dockerNamespace) {
		this.dockerNamespace = dockerNamespace;
	}

	public int getLoglevel() {
		return loglevel;
	}

	public void setLoglevel(int loglevel) {
		this.loglevel = loglevel;
	}

	public boolean isTruncateLogs() {
		return truncateLogs;
	}

	public void setTruncateLogs(boolean truncateLogs) {
		this.truncateLogs = truncateLogs;
	}

	public int getMaxLogLines() {
		return maxLogLines;
	}

	public void setMaxLogLines(int maxLogLines) {
		this.maxLogLines = maxLogLines;
	}

	public String getResultsFileDir() {
		return resultsFileDir;
	}

	public void setResultsFileDir(String resultsFileDir) {
		this.resultsFileDir = resultsFileDir;
	}

	public String getResultsFileName() {
		return resultsFileName;
	}

	public void setResultsFileName(String resultsFileName) {
		this.resultsFileName = resultsFileName;
	}

	public boolean isShowPeriodicOutput() {
		return showPeriodicOutput;
	}

	public void setShowPeriodicOutput(boolean showPeriodicOutput) {
		this.showPeriodicOutput = showPeriodicOutput;
	}

	public boolean isRedeploy() {
		return redeploy;
	}

	public void setRedeploy(boolean redeploy) {
		this.redeploy = redeploy;
	}

	public boolean isReloadDb() {
		return reloadDb;
	}

	public void setReloadDb(boolean reloadDb) {
		this.reloadDb = reloadDb;
	}

	public boolean isStopServices() {
		return stopServices;
	}

	public void setStopServices(boolean stopServices) {
		this.stopServices = stopServices;
	}

	public boolean isStopOnFailure() {
		return stopOnFailure;
	}

	public void setStopOnFailure(boolean stopOnFailure) {
		this.stopOnFailure = stopOnFailure;
	}

	public double getResponseTimePassingPercentile() {
		return responseTimePassingPercentile;
	}

	public void setResponseTimePassingPercentile(double responseTimePassingPercentile) {
		this.responseTimePassingPercentile = responseTimePassingPercentile;
	}

	public RunStrategy getRunStrategy() {
		return runStrategy;
	}

	public void setRunStrategy(RunStrategy runStrategy) {
		this.runStrategy = runStrategy;
	}

	public String getRunProcedure() {
		return runProcedure;
	}

	public void setRunProcedure(String runProcedure) {
		this.runProcedure = runProcedure;
	}

	public boolean isInteractive() {
		return interactive;
	}

	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}

	public List<ComputeResource> getComputeResources() {
		return computeResources;
	}

	public void setComputeResources(List<ComputeResource> computeResources) {
		this.computeResources = computeResources;
	}

	public LinkedList<Workload> getWorkloads() {
		return workloads;
	}

	public void setWorkloads(LinkedList<Workload> workloads) {
		this.workloads = workloads;
	}

	public VirtualInfrastructure getVirtualInfrastructure() {
		return virtualInfrastructure;
	}

	public void setVirtualInfrastructure(VirtualInfrastructure virtualInfrastructure) {
		this.virtualInfrastructure = virtualInfrastructure;
	}

	public String getStartStatsScript() {
		return startStatsScript;
	}

	public void setStartStatsScript(String startStatsScript) {
		this.startStatsScript = startStatsScript;
	}

	public String getStopStatsScript() {
		return stopStatsScript;
	}

	public void setStopStatsScript(String stopStatsScript) {
		this.stopStatsScript = stopStatsScript;
	}

}
