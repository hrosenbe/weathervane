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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RunStrategyFindMaxMultiRun extends RunStrategy{
	// same as Fixed
	private int users = 1000;
	private String runLength = "short";
	private long rampUp = 600;
	private long steadyState = 900;
	private long rampDown = 120;
	
	// type specific
	private long initialRateStep = 500;
	private long minRateStep = 250;
	private int repeatsAtMax = 0;

	// getters and setters
	public int getUsers() {
		return users;
	}

	public void setUsers(int users) {
		this.users = users;
	}

	public String getRunLength() {
		return runLength;
	}

	public void setRunLength(String runLength) {
		this.runLength = runLength;
	}

	public long getRampUp() {
		return rampUp;
	}

	public void setRampUp(long rampUp) {
		this.rampUp = rampUp;
	}

	public long getSteadyState() {
		return steadyState;
	}

	public void setSteadyState(long steadyState) {
		this.steadyState = steadyState;
	}

	public long getRampDown() {
		return rampDown;
	}

	public void setRampDown(long rampDown) {
		this.rampDown = rampDown;
	}

	public long getInitialRateStep() {
		return initialRateStep;
	}

	public void setInitialRateStep(long initialRateStep) {
		this.initialRateStep = initialRateStep;
	}

	public long getMinRateStep() {
		return minRateStep;
	}

	public void setMinRateStep(long minRateStep) {
		this.minRateStep = minRateStep;
	}

	public int getRepeatsAtMax() {
		return repeatsAtMax;
	}

	public void setRepeatsAtMax(int repeatsAtMax) {
		this.repeatsAtMax = repeatsAtMax;
	}

}
