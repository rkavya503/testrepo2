package com.akuacom.pss2.openadr2.report;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;

@Stateless
public class ReportRequestEAOBean extends BaseEAOBean<ReportRequest> implements ReportRequestEAO.L,ReportRequestEAO.R {

	
	private Logger log = Logger.getLogger(ReportRequestManagerBean.class);
	
	public ReportRequestEAOBean() {
		super(ReportRequest.class);
	}

	@Override
	public ReportRequest create(ReportRequest entity)
			throws DuplicateKeyException {
		try{
			if(getEm()!=null) {			
				getEm().persist(entity);
				//getEm().merge(entity);
			}else{
				log.error("Entity manager is null");
			}
		}catch(Exception ex){
			log.error("Exception in persisting the report entity"+ ex);
		}
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public VenReportRequestHistory create(VenReportRequestHistory entity)
			throws DuplicateKeyException {
		try{
			if(getEm()!=null) {			
				getEm().persist(entity);
			}else{
				log.error("Entity manager is null");
			}
		}catch(ConstraintViolationException e){
			log.error("Constraint violation exception in storing venReportRequestHistory!! "+e);
		}catch(Exception ex){
			log.error("Exception in persisting the VenReportRequestHistory entity"+ ex);
		}
		return null;
	}
	
	
	@Override
	public Report findById(String reportRequestId) {
		return null;
	}

	@Override
	public void updateReport(Report report) {
		getEm().merge(report);
	}

	@Override
	public void updateReportRequest(ReportRequest reportRequest) {
		getEm().merge(reportRequest);
	}
	
	@Override
	public void updateVenReportRequestHistory(VenReportRequestHistory venReportRequestHistory) {
		getEm().merge(venReportRequestHistory);
	}

	@Override
	public ReportRequest findByVenIdandReportRequestId(String venId,
			String reportRequestId) throws NoResultException {
		Query createNamedQuery = getEm().createNamedQuery("ReportRequest.findByVenIdandReportRequestId");
		createNamedQuery.setParameter("venId", venId);
		createNamedQuery.setParameter("reportRequestId", reportRequestId);
		
		ReportRequest reportRequest = (ReportRequest) createNamedQuery.getSingleResult();
		
		return reportRequest;
	}
	
	@Override
	public VenReportRequestHistory findVenReportRequestHistoryByVenIdandReportRequestId(String venId,
			String reportRequestId) throws NoResultException {
		Query createNamedQuery = getEm().createNamedQuery("VenReportRequestHistory.findByVenIdandReportRequestId");
		createNamedQuery.setParameter("venId", venId);
		createNamedQuery.setParameter("reportRequestId", reportRequestId);
		
		VenReportRequestHistory venReportRequestHistory = (VenReportRequestHistory) createNamedQuery.getSingleResult();
		
		return venReportRequestHistory;
	}
	
	@Override
	public VenReportRequestHistory findVenReportRequestHistoryByVenIdandReportRequestIds(String venId,
			List<String> reportRequestId) throws EntityNotFoundException {
		Query createNamedQuery = getEm().createNamedQuery("VenReportRequestHistory.findByVenIdandReportRequestIds");
		createNamedQuery.setParameter("venId", venId);
		createNamedQuery.setParameter("reportRequestId", reportRequestId);
		
		VenReportRequestHistory venReportRequestHistory = (VenReportRequestHistory) createNamedQuery.getSingleResult();
		
		return venReportRequestHistory;
	}
	
	@Override
	public List<VenReportRequestHistory> findVenReportRequestHistoryByVenIdandRequestId(String venId,
			String requestId) throws NoResultException {
		Query createNamedQuery = getEm().createNamedQuery("VenReportRequestHistory.findByVenIdandReportRequestId");
		createNamedQuery.setParameter("venId", venId);
		createNamedQuery.setParameter("cancelRequestId", requestId);
		List<VenReportRequestHistory> resultList = createNamedQuery.getResultList();
		return resultList;
	}
	
	@Override
	public ReportRequest findByVenIdandReportRequestIds(String venId,
			List<String> reportRequestId) throws EntityNotFoundException {
		Query createNamedQuery = getEm().createNamedQuery("ReportRequest.findByVenIdandReportRequestIds");
		createNamedQuery.setParameter("venId", venId);
		createNamedQuery.setParameter("reportRequestId", reportRequestId);
		
		ReportRequest reportRequest = (ReportRequest) createNamedQuery.getSingleResult();
		
		return reportRequest;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public ReportRequest findByVenId(String venId) throws EntityNotFoundException {

		try {
			Query createNamedQuery = getEm().createNamedQuery("ReportRequest.findByVenId");
			createNamedQuery.setParameter("venId", venId);
			ReportRequest reportRequest = (ReportRequest) createNamedQuery.getSingleResult();
			return reportRequest;
		} catch (NoResultException e) {
			log.error("No ReportRequest found for the VenId: "+ venId);
			throw new EntityNotFoundException(e);
		}
	}
	
	@Override
	public void deleteReportRequest(ReportRequest reportRequest) {
		try{
			ReportRequest result = getEm().find(ReportRequest.class, reportRequest.getUUID());
			getEm().remove(result);
		}catch(Exception e){
			log.error("Exception in deleteReportRequest !! "+e);
		}
	}
	
	@Override
	public void deleteReport(Report report) {
		try{
			Report result = getEm().find(Report.class, report.getUUID());
			getEm().remove(result);
		}catch(Exception e){
			log.error("Exception in deleteReport !! "+e);
		}
	}
	
	@Override
	public ReportRequest findCreateReportMessagesByVenId(String venId)
			throws EntityNotFoundException {
		
		ReportRequest reportRequest = null;
		try {
			Query createNamedQuery = getEm().createNamedQuery("ReportRequest.findCreateReportMessagesByVenIdNativeSql");
			createNamedQuery.setParameter("venId", venId);
			reportRequest = (ReportRequest) createNamedQuery.getSingleResult();
		} catch (NoResultException e) {
			log.error("Unable to find CreateReport message for venid :"+ venId);
		}
		return reportRequest;
	}
	
	@Override
	public ReportRequest findCancelReportMessagesByVenId(String venId)
			throws EntityNotFoundException {

		ReportRequest reportRequest = null;
		try{
			Query createNamedQuery = getEm().createNamedQuery("ReportRequest.findCancelReportMessagesByVenIdNativeSql");
			createNamedQuery.setParameter("venId", venId);
			reportRequest = (ReportRequest) createNamedQuery.getSingleResult();
		} catch (NoResultException e) {
			log.error("Unable to find CancelReport message for venid :"+ venId);
		}
		return reportRequest;
	}

	@Override
	public ReportRequest findOnlyReportRequestByVenId(String venId) {
		Query createNamedQuery = getEm().createNamedQuery("ReportRequest.findOnlyReportRequestByVenId");
		createNamedQuery.setParameter("venId", venId);
		ReportRequest reportRequest = (ReportRequest) createNamedQuery.getSingleResult();
		
		return reportRequest;
	}

	@Override
	public List<Report> findOnlyReportByReportRequest_uuid(String reportRequest_uuid,
			Boolean isCreated, Boolean isCancelled) {
		Query createNamedQuery = getEm().createNamedQuery("ReportRequest.findOnlyReportByReportRequest_uuid");
		createNamedQuery.setParameter("reportRequest_uuid", reportRequest_uuid);
		createNamedQuery.setParameter("isCreated", isCreated);
		createNamedQuery.setParameter("isCancelled", isCancelled);
		List<Report> resultList = createNamedQuery.getResultList();
		
		return resultList;
	}

	@Override
	public List<ReportDescription> findOnlyReportDescriptionByReport_uuid(
			String report_uuid, Boolean isCreated, Boolean isCancelled) {
		Query createNamedQuery = getEm().createNamedQuery("ReportRequest.findOnlyReportDescriptionByReport_uuid");
		createNamedQuery.setParameter("report_uuid", report_uuid);
		createNamedQuery.setParameter("isCreated", isCreated);
		createNamedQuery.setParameter("isCancelled", isCancelled);
		List<ReportDescription> resultList = createNamedQuery.getResultList();
		
		return resultList;
	}

	
	@Override
	public ReportRequest findCreatedReportsNotCancelledByVenId(String venId)
			throws EntityNotFoundException {
		Query createNamedQuery = getEm().createNamedQuery("ReportRequest.findCreatedButNotCancelledReportByVenId");
		createNamedQuery.setParameter("venId", venId);
		createNamedQuery.setParameter("isCreated", true);
		createNamedQuery.setParameter("isCancelled", false);
		//JPA 2.0
		//Session session = getEm().unwrap(Session.class);
		//JPA 1.0
		Session session = (Session) getEm().getDelegate();
		
		Filter reportFilter = session.enableFilter("reportCreatedNotCancelledFilter");
		reportFilter.setParameter("isCreated", true);
		reportFilter.setParameter("isCancelled", false);
		
		Filter reportDescriptionFilter = session.enableFilter("reportDescriptionCreatedNotCancelledFilter");
		reportDescriptionFilter.setParameter("isCreated", true);
		reportDescriptionFilter.setParameter("isCancelled", false);
		ReportRequest reportRequest =null;
		try{
			reportRequest  = (ReportRequest) createNamedQuery.getSingleResult();
		}catch(NoResultException e){
			log.error("ReportRequest not found for the given venid !!");
		}catch (Exception e) {
			//catching all exceptions so we can disable the filter
			log.error("Exception in findCreatedReportsNotCancelledByVenId !!"+ e);
		}
		
		session.disableFilter("reportCreatedNotCancelledFilter");
		session.disableFilter("reportDescriptionCreatedNotCancelledFilter");
		
		return reportRequest;
	}

	@Override
	public String findResourceIdfromReportDataSourceByrId(String rId,String reportSpecifierId,String venId) throws NoResultException, NonUniqueResultException{
		Query createNamedQuery = getEm().createNamedQuery("ReportDescription.findResourceIdfromReportDataSourceByrId");
		createNamedQuery.setParameter("rId", rId);
		createNamedQuery.setParameter("reportSpecifierId", reportSpecifierId);
		createNamedQuery.setParameter("venId", venId);
	    String resourceId = (String) createNamedQuery.getSingleResult();
		return resourceId;
	}	
}
