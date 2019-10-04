package com.dev.bss.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.dev.bbs.exceptions.CustomException;
import com.dev.bbs.exceptions.DeleteFailedException;
import com.dev.bbs.exceptions.UpdateFailedException;
import com.dev.bss.beans.Admin;
import com.dev.bss.beans.Available;
import com.dev.bss.beans.Bus;
import com.dev.bss.beans.Feedback;

public class DaoAdminImpl implements DaoAdmin {
	//Creating Global EntityManagerFactory
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("BusBookingSystem_jpa");
	//Add Bus
	@Override
	public Bus createBus(Bus bus) {
		try {
			EntityManager em = emf.createEntityManager();


			em.getTransaction().begin();
			em.persist(bus);
			em.getTransaction().commit();
			em.close();

			if(bus != null)
			{
				return bus;
			}
		} catch (Exception e) {
			throw new CustomException("CreateBusFailed");
		}
		return null;

	}
    //UPDATE BUS
	@Override
	public Boolean updateBus(Bus bus) {
		try {
			EntityManager em = emf.createEntityManager();

			em.getTransaction().begin();
			//FIND PARTICULAR BUS BASED ON ID TO UPDATE
			Bus bus1 = em.find(Bus.class, bus.getBusId());
			bus1.setBusName(bus.getBusName());
			bus1.setBusType(bus.getBusType());
			bus1.setDestination(bus.getDestination());
			bus1.setSource(bus.getSource());
			bus1.setPrice(bus.getPrice());
			bus1.setTotalSeats(bus.getTotalSeats());
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			throw new UpdateFailedException("FailedTOUpdate");
		}
	}

	//SEARCH BUS ON ID
	@Override
	public Bus searchBus(int busId) {
		 try {
			EntityManager em = emf.createEntityManager();
			   //SEARCH BUS BASED ON ID
			   Query query = em.createQuery("from Bus u where u.busId= :busid ");
			   query.setParameter("busid", busId );
			   em.getTransaction().begin();
			   List bus=query.getResultList();
			   em.getTransaction().commit();
			  Bus bus1= (Bus) bus.get(0);
			  return bus1;
		} catch (Exception e) {
			throw new CustomException("SearchBusException");
		}
	}
	//DELETE BUS
	@Override
	public Boolean deletebus(int busId, String password) {
		try {
			EntityManager em = emf.createEntityManager();
				em.getTransaction().begin();
				Bus bus = em.find(Bus.class,busId );
				em.remove(bus);
				em.getTransaction().commit();
				return true;
		} catch (Exception e) {
			throw new DeleteFailedException("DeleteFailed");
		}
		
	}
	//GET BUSES BETWEEN SOURCE AND DESTINATION
	@Override
	public List<Bus> busBetween(String source, String destination) {
		try {
			EntityManager em = emf.createEntityManager();
			Query query = em.createQuery("From Bus b where b.source = :source" + " and b.destination = :destination ");
			query.setParameter("source", source);
			query.setParameter("destination", destination);
			List<Bus> buses = query.getResultList();
			return buses;
		} catch (Exception e) {
			throw new CustomException("SearchBusException");
		}
	}
	//ADMIN LOGIN
	@Override
	public Boolean adminLogin(int adminId, String password) {
		try {
			EntityManager em = emf.createEntityManager();
			//CHECK FROM PASSWORD
			Query query = em.createQuery("from Admin a where a.adminId= :adminid and a.password= :password ");
			query.setParameter("adminid", adminId);
			query.setParameter("password", password);
			em.getTransaction().begin();
			List admin = query.getResultList();
			em.getTransaction().commit();
			Admin admin1 = (Admin) admin.get(0);
			return true;
		} catch (Exception e) {
			throw new com.dev.bbs.exceptions.LoginException("LoginFAILED");
		}
	}

	//ADD AVAILABILITY
	@Override
	public Boolean addAvailability(Available available) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(available);
		em.getTransaction().commit();
		return true;
	}
	//SHOW ALL FEEDBACKS
	@Override
	public List<Feedback> showFeedback() {
		EntityManager em = emf.createEntityManager();
		TypedQuery<Feedback> query = em.createQuery("Select f from Feedback f", Feedback.class);
		List<Feedback> feedbacks = query.getResultList();
		return feedbacks;
	}




}
