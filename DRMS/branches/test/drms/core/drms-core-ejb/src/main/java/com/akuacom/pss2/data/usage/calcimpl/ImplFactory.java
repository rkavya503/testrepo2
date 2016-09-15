package com.akuacom.pss2.data.usage.calcimpl;

import java.util.HashMap;
import java.util.Map;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.irr.DataCalculationHandler;
import com.akuacom.pss2.data.usage.BaselineConfig;
import com.akuacom.pss2.program.apx.APXManager;

public class ImplFactory {
    private static ImplFactory s_instance = null;
    private Map<String, Object> impls = new HashMap<String, Object>();

    public static ImplFactory instance() {
        if (null == ImplFactory.s_instance) {
            ImplFactory.s_instance = new ImplFactory();
        }
        return ImplFactory.s_instance;
    }

//    public MissingDataGenerator getMissingDataGenerator() {
//        return getMissingDataGenerator(null);
//    }
//
//    public MissingDataGenerator getMissingDataGenerator(String beanName) {
//        try {
//            if (beanName == null || beanName.isEmpty()) {
//                beanName = "com.akuacom.pss2.data.usage.calcimpl.DefaultMissingDataGeneratorBean";
//            }
//
//            MissingDataGenerator ret = (MissingDataGenerator) impls
//                    .get(beanName);
//            Class clz = Class.forName(beanName);
//            if (ret == null) {
//                ret = (MissingDataGenerator) EJBFactory.getBean(clz);
//                impls.put(beanName, ret);
//            }
//            return (MissingDataGenerator) impls.get(beanName);
//        } catch (ClassNotFoundException e) {
//            return null;
//        }
//    }

    public BaselineMod getBaselineModel(String beanName) {
        try {
            if (beanName == null || beanName.isEmpty()) {
                beanName = "com.akuacom.pss2.data.usage.calcimpl.BaselineThreeTenModelBean";
            }

            BaselineMod ret = (BaselineMod) impls.get(beanName);
            Class clz = Class.forName(beanName);
            if (ret == null) {
                ret = (BaselineMod) EJBFactory.getBean(clz);
                impls.put(beanName, ret);
            }
            return (BaselineMod) impls.get(beanName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public BaselineCalculator getBaselineCalculation(String beanName) {
        try {
            if (beanName == null || beanName.isEmpty()) {
                beanName = "com.akuacom.pss2.data.usage.calcimpl.DefaultBaselineCalculatorBean";
            }

            BaselineCalculator ret = (BaselineCalculator) impls.get(beanName);
            Class clz = Class.forName(beanName);
            if (ret == null) {
                ret = (BaselineCalculator) EJBFactory.getBean(clz);
                impls.put(beanName, ret);
            }
            return (BaselineCalculator) impls.get(beanName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public DaysSelector getDaysSelector(String beanName) {
        try {
            if (beanName == null || beanName.isEmpty()) {
                beanName = "com.akuacom.pss2.data.usage.calcimpl.DefaultDaysSelectorBean";
            }

            DaysSelector ret = (DaysSelector) impls.get(beanName);
            Class clz = Class.forName(beanName);
            if (ret == null) {
                ret = (DaysSelector) EJBFactory.getBean(clz);
                impls.put(beanName, ret);
            }
            return (DaysSelector) impls.get(beanName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public AbnormalDayHandler getAbnormalDayHandler(String beanName) {
        try {
            if (beanName == null || beanName.isEmpty()) {
                beanName = "com.akuacom.pss2.data.usage.calcimpl.DefaultDaysSelectorBean";
            }

            AbnormalDayHandler ret = (AbnormalDayHandler) impls.get(beanName);
            Class clz = Class.forName(beanName);
            if (ret == null) {
                ret = (AbnormalDayHandler) EJBFactory.getBean(clz);
                impls.put(beanName, ret);
            }
            return (AbnormalDayHandler) impls.get(beanName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    private Object getBean(String beanName) throws ClassNotFoundException {
        Object bean = impls.get(beanName);

        if (bean == null) {
            Class<?> clz = Class.forName(beanName);
            bean = EJBFactory.getLocalBean(clz);
            impls.put(beanName, bean);
        }
        return bean;
    }
    
    public DataCalculationHandler getDataCalculationHandler(String beanName) {
        try {
            if (beanName == null || beanName.isEmpty()) {
                beanName = "com.akuacom.pss2.data.irr.UsageDataCalculationHandlerBean";
            }

            DataCalculationHandler ret = (DataCalculationHandler) impls.get(beanName);
            Class clz = Class.forName(beanName);
            if (ret == null) {
                ret = (DataCalculationHandler) EJBFactory.getBean(clz);
                impls.put(beanName, ret);
            }
            return (DataCalculationHandler) impls.get(beanName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    
    public APXManager getAPXManager(String utilityName) {
    	String beanName = null;
        try {
            if (utilityName == null || utilityName.isEmpty()) {
                beanName = "com.akuacom.pss2.program.apx.PgeAPXManagerBean"; //default pge implemention
            }else if(utilityName.equalsIgnoreCase("sdg")){
            	beanName = "com.akuacom.pss2.program.apx.SdgAPXManagerBean";
            }else{
            	beanName = "com.akuacom.pss2.program.apx.PgeAPXManagerBean";
            }

            APXManager ret = (APXManager) impls.get(beanName);
            Class clz = Class.forName(beanName);
            if (ret == null) {
                ret = (APXManager) EJBFactory.getBean(clz);
                impls.put(beanName, ret);
            }
            return (APXManager) impls.get(beanName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
