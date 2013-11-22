package com.cs.umbc.project.server.cccproject.survivalCurve;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cs.umbc.project.server.cccproject.common.Patient;
import com.cs.umbc.project.server.cccproject.common.PatientComparator;
import com.cs.umbc.project.server.cccproject.myLogRankTest.Event;


public class KaplanMeierEstimate {
	
	
	public KaplanMeierEstimate(){
		
	}
    /**
     * The Kaplan Meier estimates,
     * estimate[i]: the estimate corresponding to the survival time of the
     *                  (i+1)'th patient.
     */
	public double[] estimate;
    
	/**
     * The survival times of the patients.
     */
    public double[] time;

    /**
     * The censor indicator for the patients,
     * censor[i]=1: death;
     * censor[i]=0: censored.
     */
    public double[] censor;

	public KaplanMeierEstimate(double[] time, double[] censor) {
		this.time=time;
		this.censor=censor;
		estimate = estimate();
	}

	public double[] estimate() {

		estimate = new double[time.length];
		Map<Double, Event> events= createEvents();
		double prev=time[0];
		estimate[0]=events.get(prev).proportionSurvival;
		for (int i = 1; i < time.length; i++) {
			
			if(time[i]==prev){
				estimate[i]=estimate[i-1];
			}else{
				estimate[i]=estimate[i-1]*events.get(time[i]).proportionSurvival;
				prev=time[i];
			}
			
//			System.out.println("kk "+i+" " +estimate[i]+" "+time[i]);
			
		}
		return estimate;
	}
	
	private Map<Double, Event> createEvents() {
		// change to unique survival days array
		Patient[] allPatients=getSortedGroup();
		
		Map<Double, Event> map = new HashMap<Double, Event>();
		for (int i = 0; i < allPatients.length; i++) {
			Patient patient = allPatients[i];
			double key = patient.time;
			double censor = patient.censor;

			Event event;
			double currTotal=allPatients.length - i;
			if (!map.containsKey(key)) {// if survival day exist in map
				event = new Event(key, currTotal, censor);
			} else {
				event = map.get(key);
				event.ndeath += censor;
				event.proportionSurvival=(event.currTotal-event.ndeath)/event.currTotal;
			}
			
//			System.out.println(i+": "+event.currTotal+" "+event.ndeath+" "+event.proportionSurvival);
			
			map.put(key, event);// update map
		}

		return map;
	}

	
	private Patient[] getSortedGroup() {
		int n = time.length;
		Patient[] group = new Patient[n];
		for (int i = 0; i < n; i++) {
			Patient p = new Patient(time[i], censor[i]);
			group[i] = p;

		}
		Arrays.sort(group, new PatientComparator());
		return group;
	}
	
	public static void main(String[] args){
		double[] time = { 1, 1, 4, 5, 6, 9, 9, 22 };
		double[] censor = { 1, 1, 1, 1, 0, 0, 1, 1 };
		
		KaplanMeierEstimate kap=new KaplanMeierEstimate(time,censor);
		double[] result=kap.estimate;
		
		for(double re: result){
			System.out.println(re+" ");
		}
	
	}


}
