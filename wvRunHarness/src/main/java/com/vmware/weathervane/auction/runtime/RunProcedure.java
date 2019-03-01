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
package com.vmware.weathervane.auction.runtime;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.vmware.weathervane.auction.model.AuctionAppInstance;
import com.vmware.weathervane.auction.model.AuctionService;
import com.vmware.weathervane.auction.model.AuctionWorkload;
import com.vmware.weathervane.auction.model.ComputeResource;
import com.vmware.weathervane.auction.model.RunConfiguration;
import com.vmware.weathervane.auction.model.Workload;

public class RunProcedure {

	//private String type = "full";
	RunConfiguration runConfig;
	Map<String, ComputeResource> computeResourcesMap;

	public RunProcedure(RunConfiguration runConfig) {
		super();
		//this.type = runConfig.getRunProcedure();
		this.runConfig = runConfig;
	}

	public void run() {
		System.out.println("debugprint RunProcedure.run.....");

		computeResourcesMap = new HashMap<>();
		for (ComputeResource cr : runConfig.getComputeResources()) {
			computeResourcesMap.put(cr.getName(), cr);
		}

		//FullRunProcedure super() to PrepareOnlyRunProcedure
		//seqnum
		//clean up tmpdir
		//mkdir setuplogs

		//loggers
		//power control
		//getCpuMemConfig

		//killOldWorkloadDriver

		//stopServices... cleanup
		//redeploy
		//checkversions


		//prepareData
		String tierType = WeathervaneTypes.tierData;
		for (Workload workload : runConfig.getWorkloads()) {
			System.out.println("debugprint RunProcedure preparedata workload "+workload);
			if (workload instanceof AuctionWorkload){
				AuctionWorkload auctionWorkload = ((AuctionWorkload) workload);
				for (AuctionAppInstance appInstance : auctionWorkload.getAppInstances()) {
					System.out.println("debugprint RunProcedure preparedata appinstance "+appInstance);
					//TODO need to implement prepareData
					DataManager dataManager = new DataManager();
					appInstance.setDataManager(dataManager);
					appInstance.getDataManager().prepareData();
				} // appInstance
			} // instance of
		} // workload

		//maybe cleanupAfterFailure

		//clearReloadDb

		try {
			tierType = WeathervaneTypes.tierData;
			startServicesTier(tierType);
			//setExternalPortNumbers

			//pretouchdata

			tierType = WeathervaneTypes.tierBackEnd;
			startServicesTier(tierType);
			//setExternalPortNumbers

			tierType = WeathervaneTypes.tierFrontEnd;
			startServicesTier(tierType);
			//setExternalPortNumbers

			tierType = WeathervaneTypes.tierInfrastructure;
			startServicesTier(tierType);
			//setExternalPortNumbers

			waitForServicesUp();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return;
		}


		//configureWorkloadDrivers
		//initializeWorkloadsDrivers
		//runWorkloads


		try {
			long duration = 20000;
			System.out.println("debugprint RunProcedure sleep between start and stop "+duration);
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//get config,stats,logs

		try {
			//TODO if stopservices
			{
				stopServices(); //all tiers

//	 			$sanityPassed = $self->sanityCheckServices($cleanupLogDir);
//				$self->unRegisterPortNumbers();

//				$sanityPassed = $self->sanityCheckServices($cleanupLogDir);

//				# Let the appInstances clean any run specific data or services
//				$self->cleanupAppInstances($cleanupLogDir);

//				# clean up old logs and stats
//				$self->cleanup();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		//save more stuff

		//stopWorkloads

	}


	void startServicesTier(String tierType) throws IOException, InterruptedException {
		int workloadNum = 1;
		for (Workload workload : runConfig.getWorkloads()) {
			if (workload instanceof AuctionWorkload) {
				AuctionWorkload auctionWorkload = ((AuctionWorkload) workload);
				int appInstanceNum = 1;
				for (AuctionAppInstance appInstance : auctionWorkload.getAppInstances()) {
					String kubernetesNamespace = "auctionw" + workloadNum + "i" + appInstanceNum;
					for (AuctionService auctionService : appInstance.getServices()) {
						// only start the right types for the tier
						if (auctionService.getTierType().equals(tierType)) {
							System.out.println("debugprint RunProcedure startServices(" + tierType + ") " + auctionService);
							auctionService.setKubernetesNamespace(kubernetesNamespace);
							auctionService.setComputeResourceMap(appInstance.getComputeResourceName(), computeResourcesMap);
							auctionService.start();
						}
					} // auctionService

					boolean done = false;
					int retries = 10;
					long initialSleep = 1500; // 15000; //TODO
					long waitSleep = 15000;

					System.out.println("debugprint RunProcedure areRunning initialSleep " + initialSleep);
					Thread.sleep(initialSleep);
					while (!done && retries > 0) {
						System.out.println("debugprint RunProcedure areRunning loop " + retries);
						done = true;
						for (AuctionService auctionService : appInstance.getServices()) {
							if (auctionService.getTierType().equals(tierType)) {
								boolean result = auctionService.areRunning();
								System.out.println("debugprint RunProcedure areRunning " + auctionService + " " + result);
								if (result == false) {
									done = false;
									break;
								}
							}
						} // auctionService
						retries--;
						if (!done && retries > 0) {
							System.out.println("debugprint RunProcedure areRunning waitSleep " + waitSleep);
							Thread.sleep(waitSleep);
						}
					} // while
					appInstanceNum++;
				} // appInstance
			} // instance of
			workloadNum++;
		} // workload
	}


	void stopServices() throws IOException {
		String[] tierTypes = {WeathervaneTypes.tierFrontEnd, WeathervaneTypes.tierBackEnd, WeathervaneTypes.tierData, WeathervaneTypes.tierInfrastructure};
		for (Workload workload : runConfig.getWorkloads()) {
			if (workload instanceof AuctionWorkload){
				AuctionWorkload auctionWorkload = ((AuctionWorkload) workload);
				for (AuctionAppInstance appInstance : auctionWorkload.getAppInstances()) {
					for (String stopTierType : tierTypes) {
						for (AuctionService auctionService : appInstance.getServices()) {
							if (auctionService.getTierType().equals(stopTierType)) {
								System.out.println("debugprint RunProcedure stopServices " + "(" + stopTierType + ") " + auctionService);
								auctionService.stop();
							}
						} // auctionService
					} // tierTypes
				} // appInstance
			} // instance of
		} // workload
	}


	void waitForServicesUp() throws InterruptedException, IOException {
		String[] tierTypes = { WeathervaneTypes.tierData, WeathervaneTypes.tierBackEnd, WeathervaneTypes.tierFrontEnd, WeathervaneTypes.tierInfrastructure };
		for (Workload workload : runConfig.getWorkloads()) {
			if (workload instanceof AuctionWorkload) {
				AuctionWorkload auctionWorkload = ((AuctionWorkload) workload);
				for (AuctionAppInstance appInstance : auctionWorkload.getAppInstances()) {
					boolean done = false;
					int retries = 12;
					long initialSleep = 1500; // 15000; //TODO
					long waitSleep = 15000;

					System.out.println("debugprint RunProcedure areUp initialSleep " + initialSleep);
					Thread.sleep(initialSleep);
					while (!done && retries > 0) {
						System.out.println("debugprint RunProcedure areUp loop " + retries);
						done = true;
						for (String stopTierType : tierTypes) {
							for (AuctionService auctionService : appInstance.getServices()) {
								if (auctionService.getTierType().equals(stopTierType)) {
									boolean result;
									result = auctionService.areUp();
									System.out.println("debugprint RunProcedure areUp " + auctionService + " " + result);
									if (result == false) {
										done = false;
										break;
									}
								} // tierType
							} // auctionService
						} // tierTypes
						retries--;
						if (!done && retries > 0) {
							System.out.println("debugprint RunProcedure areUp waitSleep " + waitSleep);
							Thread.sleep(waitSleep);
						}
					}
				} // appInstance
			} // instance of
		} // workload
	}
}
