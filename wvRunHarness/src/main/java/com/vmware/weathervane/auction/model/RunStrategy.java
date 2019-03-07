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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.vmware.weathervane.auction.runtime.RunProcedure;

@JsonTypeInfo(use = com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
		@Type(value = FixedRunStrategy.class, name = "fixed"),
		@Type(value = TargetUtilizationRunStrategy.class, name = "targetUtilization"),
		@Type(value = CustomRunStrategy.class, name = "custom"),
		@Type(value = FindMaxSingleAIRunStrategy.class, name = "findMaxSingleAI"),
		@Type(value = FindMaxSingleAIWithScalingRunStrategy.class, name = "findMaxSingleAIWithScaling"),
		@Type(value = FindMaxMultiAIRunStrategy.class, name = "findMaxMultiAI"),
		@Type(value = FindMaxMultiRunStrategy.class, name = "findMaxMultiRun"),
		@Type(value = IntervalRunStrategy.class, name = "interval"),

})

public abstract class RunStrategy {

	// Runtime
	protected RunProcedure runProc;

	public void setRunProcedure(RunProcedure runProc) {
		this.runProc = runProc;
	}

	public void start() {
		System.out.println("debugprint RunStrategy base,  starting run should not happen here.");
		return;
	}

}
