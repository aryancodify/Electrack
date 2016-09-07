package com.electra.processor;

import java.util.ArrayList;
import java.util.List;

import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.batch.item.ItemProcessor;

import com.electra.domain.PredictionBean;
import com.electra.domain.UserBean;
import com.electra.domain.UserBillBean;
import com.electra.service.ElectraService;


public class PredictionProcessor implements ItemProcessor<UserBean, PredictionBean>{
	private ElectraService electraService;
	private static RConnection rConnection;
	public PredictionProcessor(ElectraService electraService) {
		this.electraService = electraService;
	}

	/**
	 * @return the electraService
	 */
	public ElectraService getElectraService() {
		return electraService;
	}

	/**
	 * @param electraService the electraService to set
	 */
	public void setElectraService(ElectraService electraService) {
		this.electraService = electraService;
	}

	@Override
	public PredictionBean process(UserBean user) throws Exception {
		// TODO Auto-generated method stub
		List<UserBillBean> userBills = new ArrayList<>(0);
		PredictionBean predictionBean = new PredictionBean();
		predictionBean.setUserId(user.getUserId());
		userBills = electraService.getBill(user.getUserId());
		
		if(userBills == null || userBills.size()==0){
			return null;
		}
		if(userBills.size()==1){
			int month = userBills.get(0).getTimestamp().getMonthValue();
			int year = userBills.get(0).getTimestamp().getYear();
			userBills.clear();
			userBills = electraService.getDayData(user.getUserId(), month, year);
			return getPredictionMonthNotAvail(userBills,predictionBean);
		}else{
			return getPredictionMonthAvail(userBills,predictionBean);
		}
	}
	
	public PredictionBean getPredictionMonthAvail(List<UserBillBean> userBills,PredictionBean predictionBean){
		
		List<Double> prevUnits = new ArrayList<>();
		for(int i=0;i<userBills.size();i++){
			prevUnits.add(userBills.get(i).getMainUnit());
		}
		try{
			rConnection = new RConnection();
			String timeVector = "c(0";
			String unitVector = "c(";
			unitVector += String.valueOf(prevUnits.get(0));
			for(int i = 1;i<prevUnits.size();i++){
				unitVector+=","+String.valueOf(prevUnits.get(i));
				timeVector+=","+String.valueOf(i);
			}
			timeVector+=")";
			unitVector+=")";
			rConnection.eval("time=" + timeVector);
			rConnection.eval("units=" + unitVector);
			double[] coeff=rConnection.eval("coefficients(lm(units~time))").asDoubles();
			coeff[0] = (double) Math.round(coeff[0] * 100) / 100;
			coeff[1] = (double) Math.round(coeff[1] * 100) / 100;
			int unitsSize = prevUnits.size();
			for(int i=unitsSize;i<(unitsSize+2);i++){
				prevUnits.add(coeff[0]+coeff[1]*i);
			}
				
			switch(unitsSize){
			case 2:
				predictionBean.setStart("p1");
				predictionBean.setStartMonth(userBills.get(0).getTimestamp().getMonthValue());
				predictionBean.setP1(prevUnits.get(0));
				predictionBean.setCurrentMonth(prevUnits.get(1)-prevUnits.get(0));
				predictionBean.setNextMonth(prevUnits.get(2)-prevUnits.get(1));
				predictionBean.setNextToMonth(prevUnits.get(3)-prevUnits.get(2));
				break;
			case 3:
				predictionBean.setStart("p2");
				predictionBean.setStartMonth(userBills.get(0).getTimestamp().getMonthValue());
				predictionBean.setP2(prevUnits.get(0));
				predictionBean.setP1(prevUnits.get(1)-prevUnits.get(0));
				predictionBean.setCurrentMonth(prevUnits.get(2)-prevUnits.get(1));
				predictionBean.setNextMonth(prevUnits.get(3)-prevUnits.get(2));
				predictionBean.setNextToMonth(prevUnits.get(4)-prevUnits.get(3));
				break;
			case 4:
				predictionBean.setStart("p3");
				predictionBean.setStartMonth(userBills.get(0).getTimestamp().getMonthValue());
				predictionBean.setP3(prevUnits.get(0));
				predictionBean.setP2(prevUnits.get(1)-prevUnits.get(0));
				predictionBean.setP1(prevUnits.get(2)-prevUnits.get(1));
				predictionBean.setCurrentMonth(prevUnits.get(3)-prevUnits.get(2));
				predictionBean.setNextMonth(prevUnits.get(4)-prevUnits.get(3));
				predictionBean.setNextToMonth(prevUnits.get(5)-prevUnits.get(4));
				break;
			case 5 :
				predictionBean.setStart("p3");
				predictionBean.setStartMonth(userBills.get(1).getTimestamp().getMonthValue());
				predictionBean.setP3(prevUnits.get(1)-prevUnits.get(0));
				predictionBean.setP2(prevUnits.get(2)-prevUnits.get(1));
				predictionBean.setP1(prevUnits.get(3)-prevUnits.get(2));
				predictionBean.setCurrentMonth(prevUnits.get(4)-prevUnits.get(3));
				predictionBean.setNextMonth(prevUnits.get(5)-prevUnits.get(4));
				predictionBean.setNextToMonth(prevUnits.get(6)-prevUnits.get(5));
				break;
			case 6:
				predictionBean.setStart("p3");
				predictionBean.setStartMonth(userBills.get(2).getTimestamp().getMonthValue());
				predictionBean.setP3(prevUnits.get(2)-prevUnits.get(1));
				predictionBean.setP2(prevUnits.get(3)-prevUnits.get(2));
				predictionBean.setP1(prevUnits.get(4)-prevUnits.get(3));
				predictionBean.setCurrentMonth(prevUnits.get(5)-prevUnits.get(4));
				predictionBean.setNextMonth(prevUnits.get(6)-prevUnits.get(5));
				predictionBean.setNextToMonth(prevUnits.get(7)-prevUnits.get(6));
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			rConnection.close();
		}
		return predictionBean;
		
	}
	public PredictionBean getPredictionMonthNotAvail(List<UserBillBean> userBills,PredictionBean predictionBean){
		List<Double> prevUnits = new ArrayList<>();
		for(int i=0;i<userBills.size();i++){
			prevUnits.add(userBills.get(i).getMainUnit());
		}
		try{
			rConnection = new RConnection();
			String timeVector = "c(0";
			String unitVector = "c(";
			unitVector += String.valueOf(prevUnits.get(0));
			for(int i = 1;i<prevUnits.size();i++){
				unitVector+=","+String.valueOf(prevUnits.get(i));
				timeVector+=","+String.valueOf(i);
			}
			timeVector+=")";
			unitVector+=")";
			rConnection.eval("time=" + timeVector);
			rConnection.eval("units=" + unitVector);
			double[] coeff=rConnection.eval("coefficients(lm(units~time))").asDoubles();
			coeff[0] = (double) Math.round(coeff[0] * 100) / 100;
			coeff[1] = (double) Math.round(coeff[1] * 100) / 100;
			predictionBean.setCurrentMonth(coeff[1]*30);
			predictionBean.setNextMonth(coeff[1]*60-coeff[1]*30);
			predictionBean.setNextToMonth(coeff[1]*90-coeff[1]*60);
			predictionBean.setStartMonth(userBills.get(0).getTimestamp().getMonthValue());
			predictionBean.setStart("currentMonth");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			rConnection.close();
		}
		return predictionBean;
		
	}
}
